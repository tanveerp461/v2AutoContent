package com.test;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.util.Calendar;

public class Xls_Reader {

	/* @HELP
	@class:			Xls_Reader
	@method:		 
	@parameter:	Different parameters are passed as per the method declaration
	@notes:			
			1.	get the row count in a sheet
			2.	get the data from a cell
			3.	true if data is set successfully else false
			4.	true if sheet is created successfully else false
			5.	true if sheet is removed successfully else false if sheet does not exist
			6.	true if column is created successfully
			7. removes a column and all the contents
			8.	find whether sheets exists
			9. number of columns in a sheet
	@returns:		All respective methods have there return types
	@END
	 */

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public Xls_Reader(String path) {
		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			System.out.println("INFO:=> Title.xlsx don't exist");
		}
	}


	// returns the row count in a sheet
	public int getRowCount() {
		int index = workbook.getSheetIndex(sheet);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}
	}

	// returns the data from a cell
	public String getCellData(String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";
			int index = workbook.getSheetIndex(sheet);
		//	System.out.println("INFO:=> getcelldata index: "+index);
			int col_Num = -1;
			if (index == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim()
						.equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);
			if (cell == null)
				return "";
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
					|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR)))
							.substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
				}
				System.out.println("INFO:=> third");
				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}
	
	// returns true if data is set successfully else false
	
	
		public boolean setCellData(String colName, int rowNum, String data) throws IOException {
			try {
				//System.out.println("INFO:=> First");
				fis = new FileInputStream(path);
				workbook = new XSSFWorkbook(fis);
				if (rowNum <= 0)
					return false;
				int index = workbook.getSheetIndex(sheet);
				int colNum = -1;
			//	System.out.println("INFO:=> second");
			//	System.out.println("INFO:=> index: "+index);
				//if (index == -1)
					//return false;
			//	System.out.println("INFO:=> third");
				sheet = workbook.getSheetAt(0);
			//	System.out.println("INFO:=> third");
				row = sheet.getRow(0);
				
				for (int i = 0; i < row.getLastCellNum(); i++) {
			//		System.out.println("INFO:=> fourth");
					if (row.getCell(i).getStringCellValue().trim().equals(colName))
						colNum = i;
				}
				if (colNum == -1)
					return false;
			//	System.out.println("INFO:=> fifth");
				sheet.autoSizeColumn(colNum);
				row = sheet.getRow(rowNum - 1);
				if (row == null)
					row = sheet.createRow(rowNum - 1);
				cell = row.getCell(colNum);
				if (cell == null)
					cell = row.createCell(colNum);
			//	System.out.println("INFO:=> sixth");
				cell.setCellValue(data);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();
			} catch (Exception e) {
		//		System.out.println("INFO:=> exception");
				e.printStackTrace();
				return false;
			}
			fis.close();
			return true;
		}
}
