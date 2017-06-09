package com.paul.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paul.model.CalcGroup;
import com.paul.model.Condition;
import com.paul.model.ConditionSet;
import com.paul.model.ExcelModel;
import com.paul.model.Parameter;
import com.paul.model.RtmResult;
import com.paul.model.Rule;
import com.paul.service.excel.ExcelService;

import javafx.stage.Stage;

@Singleton
public class MainServiceImpl extends BaseService implements MainService {
	
	@Inject
	ExcelService excelService;
	
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
	
	@Override
	public List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode){
		return super.getEligibleRules(calcGroup, segmentCode);
	}
	
	@Override
	public File getNewFileLocation(Stage ownerStage, String dialogTitle){
		return super.getNewFileLocation(ownerStage, dialogTitle);
	}
	
	@Override
	public void exportExcelFile(List<RtmResult> rules, File newFileLocation) throws Exception{
		ExcelModel excelModel = new ExcelModel
								.ExcelBuilder("RTM")
								.header("Rule Name + [Rule Code]",
										"Condition Set + [Condition Set Code]",
										"Key Condition + [Condition Code]",
										"Key Parameters",
										"Build ID")
								.data(rules)
								.file(newFileLocation)
								.build();
		
		excelService.exportExcel(excelModel);
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
