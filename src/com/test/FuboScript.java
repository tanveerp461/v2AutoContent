package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class FuboScript 
{
	public static WebDriver driver;
	public static Properties OR = null;
	public static String rootPath = System.getProperty("user.dir");
	public static String chromeDriverPath=rootPath+"\\lib\\chromedriver.exe";
	public static String fileToUploadPath=rootPath+"\\filetoupload\\testtextfileforupload.txt";
	FileInputStream fs = null;
	String autUrl="https://dproto.hulftinc.com/";
	
	@BeforeTest	//+++++++++++++++++++++++++++++++++++++++++++++++ BEFORE TEST +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void property()
	{
		try
		{
			OR = new Properties();
			System.out.println("rootPath==============>"+rootPath);
			fs = new FileInputStream(rootPath + "/src/FuboOR.properties/");
			OR.load(fs);
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println("File could not found");
			fnfe.printStackTrace();
		}
		catch(IOException ioe)
		{
			System.out.println("input output exception");
			ioe.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Problem with to read Property file");
			e.printStackTrace();
		}	
	}
	
	@BeforeClass //+++++++++++++++++++++++++++++++++++++++++++++++ BEFORE CLASS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void setUp() throws InterruptedException {
		System.out.println("Test Script Started, Launching Chrome Browser");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	@Test //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void dishBravo() throws InterruptedException, IOException{
		try{
			//STEP1: Navigate to "https://dproto.hulftinc.com/
			driver.get(OR.getProperty("AUT_URL"));
		
			//STEP3: LOGIN
			driver.findElement(By.xpath(OR.getProperty("LOG_IN_LINK"))).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("USERNAME"));
			driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("PASSWORD"));
			driver.findElement(By.xpath(OR.getProperty("SIGNIN_BUTTON"))).click();
			Thread.sleep(40000);
			//STEP4: CLICK on CHANNELS->NETWORKS->BRAVO->SERIES tab and select "BRAVO" 
			driver.findElement(By.xpath(OR.getProperty("CHANNELS_TAB"))).click();
			Thread.sleep(10000);
			driver.findElement(By.xpath(OR.getProperty("NETWORKS_TAB"))).click();
			Thread.sleep(10000);
			driver.findElement(By.xpath(OR.getProperty("NETWORK_TOBE_SELECTED"))).click();
			Thread.sleep(10000);
			driver.findElement(By.xpath(OR.getProperty("SERIES_TAB"))).click();
			Thread.sleep(10000);
			// STEP6: READ text file containing all titles and store them in array
			BufferedReader in = new BufferedReader(new FileReader(rootPath+"/title/fubo/title.txt"));
			String str;
			List<String> list = new ArrayList<String>();
			while((str = in.readLine()) != null){
			    list.add(str);
				}
			System.out.println("File read and data is puttenin list");
			String[] stringArr = list.toArray(new String[0]);
			System.out.println("Data is now in array List");
			for (String s: stringArr) {
				System.out.println("Going to perform click each available Titles "+s); 
				//STEP5: Take titles from file and PERFORM search on page
				String sDynamicXpath ="//*[contains(text(), '"+s+"')]";
				if (driver.findElement(By.xpath(sDynamicXpath)) != null)
					{
					driver.findElement(By.xpath(sDynamicXpath)).click();
					}
				Thread.sleep(3000);
				//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
				SimpleDateFormat sdf = new SimpleDateFormat("ddMM_hhmmss_aaa(zzz)");
				java.util.Date curDate = new java.util.Date();
				String strDate = sdf.format(curDate);
				String strActDate = strDate.toString();
				//FileUtils.copyFile(scrFile, new File(rootPath + "\\screenshot\\fubo\\"+s+"_"+strActDate+".jpg"));
				ImageIO.write(fpScreenshot.getImage(),"PNG",new File(rootPath + "\\screenshot\\fubo\\titlefound\\"+s+"_"+strActDate+".png"));
				driver.navigate().back();
				Thread.sleep(3000);
			}
		}	
		catch(Exception e)
		{
			System.out.println("Exception : "+e.getMessage());
			Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
			SimpleDateFormat sdf = new SimpleDateFormat("ddMM_hhmmss_aaa(zzz)");
			java.util.Date curDate = new java.util.Date();
			String strDate = sdf.format(curDate);
			String strActDate = strDate.toString();
			ImageIO.write(fpScreenshot.getImage(),"PNG",new File(rootPath + "\\screenshot\\fubo\\systemerror\\"+"Execution Halted!"+strActDate+".png"));
		}
	}
	@AfterClass
	//STEP12: QUIT BROWSER
	public void tearDown() {
		if(driver!=null) {
			System.out.println("Closing Chrome Browser");
			driver.quit();
		}
	}

}
