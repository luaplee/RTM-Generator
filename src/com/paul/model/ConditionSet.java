package com.paul.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "conditionset")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Condition.class)
public class ConditionSet {
	
	@XmlAttribute(name = "description")
	private String description;
	@XmlElement(name = "condition")
	private List<Condition> condition;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Condition> getParameter() {
		return condition;
	}
	public void setParameter(List<Condition> parameter) {
		this.condition = parameter;
	}
	public List<Condition> getCondition() {
		return condition;
	}
	public void setCondition(List<Condition> condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return String.format("%s {\n\tdescription: %s\n\tparameter: %s\n}", getClass().getName(), description,
				condition);
	}

}
