package com.paul.model;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelModel {
	
	private XSSFWorkbook workbook = new XSSFWorkbook();
	private String sheetName;
	private String[] headers;
	private List<RtmResult> sheetContents;
	private File excelFile;
	
	private ExcelModel(ExcelBuilder excelBuilder){
		this.sheetName = excelBuilder.sheetName;
		this.headers = excelBuilder.headers;
		this.sheetContents = excelBuilder.sheetContents;
		this.excelFile = excelBuilder.excelFile;
	}
	
	public XSSFWorkbook getWorkbook(){
		return workbook;
	}
	
	public String getSheetname(){
		return sheetName;
	}
	
	public String[] getHeaders(){
		return headers;
	}
	
	public List<RtmResult> getSheetContents(){
		return sheetContents;
	}
	
	public File getExcelFile(){
		return excelFile;
	}
	
	public int getMaxHeaderCharacter(){
		if(headers.length > 0){
			Arrays.sort(headers, new Comparator<String>(){
				@Override
				public int compare(String o1, String o2) {
					return Integer.compare(o2.length(), o1.length());
				}
			});
			return headers[0].length();
		}
		return 0;
	}
	
	
	public static class ExcelBuilder{
		private String sheetName;
		private String[] headers;
		private List<RtmResult> sheetContents;
		private File excelFile;
		
		public ExcelBuilder(String sheetName){
			this.sheetName = sheetName;
		}
		
		public ExcelBuilder header(String...headers){
			this.headers = headers;
			return this;
		}
		
		public ExcelBuilder data(List<RtmResult> sheetContents){
			this.sheetContents = sheetContents;
			return this;
		}
		
		public ExcelBuilder file(File excelFile){
			this.excelFile = excelFile;
			return this;
		}
		
		public ExcelModel build(){
			return new ExcelModel(this);
		}
	}

}
