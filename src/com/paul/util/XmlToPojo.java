package com.paul.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class XmlToPojo {
	
	private XmlToPojo(){}
	
	public static Object getPojo(File xmlFile, Object pojo){
		try {
			JAXBContext jaxbContent = JAXBContext.newInstance(pojo.getClass());
			
			Unmarshaller unmarshaller = jaxbContent.createUnmarshaller();
			return unmarshaller.unmarshal(xmlFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
