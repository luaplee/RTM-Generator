package com.paul.service;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.paul.model.CalcGroup;
import com.paul.model.Rule;
import com.paul.util.StringUtil;

public class BaseService {
	
	//TODO: remove static hardcodes and incorporate in a property file
	protected static final String RULE_SEGMENT_CODE = "RL";
	protected static final String CONDITION_SET_SEGMENT_CODE = "CS";
	protected static final String KEY_CONDITION_SEGMENT_CODE = "CN";
	protected static final String CODE_ERROR_STIRNG = "CODE ERROR";
	protected static final String DESCRIPTION_CODE_DELIMITER = "-";
	protected static final String SEGMENT_CODE_PREFIX = ".";
	protected static final String DESCRIPTION_ATTRIBUTE = "description";
	
	public List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode){
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
	
	public List<Node> getEligibleRules(NodeList rules, String segmentCode){
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
	
	private boolean isRuleEligible(String ruleDescription, String segmentCode){
		if(ruleDescription.lastIndexOf(DESCRIPTION_CODE_DELIMITER) != -1 
				&& getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE).contains(segmentCode)
				&& !getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE).contains(CODE_ERROR_STIRNG)){
			return true;
		}
		return false;
	}
	
	protected String getValidSegmentCode(String description, String prefix, String validationString){
		String segmentCode = getSegmentCode(description);
		if(isSegmentCodeValid(segmentCode, validationString)){
			return prefix + segmentCode;
		}
		return prefix + CODE_ERROR_STIRNG;
	}
	
	private String getSegmentCode(String description){
		if(description != null && description.lastIndexOf(DESCRIPTION_CODE_DELIMITER) != -1){
			return (description.substring(description.lastIndexOf(DESCRIPTION_CODE_DELIMITER) + 1, description.length())).trim();
		}
		return "";
	}
	
	private boolean isSegmentCodeValid(String segmentCode, String validationString){
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

}
