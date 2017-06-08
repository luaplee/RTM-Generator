package com.paul.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.paul.model.CalcGroup;
import com.paul.model.Condition;
import com.paul.model.ConditionSet;
import com.paul.model.Parameter;
import com.paul.model.RtmResult;
import com.paul.model.Rule;

import javafx.stage.Stage;

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
	public void exportExcelFile(List<Rule> rules, File newFileLocation) throws FileNotFoundException , IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("RTM");
		int rowCount = 0;
		Row row = sheet.createRow(++rowCount);
		writeRow(row, 
				"Rule Name + [Rule Code]",
				"Condition Set + [Condition Set Code]",
				"Key Condition + [Condition Code]",
				"Key Parameters",
				"Build ID");
		
		try{
			FileOutputStream outputStream = new FileOutputStream(newFileLocation);
			workbook.write(outputStream);
		} finally {
			workbook.close();
		}
		
	}
	
	private void writeRow(Row row, String... cellValues){
		int cellCount = 0;
		Cell cell = row.createCell(cellCount);
		for(String cellValue : cellValues){
			cell.setCellValue(cellValue);
			cell = row.createCell(cellCount++);
		}
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
