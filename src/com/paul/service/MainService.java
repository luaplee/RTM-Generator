package com.paul.service;

import java.util.List;

import com.paul.model.CalcGroup;
import com.paul.model.RtmResult;
import com.paul.model.Rule;

public interface MainService {
	
	public List<RtmResult> getRtmResult(List<Rule> eligibleRules);
	
	public List<Rule> getEligibleRules(CalcGroup calcGroup, String segmentCode);
	
}
