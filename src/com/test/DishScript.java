package com.test;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class DishScript extends Keywords
{
	public static WebDriver driver;
//	public static Properties OR = null;
//	public static String rootPath = System.getProperty("user.dir");
	public static String chromeDriverPath=rootPath+"\\lib\\chromedriver.exe";
	public static String disTvTitleFolderPath=rootPath+"\\client\\dishanywhere\\";
	public static String disTvTitleScreenshotFolderPath=rootPath+"\\client\\dishanywhere\\screenshot\\";
	public static Xls_Reader xls = new Xls_Reader(disTvTitleFolderPath+"Title.xlsx");
	boolean titleFound=false;	
//	FileInputStream fs = null;
	int iStartRowNo=2;
	int iImplicitWaitTime=10;
//	public Logger APP_LOGS = Logger.getLogger("AutomationLog");
	String AUT_URL="http://www.dishanywhere.com";
	String sTitleNotFoundSSPathIfNoSuggestions;
	String sTitleNotFoundSSPathIfSuggestionsDisplayed;
	String sTitleFoundScreeshotPath;
	String sSystemErrorSSPath;
	String sException;
	String filename;
	String sMovie="movie";
	String sShow="show";
	boolean bTitleNotFoundSSPathIfNoSuggestions=false;
	boolean bTitleNotFoundSSPathIfSuggestionsDisplayed=false;
	String ActTitleText;
	List<WebElement> EpisodeTexts;

	
	@BeforeClass //+++++++++++++++++++++++++++++++++++++++++++++++ BEFORE CLASS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void setUp() throws InterruptedException {
		System.out.println("**************Test Script Started=> Launching Chrome Browser");
		APP_LOGS.debug("**************Test Script Started=> Launching Chrome Browser");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(iImplicitWaitTime, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	
	@Test //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public void dishBravo() throws Exception{
		try{
			//STEP1: Navigate to AUT_URL
			System.out.println("INFO: Navigating to (" + AUT_URL + ") Site");
			APP_LOGS.debug("INFO: Navigating to (" + AUT_URL + ") Site");
			driver.get(OR.getProperty("AUT_URL"));
		
	
			//STEP2: LOGIN with Valid credentials
			System.out.println("INFO: Performing Click action on LOG_IN LINK");
			APP_LOGS.debug("INFO: Performing Click action on LOG_IN LINK");
			driver.findElement(By.xpath(OR.getProperty("LOG_IN_LINK"))).click();
			Thread.sleep(2000);
			
			System.out.println("INFO: Entering USERNAME text in USERNAME_INPUTTEXT_FIELD Field");
			APP_LOGS.debug("INFO: Entering USERNAME text in USERNAME_INPUTTEXT_FIELD Field");
			driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("USERNAME_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("USERNAME"));

			System.out.println("INFO: Entering PASSWORD text in PASSWORD INPUT TEXT FIELD and clicking on SIGNIN BUTTON");
			APP_LOGS.debug("INFO: Entering PASSWORD text in PASSWORD INPUT TEXT FIELD and clicking on SIGNIN BUTTON");
			driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).clear();
			driver.findElement(By.xpath(OR.getProperty("PASSWORD_INPUTTEXT_FIELD"))).sendKeys(OR.getProperty("PASSWORD"));
			driver.findElement(By.xpath(OR.getProperty("SIGNIN_BUTTON"))).click();
			Thread.sleep(10000);
		
			
			
			//STEP3: Select SPORTS tab
			System.out.println("INFO: Performing Click action on SPORTS TAB");
			APP_LOGS.debug("INFO: Performing Click action on SPORTS TAB");
			driver.findElement(By.xpath(OR.getProperty("SPORTS_TAB"))).click();
			Thread.sleep(5000);

			// STEP4: READ xlsx file row by row and run following "for" loop for verification
			for(int iStartRow=iStartRowNo; iStartRow <= xls.getRowCount();iStartRow++) {
				String sTitle = xls.getCellData("Titles", iStartRow);
				String sEpisode = xls.getCellData("Episode", iStartRow);
				String sType = xls.getCellData("Type", iStartRow);
				String sNetwork = xls.getCellData("Network", iStartRow);
				String sTitleWithoutSpace=sTitle.replaceAll(" ","_");
				titleFound=false;
				bTitleNotFoundSSPathIfNoSuggestions=false;
				bTitleNotFoundSSPathIfSuggestionsDisplayed=false;
				
				
				//STEP4a: Select NETWORK tab and then select specific network passed via xlsx
				System.out.println("INFO: Performing Click action on NETWORKS TAB");
				APP_LOGS.debug("INFO: Performing Click action on NETWORKS TAB");
				driver.findElement(By.xpath(OR.getProperty("NETWORKS_TAB"))).click();
				Thread.sleep(5000);
				System.out.println("INFO: Performing Click action on "+sNetwork+" NETWORK");
				APP_LOGS.debug("INFO: Performing Click action on "+sNetwork+" NETWORK");
				driver.findElement(By.xpath(OR.getProperty("NETWORK_TOBE_SELECTED")+sNetwork+"']")).click();
				Thread.sleep(5000);
				
				//STEP4a: Click on "SEARCH_BUTTON", Clear "SEARCH_INPUT_FIELD", Enter Search Title Data
				System.out.println("INFO: Performing Click action on SEARCH BUTTON");
				APP_LOGS.debug("INFO: Performing Click action on SEARCH BUTTON");
				driver.findElement(By.xpath(OR.getProperty("SEARCH_BUTTON"))).click();
				Thread.sleep(3000);
				System.out.println("INFO: Entering "+sTitle+" Title for Search in SEARCH field");
				APP_LOGS.debug("INFO: Entering "+sTitle+" Title for Search in SEARCH field");
				driver.findElement(By.xpath(OR.getProperty("SEARCH_INPUT_FIELD"))).clear();
				driver.findElement(By.xpath(OR.getProperty("SEARCH_INPUT_FIELD"))).sendKeys(sTitle);
				
				Thread.sleep(5000);
				List<WebElement> myElements = driver.findElements(By.xpath(OR.getProperty("SEARCH_DROPDOWN_LIST")));
				System.out.println("INFO: No. of Titles returned after searching Title "+sTitle+" is = "+myElements.size()); 
				System.out.println("INFO: No. of Titles returned after searching Title "+sTitle+" is Empty= "+myElements.isEmpty());
				
				//STEP4c: Capturing the screenshot as the No Search Suggestions Displayed for Title
				if (myElements.isEmpty()){
					bTitleNotFoundSSPathIfNoSuggestions=true;
					System.out.println("INFO: No Search Suggestions Displayed for "+sTitle+ ", Capturing the screeshot and Moving ahead with Next Title");
					APP_LOGS.debug("INFO: No Search Suggestions Displayed for "+sTitle+ ", Capturing the screeshot and Moving ahead with Next Title");
					sTitleNotFoundSSPathIfNoSuggestions=disTvTitleFolderPath+ "screenshot\\titlenotfound\\"+sTitleWithoutSpace+"_"+TimeStamp()+".png";
					CaptureScreenShot(driver,sTitleNotFoundSSPathIfNoSuggestions);
					}
				
				//STEP4d: Verifying the titles one by one as the search Suggestions Displayed
				else{
					System.out.println("INFO: Search Suggestions Displayed for "+sTitle+ ", Checking them one by one to find exact Match");
					APP_LOGS.debug("INFO: Search Suggestions Displayed for "+sTitle+ ", Checking them one by one to find exact Match");
					int iElement=1;
					
					outerloop:
					for(WebElement e : myElements) {
						
						System.out.println("INFO: s above if loop "+sTitle);
						System.out.println("INFO: e.getText() above if loop "+e.getText());
						System.out.println("INFO: e.getAttribute above if loop "+e.getAttribute("class"));
						System.out.println("INFO: iElement===========================>: "+iElement);

						//STEP4e: Selecting the Exact Title Match found
						if ((e.getText().toLowerCase()).equals(sTitle.toLowerCase()) && e.getAttribute("class").contains(sType)) {
							System.out.println("INFO: Exact Match Found for "+sTitle+ ", Selecting it Now");
							APP_LOGS.debug("INFO: Exact Match Found for "+sTitle+ ", Selecting it Now");
							System.out.println("INFO: Exact Match Suggestion option is displayed: "+e.getText());
			                e.click();
							Thread.sleep(3000);
							driver.findElement(By.xpath(OR.getProperty("ON_DEMAND_BUTTON"))).click();
							ActTitleText=driver.findElement(By.xpath(OR.getProperty("TITLE_ACT_TEXT"))).getText();
							if(ActTitleText.toLowerCase().equals(sTitle.toLowerCase())&& sType.equals(sMovie))
							{
								System.out.println("INFO: PASS : Verifing Movie Title Text on ON DEMAND page  : Actual is ->"+ActTitleText+ " AND expected is ->"+sTitle);
								APP_LOGS.debug("INFO: PASS : Verifing MovieTitle Text on ON DEMAND page  : Actual is ->"+ActTitleText+ " AND expected is ->"+sTitle);
								titleFound=true;
				                break;
							}
							
							else if (ActTitleText.toLowerCase().equals(sTitle.toLowerCase())&& sType.equals(sShow))
							{
								System.out.println("INFO: PASS : Verifing Show Title Text on ON DEMAND page  : Actual is ->"+ActTitleText+ " AND expected is ->"+sTitle);
								APP_LOGS.debug("INFO: PASS : Verifing Title Show Text on ON DEMAND page  : Actual is ->"+ActTitleText+ " AND expected is ->"+sTitle);
								EpisodeTexts=driver.findElements(By.xpath(OR.getProperty("EPISODETEXT")));
								for (WebElement Episodetext : EpisodeTexts){
								if (Episodetext.getText().toLowerCase().equals(sEpisode.toLowerCase())){
									System.out.println("INFO: PASS : Verifing Show Episode Text on ON DEMAND page  : Actual is ->"+Episodetext.getText()+ " AND expected is ->"+sEpisode);
									APP_LOGS.debug("INFO: PASS : Verifing Show Episode Text on ON DEMAND page  : Actual is ->"+Episodetext.getText()+ " AND expected is ->"+sEpisode);
									titleFound=true;
									break outerloop;
									}
								}
							}
							else{
							System.out.println("INFO: Although Exact Match found in suggestion dropdown but when clicked on it, the different OnDemand Page Opened");
							APP_LOGS.debug("INFO: Although Exact Match found in suggestion dropdown but when clicked on it, the different OnDemand Page Opened");
							break;
							
							}
						}
						
						//STEP4f: Capturing the screenshot when Exact Match not found for Title from the suggestions received
						else if (iElement==myElements.size() && titleFound==false)
							{
							bTitleNotFoundSSPathIfSuggestionsDisplayed=true;
							System.out.println("INFO: Exact Match NOT Found for "+sTitle+ ", Capturing the ScreenShot");
							APP_LOGS.debug("INFO: Exact Match NOT Found for "+sTitle+ ", Capturing the ScreenShot");
							sTitleNotFoundSSPathIfSuggestionsDisplayed=disTvTitleFolderPath+ "screenshot\\titlenotfound\\"+sTitleWithoutSpace+"_"+TimeStamp()+".png";
							CaptureScreenShot(driver,sTitleNotFoundSSPathIfSuggestionsDisplayed);
							
							break;
							}
						iElement++;
						}
				
				//STEP4g: Capturing the screenshot for Exact Title Match found and Verifying the Title on ON DEMAND page
					
					if (titleFound)
					{
					System.out.println("INFO: Capturing the ScreenShot of ON DEMAND page for "+sTitle+ " Title");
					APP_LOGS.debug("INFO: Capturing the ScreenShot of ON DEMAND page for "+sTitle+ " Title");
					Thread.sleep(3000);
					sTitleFoundScreeshotPath=disTvTitleFolderPath+ "screenshot\\titlefound\\"+sTitleWithoutSpace+"_"+TimeStamp()+".png";
					CaptureScreenShot(driver,sTitleFoundScreeshotPath);
					Thread.sleep(3000);
					}
					
				}
				
				//STEP5: Writing YES or NO in xlsx depending on Title Match
			if (titleFound==true)
					{   
					String tempStr;
					tempStr=sTitle+"__"+sType+"__"+sNetwork+"__"+"YES"+"__"+sTitleFoundScreeshotPath;
					verificationData.add(tempStr);
					}
			else if ((myElements.isEmpty()) || (titleFound==false))
					{
					String tempStrr = null;
					if (bTitleNotFoundSSPathIfNoSuggestions){
					tempStrr=sTitle+"__"+sType+"__"+sNetwork+"__"+"NO"+"__"+sTitleNotFoundSSPathIfNoSuggestions;
					verificationData.add(tempStrr);
					}
					else if (bTitleNotFoundSSPathIfSuggestionsDisplayed){
					tempStrr=sTitle+"__"+sType+"__"+sNetwork+"__"+"NO"+"__"+sTitleNotFoundSSPathIfSuggestionsDisplayed;	
					verificationData.add(tempStrr);
					}
					}
			else
			{
				System.out.println("INFO: Unable to write data in Title_Results file");
				APP_LOGS.debug("INFO: Unable to write data in Title_Results file");
			}
		}
			
			//STEP6: Generating xlsx file in run time with the following data (Titles,Type,Network,Available,Screenshot)
			String xlsxfile=disTvTitleScreenshotFolderPath+"Title_Results"+TimeStamp()+".xlsx";
			Generatexlsx(xlsxfile);
			
			}

		//STEP7: Capturing screenshots for system errors 
		catch(Exception e)
		{
			System.out.println("INFO: Exception : "+e.getMessage());
			APP_LOGS.debug("INFO: Exception : "+e.getMessage());
			sSystemErrorSSPath=disTvTitleFolderPath+ "\\screenshot\\systemerror\\"+TimeStamp()+".png";
			CaptureScreenShot(driver,sSystemErrorSSPath);
		}
	}
	
	@AfterClass
	//STEP8: QUIT BROWSER
	public void tearDown() {
		if(driver!=null) {
			System.out.println("Closing Chrome Browser");
			APP_LOGS.debug("INFO: Closing Chrome Browser");
			driver.quit();
		}
	}

}
