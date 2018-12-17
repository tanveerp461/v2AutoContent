package com.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Keywords {
	public static WebDriver driver;
	public static Vector<String> verificationData=new Vector<String>();
	public Logger APP_LOGS = Logger.getLogger("AutomationLog");
	public static Properties OR = null;
	FileInputStream fs = null;
	public static String rootPath = System.getProperty("user.dir");
	
	
	@BeforeTest	//+++++++++++++++++++++++++++++++++++++++++++++++ BEFORE TEST +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void property()
	{
		try
		{
			OR = new Properties();
		//	System.out.println("rootPath==============>"+rootPath);
			fs = new FileInputStream(rootPath + "/src/DishOR.properties/");
			OR.load(fs);
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println("File could not found");
			APP_LOGS.debug("File could not found");
			fnfe.printStackTrace();
		}
		catch(IOException ioe)
		{
			System.out.println("input output exception");
			APP_LOGS.debug("input output exception");
			ioe.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Problem with to read Property file");
			APP_LOGS.debug("Problem with to read Property file");
			e.printStackTrace();
		}	
	}
	
	
	
	public static void CaptureScreenShot(WebDriver driver,String Screenshotname) throws Exception {
		Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
		ImageIO.write(fpScreenshot.getImage(),"PNG",new File(Screenshotname));
		  }
	
	
	
	public String TimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMM_hhmmss_aaa(zzz)");
		java.util.Date curDate = new java.util.Date();
		String strDate = sdf.format(curDate);
		String strActDate = strDate.toString();
		return strActDate;
		}
	
	
	public  void Generatexlsx(String xlsxfile) throws IOException{
	Workbook wb = new XSSFWorkbook();
	Row row;
	Cell cell;
	String[] cellvalue = null;
	int t = 0;
	Sheet sh = wb.createSheet("Sheet1");
	FileOutputStream fos = new FileOutputStream(xlsxfile);
	row = sh.createRow((short) t);
	t++;
	for (int k = 0; k <= 4; k++) {
		cell = row.createCell((short) k);
		switch (k) {
		case 0:
			cell.setCellValue("Titles");
			break;
		case 1:
			cell.setCellValue("Type");
			break;
		case 2:
			cell.setCellValue("Network");
			break;
		case 3:
			cell.setCellValue("Available");
			break;
		case 4:
			cell.setCellValue("Screenshot");
			break;
		}
	}
	for (int i = 0; i < verificationData.size(); i++) {
		row = sh.createRow((short) t);
		t++;
		String str1 = verificationData.get(i).replaceAll("\\r\\n|\\r|\\n", " ");
		cellvalue = str1.split("__");
		for (int j = 0; j < cellvalue.length; j++) {
			cell = row.createCell((short) j);
			cell.setCellValue(cellvalue[j]);
			if (j == cellvalue.length - 1) {
				CreationHelper createHelper = wb.getCreationHelper();
				Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
				CellStyle hlink_style = wb.createCellStyle();
				Font hlink_font = wb.createFont();
				hlink_font.setUnderline(Font.U_SINGLE);
				hlink_font.setColor(IndexedColors.BLUE.getIndex());
				hlink_style.setFont(hlink_font);
				String FileAddress = cellvalue[j];
				FileAddress = FileAddress.replace("\\", "/");
				link.setAddress(FileAddress);
				cell.setHyperlink(link);
				cell.setCellStyle(hlink_style);
			}
		}
	}
	wb.write(fos);
	fos.close();
	System.out.println("INFO: Result Excel file has been generated!");
	APP_LOGS.debug("INFO: Result Excel file has been generated!");
	
}
	
}
