package com.paul.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paul.model.CalcGroup;
import com.paul.model.Condition;
import com.paul.model.ConditionSet;
import com.paul.model.Parameter;
import com.paul.model.RtmResult;
import com.paul.model.Rule;

public class RtmUtil {
	
	private RtmUtil(){}
	
	//TODO: remove static hardcodes and incorporate in a property file
	private static final String RULE_SEGMENT_CODE = "RL";
	private static final String CONDITION_SET_SEGMENT_CODE = "CS";
	private static final String KEY_CONDITION_SEGMENT_CODE = "CN";
	private static final String CODE_ERROR_STIRNG = "CODE ERROR";
	private static final String DESCRIPTION_CODE_DELIMITER = "-";
	private static final String SEGMENT_CODE_PREFIX = ".";
	private static final String DESCRIPTION_ATTRIBUTE = "description";
	
	public static List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode){
		List<Rule> eligibleRules = new ArrayList<Rule>();
		for(Rule rule : calcGroup.getRule()){
			if(rule.getDescription() != null){
				String ruleDescription = rule.getDescription();
				if(isRuleEligible(ruleDescription, segmentCode)){
					eligibleRules.add(rule);
				} 
			}
		}
		return eligibleRules;
	}
	
	public static List<Node> getEligibleRules(NodeList rules, String segmentCode){
		List<Node> eligibleRules = new ArrayList<Node>();
		for(int i = 0; i < rules.getLength(); i++){
			Node rule = rules.item(i);
			NamedNodeMap attributes = rule.getAttributes();
			Node ruleNode = attributes.getNamedItem(DESCRIPTION_ATTRIBUTE);
			if(ruleNode != null){
				if(isRuleEligible(ruleNode.getNodeValue(), segmentCode)){
					eligibleRules.add(rule);
				}
			}
		}
		return eligibleRules;
	}
	
	public static List<RtmResult> getRtmResult(List<Rule> eligibleRules){
		List<RtmResult> rtmResultList = new ArrayList<RtmResult>();
		
		for(Rule rule : eligibleRules){
			String ruleDescription = rule.getDescription();
			String ruleSegmentCode = getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE);
			String ruleName = rule.getName() + " - " + ruleSegmentCode;
			
			for(ConditionSet conditionSet : rule.getConditionSet()){
				String conditionSetDescription = conditionSet.getDescription();
				String conditionSetSegmentCode = getValidSegmentCode(conditionSetDescription, SEGMENT_CODE_PREFIX, CONDITION_SET_SEGMENT_CODE);
				
				for(Condition condition : conditionSet.getCondition()){
					String conditionDescription = condition.getDescription();
					String keyConditionSegmentCode = getValidSegmentCode(conditionDescription, SEGMENT_CODE_PREFIX, KEY_CONDITION_SEGMENT_CODE);
					String conditionName = condition.getName() + " - " + getValidSegmentCode(conditionDescription, "", KEY_CONDITION_SEGMENT_CODE);;
					
					List<String> keyParameterList = new ArrayList<>();
					if(condition.getParameter() != null){
						for(Parameter parameter : condition.getParameter()){
							if(parameter != null && !parameter.getValue().isEmpty() && !parameter.getName().isEmpty()){
								keyParameterList.add(parameter.getName() + " - " + parameter.getValue());
							}
						}
					}
					
					//TODO: create a validator to validate the sequence of segment code
					StringBuilder buildId = new StringBuilder();
					buildId.append(ruleSegmentCode);
					buildId.append(conditionSetSegmentCode);
					buildId.append(keyConditionSegmentCode);
					
					RtmResult rtmResult = new RtmResult(ruleName, conditionSetDescription, 
							conditionName, keyParameterList, buildId.toString());
					
					rtmResultList.add(rtmResult);
				}
			}
		}
		
		return rtmResultList;
	}
	
	
	//TODO: MOVE THIS TO SEGMENT UTIL
	public static Document appendConditionSegmentCodes(File xmlFile){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFile);
			NodeList rules = doc.getElementsByTagName("rule");
			List<Node> eligibleRuleList = getEligibleRules(rules, "");
			for(Node eligibleRule : eligibleRuleList){
				NodeList conditionSetList = eligibleRule.getChildNodes();
				appendSegmentCode(conditionSetList, "conditionset", "condition");
			}
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(xmlFile);
//			transformer.transform(source, result);
			return doc;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO: 2nd varargs parameters can only handle up to 2, fix this in the future
	private static void appendSegmentCode(NodeList nodeList, String... xmlTag){
		int segmentCodeCounter = 1;
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			//Checking for the varargs of xml tags, limitation is up to length of 2, consider refactoring this method and remove recursiveness
			if(xmlTag[0] != null && xmlTag[0].equals(node.getNodeName())){
				String segmentCodeWithCounter = getSegmentCodeOfXmlTag(xmlTag[0]) + segmentCodeCounter;
				String segmentCodeForAppend = " " + DESCRIPTION_CODE_DELIMITER + " " + segmentCodeWithCounter;
				Element element = (Element) node;
				//Trim the description to remove extra spaces(trailing and leading)
				String descriptionAttribute = element.getAttribute(DESCRIPTION_ATTRIBUTE) != null ? element.getAttribute(DESCRIPTION_ATTRIBUTE).trim() : "";
				String checkCode = getValidSegmentCode(descriptionAttribute, "", getSegmentCodeOfXmlTag(xmlTag[0]));
				//Checks if the current description have a segment code, if yes don't append.
				if(checkCode.isEmpty() || checkCode.contains(CODE_ERROR_STIRNG)){
					element.setAttribute(DESCRIPTION_ATTRIBUTE, (descriptionAttribute + segmentCodeForAppend));
					segmentCodeCounter++;
					if(xmlTag.length > 1){
						appendSegmentCode(node.getChildNodes(), xmlTag[1]);
					}
				} else if(!checkCode.equals(segmentCodeWithCounter)){
					element.setAttribute(DESCRIPTION_ATTRIBUTE, (descriptionAttribute.replace(checkCode, segmentCodeWithCounter)));
					segmentCodeCounter++;
					if(xmlTag.length > 1){
						appendSegmentCode(node.getChildNodes(), xmlTag[1]);
					}
				} else if(checkCode.equals(segmentCodeWithCounter)){
					segmentCodeCounter++;
					if(xmlTag.length > 1){
						appendSegmentCode(node.getChildNodes(), xmlTag[1]);
					}
				}
			}
		}
	}
	
	private static String getSegmentCodeOfXmlTag(String xmlTag){
		switch(xmlTag){
		case "rule":
			return RULE_SEGMENT_CODE;
		case "conditionset":
			return CONDITION_SET_SEGMENT_CODE;
		case "condition":
			return KEY_CONDITION_SEGMENT_CODE;
		default:
			return "";
		}
	}
	
	private static String getSegmentCode(String description) throws NumberFormatException{
		String value = "";
		Integer conditionNumber;
		if(description != null && description.lastIndexOf(DESCRIPTION_CODE_DELIMITER) != -1
				&& (description.indexOf(RULE_SEGMENT_CODE) != -1 || 
				description.indexOf(CONDITION_SET_SEGMENT_CODE) != -1 || 
				description.indexOf(KEY_CONDITION_SEGMENT_CODE) != -1)){
			return (description.substring(description.lastIndexOf(DESCRIPTION_CODE_DELIMITER) + 1, description.length())).trim();
		} else if(description.indexOf(RULE_SEGMENT_CODE) != -1){
			 conditionNumber = Integer.valueOf(description.substring(description.indexOf(RULE_SEGMENT_CODE) + RULE_SEGMENT_CODE.length(), description.length()));
			 if(conditionNumber != null)
				 value = (description.substring(description.lastIndexOf(RULE_SEGMENT_CODE) - 4, description.length())).trim(); 
				 
		}else if(description.indexOf(CONDITION_SET_SEGMENT_CODE) != -1){
			 conditionNumber = Integer.valueOf(description.substring(description.indexOf(CONDITION_SET_SEGMENT_CODE) + CONDITION_SET_SEGMENT_CODE.length(), description.length()));
			if(conditionNumber != null)
					value = (description.substring(description.lastIndexOf(CONDITION_SET_SEGMENT_CODE) , description.length())).trim();
		}else if(description.indexOf(KEY_CONDITION_SEGMENT_CODE) != -1){
			 conditionNumber = Integer.valueOf(description.substring(description.indexOf(KEY_CONDITION_SEGMENT_CODE) + KEY_CONDITION_SEGMENT_CODE.length(), description.length()));
				if(conditionNumber != null)
					value = (description.substring(description.lastIndexOf(KEY_CONDITION_SEGMENT_CODE) , description.length())).trim();
		}
		return value;
	}
	
	private static String getValidSegmentCode(String description, String prefix, String validationString){
		String segmentCode = getSegmentCode(description);
		if(isSegmentCodeValid(segmentCode, validationString)){
			return prefix + segmentCode;
		}
		return prefix + CODE_ERROR_STIRNG;
	}
	
	private static boolean isSegmentCodeValid(String segmentCode, String validationString){
		int prefixIndex = segmentCode.indexOf(SEGMENT_CODE_PREFIX);
		int indexStart = prefixIndex > -1 ? prefixIndex + 1 : 0;
		if(segmentCode != null && !segmentCode.isEmpty()){
			String segmentCodeWithoutNumber = (segmentCode.substring(indexStart, validationString.length() + indexStart)).trim();
			String segmentNumber = segmentCode.substring(validationString.length() + indexStart, segmentCode.length()).trim();
			if(!validationString.isEmpty()
					&& segmentCodeWithoutNumber.equals(validationString)
					&& StringUtil.isInteger(segmentNumber)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isRuleEligible(String ruleDescription, String segmentCode){
		if(getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE).contains(segmentCode)
				&& !getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE).contains(CODE_ERROR_STIRNG)){
			return true;
		}
		return false;
	}
	
}
