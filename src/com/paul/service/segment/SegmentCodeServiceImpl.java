package com.paul.service.segment;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paul.service.BaseService;

public class SegmentCodeServiceImpl extends BaseService implements SegmentCodeService{
	
	//TODO: 2nd varargs parameters can only handle up to 2, fix this in the future
	private void appendSegmentCode(NodeList nodeList, String... xmlTag){
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
	
	@Override
	public Document appendConditionSegmentCodes(File xmlFile){
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
	
	private String getSegmentCodeOfXmlTag(String xmlTag){
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

}
