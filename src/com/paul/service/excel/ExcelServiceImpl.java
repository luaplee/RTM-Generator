package com.paul.service.excel;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.inject.Singleton;
import com.paul.model.ExcelModel;
import com.paul.model.RtmResult;

@Singleton
public class ExcelServiceImpl implements ExcelService {
	
	@Override
	public void exportExcel(ExcelModel excelModel) throws Exception{
		XSSFWorkbook workbook = excelModel.getWorkbook();
		XSSFSheet sheet = workbook.createSheet(excelModel.getSheetname());
		int rowCount = 0;
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setWrapText(true);
		if(excelModel.getHeaders().length > 0){
			Row row = sheet.createRow(rowCount++);
			writeRowAutoAjustColumn(row, headerStyle, excelModel.getHeaders());
		}
		
		for(RtmResult rtmResult : excelModel.getSheetContents()){
			Row row = sheet.createRow(rowCount++);
			writeRow(row, workbook.createCellStyle(), rtmResult.getArray());
		}
		
		try{
			FileOutputStream outputStream = new FileOutputStream(excelModel.getExcelFile());
			workbook.write(outputStream);
		} finally {
			workbook.close();
		}
		
		
	}
	
	private void writeRow(Row row, CellStyle cellStyle, String... cellValues){
		int cellCount = 0;
		Cell cell = row.createCell(cellCount);
		cell.setCellStyle(cellStyle);
		for(String cellValue : cellValues){
			cell.setCellValue(cellValue);
			cell = row.createCell(++cellCount);
		}
	}
	
	private void writeRowAutoAjustColumn(Row row, CellStyle cellStyle, String... cellValues){
		int cellCount = 0;
		Cell cell = row.createCell(cellCount);
		cell.setCellStyle(cellStyle);
		for(String cellValue : cellValues){
			cell.setCellValue(cellValue);
			cell.getSheet().autoSizeColumn(cellCount);
			cell = row.createCell(++cellCount);
		}
	}

}

