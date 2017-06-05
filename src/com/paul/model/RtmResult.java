package com.paul.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RtmResult {
	
	public RtmResult(String ruleName, String conditionSet, String keyCondition, List<String> keyParameters, String buildId){
		this.ruleName.set(ruleName);
		this.conditionSet.set(conditionSet);
		this.keyCondition.set(keyCondition);
		this.keyParameters.set(FXCollections.observableArrayList(keyParameters));
		this.buildId.set(buildId);
	}
	
	private SimpleStringProperty ruleName = new SimpleStringProperty("");
	private SimpleStringProperty conditionSet = new SimpleStringProperty("");
	private SimpleStringProperty keyCondition = new SimpleStringProperty("");
	private SimpleListProperty<String> keyParameters = new SimpleListProperty<String>();
	private SimpleStringProperty buildId = new SimpleStringProperty("");
	
	private SimpleListProperty<String> keyParametersChoice = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<String>()));
	
		
	public String getRuleName() {
		return ruleName.get();
	}
	public StringProperty getRuleNameProperty(){
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName.set(ruleName);
	}
	
	public String getConditionSet() {
		return conditionSet.get();
	}
	public StringProperty getConditionSetProperty(){
		return conditionSet;
	}
	public void setConditionSet(String conditionSet) {
		this.conditionSet.set(conditionSet);;
	}
	
	public String getKeyCondition() {
		return keyCondition.get();
	}
	public StringProperty getKeyConditionProperty(){
		return keyCondition;
	}
	public void setKeyCondition(String keyCondition) {
		this.keyCondition.set(keyCondition);
	}
	
	public List<String> getKeyParameters() {
		return keyParameters.get();
	}
	public ListProperty getKeyParametersProperty(){
		return keyParameters;
	}
	public void setKeyParameters(List<String> keyParameters) {
		this.keyParameters.set(FXCollections.observableArrayList(keyParameters));
	}
	
	public String getBuildId() {
		return buildId.get();
	}
	public StringProperty getBuildIdProperty(){
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId.set(buildId);
	}
	
	
	public List<String> getKeyParametersChoice() {
		return keyParametersChoice.get();
	}
	public ListProperty<String> getKeyParametersChoiceProperty() {
		return keyParametersChoice;
	}
	public void setKeyParametersChoice(ObservableList<String> keyParametersChoice) {
		this.keyParametersChoice.set(keyParametersChoice);
	}
	
	@Override
	public String toString() {
		return String.format(
				"%s {\n\truleName: %s\n\tconditionSet: %s\n\tkeyCondition: %s\n\tkeyParameters: %s\n\tbuildId: %s\n}",
				getClass().getName(), ruleName, conditionSet, keyCondition, keyParameters, buildId);
	}

}
