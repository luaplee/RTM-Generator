package com.paul.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="rule")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(ConditionSet.class)
public class Rule extends BaseModel {
	
	@XmlAttribute(name = "isActive")
	private boolean isActive;
	@XmlElement(name = "conditionset")
	private List<ConditionSet> conditionSet;
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public List<ConditionSet> getConditionSet() {
		return conditionSet;
	}
	public void setConditionSet(List<ConditionSet> conditionSet) {
		this.conditionSet = conditionSet;
	}
	
	@Override
	public String toString() {
		return String.format("%s {\n\tisActive: %s\n\tconditionSet: %s\n\ttoString: %s\n}", getClass().getName(),
				isActive, conditionSet, super.toString());
	}

}
