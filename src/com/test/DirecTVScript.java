package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
//import org.openqa.selenium.interactions.Actions;

public class DirecTVScript 
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
			fs = new FileInputStream(rootPath + "/src/DirecTVOR.properties/");
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
	public void DirectTV() throws InterruptedException, IOException{
		try{
			//STEP1: Navigate to "https://dproto.hulftinc.com/
			driver.get(OR.getProperty("AUT_URL"));
		
			//STEP2: LOGIN
			driver.findElement(By.xpath(OR.getProperty("LOG_IN_LINK"))).click();
			Thread.sleep(5000);
			//driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("USERNAME"));
		//	driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("PASSWORD"));
			driver.findElement(By.xpath(OR.getProperty("SIGNIN_BUTTON"))).click();
			Thread.sleep(20000);
		
			//STEP2: CLICK on AT&T->MY_DIRECTTV_LINK->WATCH_TV_LINK 
			driver.findElement(By.xpath(OR.getProperty("AT&T_TAB"))).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty("MY_DIRECTTV_LINK"))).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty("WATCH_TV_LINK"))).click();
			Thread.sleep(5000);
		
		// STEP3: READ text file containing all titles and store them in array
			BufferedReader in = new BufferedReader(new FileReader(rootPath+"/title/directv/title.txt"));
			String str;
			List<String> list = new ArrayList<String>();
			while((str = in.readLine()) != null){
			    list.add(str);
			}
			System.out.println("File read and data is puttenin list");
			String[] stringArr = list.toArray(new String[0]);
			System.out.println("Data is now in array List");
			for (String s: stringArr) {
				System.out.println("Going to perform search for "+s); 
				//STEP5: PERFORM search
				
				//driver.findElement(By.xpath(OR.getProperty("SEARCH_INPUT_FIELD"))).sendKeys(s);
				driver.findElement(By.xpath(OR.getProperty("SEARCH_INPUT_FIELD"))).click();
				Actions actions = new Actions(driver);
				for (int i = 0; i < s.length(); i++){
					char eachInvidualCharacter = s.charAt(i);
					String indivualLetter = new StringBuilder().append(eachInvidualCharacter).toString();
					actions.sendKeys(indivualLetter);
					actions.build().perform();
					Thread.sleep(1000);
				}
				
				Thread.sleep(5000);
				
				List<WebElement> myElements = driver.findElements(By.xpath(OR.getProperty("SEARCH_DROPDOWN_LIST")));
				outerloop:
				for(WebElement e : myElements) {
					System.out.println("MK: "+e.getText());
					// We have title name in "s" and type in say "t" variable
					String t="TV SHOWS";
					String[] sActTileName;
					String sMovies="MOVIES";
					String sTvShows="TV SHOWS";
					String sPerson="PERSON";
					String sCompleteText=e.getText();
					
					
							if (sCompleteText.contains(sTvShows) ||sCompleteText.contains(sMovies) || sCompleteText.contains(sPerson))
							{
							//	System.out.println("MK: inside first if "+sCompleteText);
								sActTileName=sCompleteText.split(t);
							//	System.out.println("MK:sCompleteText [0]"+sActTileName[0]);
								
								sActTileName[0]=sActTileName[0].trim();
								
							//	System.out.println("MK:sCompleteText [0]"+sActTileName[0]);
								
								
							//	System.out.println("MK:sCompleteText [1]"+sActTileName[1]);
								if (sActTileName[0].trim().equals(s))
								{
								//	System.out.println("MK: inside first if "+sCompleteText);
									e.click();
									break outerloop;
								}
								
							}

				}
			
				Thread.sleep(3000);
				//File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
				SimpleDateFormat sdf = new SimpleDateFormat("ddMM_hhmmss_aaa(zzz)");
				java.util.Date curDate = new java.util.Date();
				String strDate = sdf.format(curDate);
				String strActDate = strDate.toString();
				//FileUtils.copyFile(scrFile, new File(rootPath + "\\screenshot\\dishanywhere\\"+s+"_"+strActDate+".jpg"));
				ImageIO.write(fpScreenshot.getImage(),"PNG",new File(rootPath + "\\screenshot\\directv\\titlefound\\"+s+"_"+strActDate+".png"));
				
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
			ImageIO.write(fpScreenshot.getImage(),"PNG",new File(rootPath + "\\screenshot\\directv\\systemerror\\"+"Execution Halted!"+strActDate+".png"));
			
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
