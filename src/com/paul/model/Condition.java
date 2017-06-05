package com.paul.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "condition")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Parameter.class)
public class Condition extends BaseModel {
	
	@XmlAttribute
	private String negate;
	@XmlElement
	private List<Parameter> parameter;
	
	public String getNegate() {
		return negate;
	}
	public void setNegate(String negate) {
		this.negate = negate;
	}
	public List<Parameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public String toString() {
		return String.format("%s {\n\tnegate: %s\n\tparameter: %s\n\ttoString: %s\n}", getClass().getName(), negate,
				parameter, super.toString());
	}

}
