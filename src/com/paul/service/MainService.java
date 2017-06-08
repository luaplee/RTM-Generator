package com.paul.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.paul.model.CalcGroup;
import com.paul.model.RtmResult;
import com.paul.model.Rule;

import javafx.stage.Stage;

public interface MainService {
	
	public List<RtmResult> getRtmResult(List<Rule> eligibleRules);
	
	public List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode);
	
	public File getNewFileLocation(Stage ownerStage, String dialogTitle);
	
	public void exportExcelFile(List<Rule> rules, File newFileLocation) throws FileNotFoundException , IOException;
	
}
