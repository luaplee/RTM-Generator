package com.paul.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.paul.model.CalcGroup;
import com.paul.model.Rule;
import com.paul.util.StringUtil;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

@Singleton
public class BaseService {

	@Inject @Named("build-code.rule")
	protected String RULE_SEGMENT_CODE;
	@Inject @Named("build-code.condition.set")
	protected String CONDITION_SET_SEGMENT_CODE;
	@Inject @Named("build-code.condition")
	protected String KEY_CONDITION_SEGMENT_CODE = "CN";
	@Inject @Named("build-code.error")
	protected String CODE_ERROR_STIRNG = "CODE ERROR";
	@Inject @Named("build-code.delimeter")
	protected String DESCRIPTION_CODE_DELIMITER = "-";
	@Inject @Named("build-code.prefix")
	protected String SEGMENT_CODE_PREFIX = ".";
	@Inject @Named("build-code.attribute.tag")
	protected String DESCRIPTION_ATTRIBUTE = "description";
	
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
	
	public File getNewFileLocation(Stage ownerStage, String dialogTitle){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(dialogTitle);
		return directoryChooser.showDialog(ownerStage);
	}
	
	protected List<Node> getEligibleRules(NodeList rules, String segmentCode){
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
	
	protected boolean isRuleEligible(String ruleDescription, String segmentCode){
		if(getValidSegmentCode(ruleDescription, "", RULE_SEGMENT_CODE).contains(segmentCode)
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
	
	protected String getSegmentCode(String description) throws NumberFormatException{
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
	
	protected boolean isSegmentCodeValid(String segmentCode, String validationString){
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
