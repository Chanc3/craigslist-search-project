package com.sqa.cs.qa.search;

import static org.testng.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class CraigslistTest {

	private WebDriver driver;

	private String baseUrl;

	private boolean acceptNextAlert = true;

	private StringBuffer verificationErrors = new StringBuffer();

	@DataProvider
	public Object[][] postData() {
		return new Object[][] {
				new Object[] { "Test 1", "testing@test.com", "testing title 1", "94580", "This is only a test" },
				new Object[] { "Test 2", "testing@test.com", "testing title 2", "94555", "This is only a test" }, };
	}

	@DataProvider
	public Object[][] searchData() {
		return new Object[][] { new Object[] { "Test 1", new String[] { "QA ", "Analyst " } },
				new Object[] { "Test 1", new String[] { "iOS ", "Developer " } }, };
	}

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://sfbay.craigslist.org/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	@Test(dataProvider = "postData", enabled = true)
	public void testPost(String label, String email, String postTitle, String zipCode, String bodyText)
			throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.cssSelector("#postlks > li > #post")).click();
		driver.findElement(By.xpath("(//input[@name='id'])[3]")).click();
		driver.findElement(By.name("id")).click();
		driver.findElement(By.name("n")).click();
		driver.findElement(By.xpath("(//input[@name='n'])[38]")).click();
		driver.findElement(By.id("FromEMail")).clear();
		driver.findElement(By.id("FromEMail")).sendKeys(email);
		driver.findElement(By.id("ConfirmEMail")).clear();
		driver.findElement(By.id("ConfirmEMail")).sendKeys(email);
		driver.findElement(By.id("PostingTitle")).clear();
		driver.findElement(By.id("PostingTitle")).sendKeys(postTitle);
		driver.findElement(By.id("postal_code")).clear();
		driver.findElement(By.id("postal_code")).sendKeys(zipCode);
		driver.findElement(By.id("PostingBody")).clear();
		driver.findElement(By.id("PostingBody")).sendKeys(bodyText);
		driver.findElement(By.name("go")).click();
		driver.findElement(By.cssSelector("section.body > form > button[name=\"go\"]")).click();
		driver.findElement(By.cssSelector("#publish_bottom > button[name=\"go\"]")).click();
		WebElement finished = driver.findElement(By.tagName("em"));
		Assert.assertEquals("IMPORTANT - FURTHER ACTION IS REQUIRED TO COMPLETE YOUR REQUEST !!!", finished.getText());
	}

	@Test(dataProvider = "searchData", enabled = false)
	public void testSearch(String label, String[] keywords) throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.cssSelector("a.sof > span.txt")).click();
		driver.findElement(By.id("query")).clear();
		driver.findElement(By.id("query")).sendKeys(keywords);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		List<WebElement> links = driver.findElements(By.partialLinkText(keywords[0]));
		System.out.println(label);
		System.out.println("10 most recent job postings:");
		for (int i = 0; i < links.size(); i++) {
			System.out
					.println("(" + (i + 1) + ") " + links.get(i).getText() + " : " + links.get(i).getAttribute("href"));
		}
		System.out.println("");
		Assert.assertTrue("A minimum of 10 positions could not be found.", 10 <= links.size());
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
