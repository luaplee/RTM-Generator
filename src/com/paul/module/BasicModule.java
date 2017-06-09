package com.paul.module;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.paul.controller.SegmentController;
import com.paul.service.BaseService;
import com.paul.service.MainService;
import com.paul.service.MainServiceImpl;
import com.paul.service.excel.ExcelService;
import com.paul.service.excel.ExcelServiceImpl;
import com.paul.service.segment.SegmentCodeService;
import com.paul.service.segment.SegmentCodeServiceImpl;

public class BasicModule extends AbstractModule {

	@Override
	protected void configure(){
		Names.bindProperties(binder(), getPropertyFile());
		bind(SegmentController.class);
		bind(BaseService.class);
		bind(SegmentCodeService.class).to(SegmentCodeServiceImpl.class);
		bind(MainService.class).to(MainServiceImpl.class);
		bind(ExcelService.class).to(ExcelServiceImpl.class);
	}
	
	private Properties getPropertyFile(){
		Properties properties = new Properties();
		try{
			properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		return properties;
	}
}
