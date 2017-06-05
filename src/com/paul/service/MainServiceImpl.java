package com.paul.service;

import java.util.ArrayList;
import java.util.List;

import com.paul.model.CalcGroup;
import com.paul.model.Condition;
import com.paul.model.ConditionSet;
import com.paul.model.Parameter;
import com.paul.model.RtmResult;
import com.paul.model.Rule;

public class MainServiceImpl extends BaseService implements MainService {
	
	@Override
	public List<RtmResult> getRtmResult(List<Rule> eligibleRules){
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
					
					List<String> keyParameterList = getConditionParameterList(condition);
					
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
	
	@Override
	public List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode){
		return getEligibleRules(calcGroup, segmentCode);
	}
	
	private static List<String> getConditionParameterList(Condition condition){
		List<String> keyParameterList = new ArrayList<>();
		if(condition.getParameter() != null){
			for(Parameter parameter : condition.getParameter()){
				if(parameter != null && !parameter.getValue().isEmpty() && !parameter.getName().isEmpty()){
					keyParameterList.add(parameter.getName() + " - " + parameter.getValue());
				}
			}
		}
		return keyParameterList;
	}

	

}
