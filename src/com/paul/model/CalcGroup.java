package com.paul.model;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name = "calcgroup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Rule.class)
public class CalcGroup extends BaseModel {
	
	@XmlElement
	private List<Rule> rule;
	
	public List<Rule> getRule() {
		return rule;
	}
	public void setRule(List<Rule> rule) {
		this.rule = rule;
	}
	
	public boolean isEmpty(){
		return rule.isEmpty();
	}
	
	public static void main(String[] args) {
		CalcGroup calcGroup = new CalcGroup();
		ClassLoader classLoader = calcGroup.getClass().getClassLoader();
		
		try {
			File file = new File(classLoader.getResource("file.xml").getFile());
			JAXBContext jaxbContent = JAXBContext.newInstance(CalcGroup.class);
			
			Unmarshaller unmarshaller = jaxbContent.createUnmarshaller();
			calcGroup = (CalcGroup) unmarshaller.unmarshal(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String toString() {
		return String.format("%s {\n\trule: %s\n\ttoString: %s\n}", getClass().getName(), rule, super.toString());
	}
	
}
