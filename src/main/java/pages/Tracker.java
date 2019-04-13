package pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Tracker {
	
	@Test
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		WebDriver driver = chromeTest();
		driver.close();
	}
	
	@Test
	public static Properties setupProps() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/data.properties");
		prop.load(fis);		
		return prop;
	}

		@Test
		public static WebDriver chromeTest() throws IOException
		{
			Properties prop = setupProps();
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver");
			WebDriver driver = new ChromeDriver();
			ArrayList<Items> esoItems = setupItems(prop);
			ArrayList<CheapProducts> results = findItems(driver, esoItems);
			if(!(results.isEmpty())) {
				String emailText = composeEmailMsg(results);
				Miscellaneous misc = new Miscellaneous(emailText);
				misc.sendEmail();
			}
			return driver;
		}
	
		@Test
		public static ArrayList<Items> setupItems(Properties prop) {
			ArrayList<Items> items = new ArrayList<Items>();
			for(int i=0; i<prop.size();i++) {
				String value = (String) prop.get(String.valueOf(i+1));
				String name = value.split("/")[0];
				String itemNum = value.split("/")[1];
				double price = Double.valueOf(value.split("/")[2]);
				items.add(new Items(name,itemNum,price));
			}
			return items;
		}
				
		//fix this - retrieve the items from each method
		//need to update the parameters
		public static ArrayList<CheapProducts> findItems(WebDriver driver, ArrayList<Items> esoItems) {
			
			ArrayList<CheapProducts> cheap = new ArrayList<CheapProducts>();
			for(int j=0;j<esoItems.size();j++) {
				driver.get(esoItems.get(j).getUrl());
			
				int total = driver.findElements(By.xpath("//table/tbody/tr/td[@class='gold-amount bold']")).size();
				for(int i=0; i<total; i++) {
					
					double goldPrice = findGoldPrice(driver, "//table/tbody/tr/td[@class='gold-amount bold']", i);
					int timeElapsed = findMinutes(driver, "//table/tbody/tr/td[@class='bold hidden-xs']", i);
					String location = findLocation(driver, "//table/tbody/tr/td[@class='hidden-xs']", i);
					//Store this information for the email
					if(timeElapsed <=15 && goldPrice<=esoItems.get(j).getPrice()) {
						cheap.add(new CheapProducts(esoItems.get(j).getName(), goldPrice, timeElapsed, location));
					}
				}
			}
			return cheap;
		}
		
		public static String composeEmailMsg(ArrayList<CheapProducts> products) {
			StringBuilder sb = new StringBuilder();
			for(int i =0; i<products.size();i++) {
				sb.append("Item name = " + products.get(i).getName() + "\n");
				sb.append("Price = " + products.get(i).getGoldPrice() + "\n");
				sb.append("Location = " + products.get(i).getLocation() + "\n");
				sb.append("Time Elapsed = " + products.get(i).getTimeElapsed() + "\n\n");			
			}
			
			return sb.toString();
		}	
		
		public static double findGoldPrice(WebDriver driver, String gold, int i) {
			double goldAmout;
			String price = driver.findElements(By.xpath(gold)).get(i).getText();
			String [] elements = price.split("\n");
			goldAmout = Double.valueOf(elements[0].replaceAll(",", ""));	
			return goldAmout;
		}
		
		public static int findMinutes(WebDriver driver, String xpath, int index) {
			int totalMinutes;
			String minutes = driver.findElements(By.xpath(xpath)).get(index).getAttribute("data-mins-elapsed");
			totalMinutes = Integer.valueOf(minutes);
			//find the time elapsed in minutes - this returns a string, but there are multiple instances to be accessed
			return totalMinutes;		
		}
		
		//String locationXpath = "//table/tbody/tr/td[@class='hidden-xs']";
		public static String findLocation(WebDriver driver, String xpath, int i) {
			String location = new String();
//			for(int i=0; i<size; i++) {
				if(i%2==1) {
					String minutes = driver.findElements(By.xpath("//table/tbody/tr/td[@class='hidden-xs']")).get(i).getText();
					location = minutes.replaceAll("\n", " Guild: ");
//					totalMinutes = Integer.valueOf(minutes);
//					System.out.println("item " + (i+1) + " location = " + location);
				}		
//			}
			return location;
		}


}
