package com.autoamtiononthego;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class SeleniumTest {

    WebDriver driver;
    String appUrl ="https://admin-demo.virtocommerce.com/#/login";
    @Parameters({"browser"})
    @BeforeClass
    public void OpenUrl(String browser)
    {
        if(browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.setCapability(ChromeOptions.CAPABILITY, options);
            options.setCapability("browserName", "chrome");
            options.setCapability("acceptSslCerts", "true");
            options.setCapability("javascriptEnabled", "true");
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            options.merge(cap);
            try {
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (browser.equalsIgnoreCase("firefox")){
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--start-maximized");
            options.setCapability("browserName", "firefox");
            options.setCapability("acceptSslCerts", "true");
            options.setCapability("javascriptEnabled", "true");
            DesiredCapabilities cap = DesiredCapabilities.firefox();
            options.merge(cap);
            try {
                driver = new RemoteWebDriver (new URL("http://localhost:4444/wd/hub"), options);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
        else if (browser.equalsIgnoreCase("localchrome"))
        {
            System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedrivernew.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }

        driver.get(appUrl);
    }
    @Test(priority =0)
    public void loginInvalidAdmin()
    {
        WebDriverWait webDriverWait = new WebDriverWait(driver,20);
        WebElement login;
        login = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        login.sendKeys("Demo");
        driver.findElement(By.name("password")).sendKeys("demo");
        driver.findElement(By.xpath("//button[@class='btn']")).click();
        WebElement error;
        error=webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='form-error']")));
        String message= error.getText().trim();
        Assert.assertEquals(message,"The login or password is incorrect.");
        driver.navigate().refresh();
    }
    @Test(priority =1)
    public void validAdmin() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
        WebElement login;
        login = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        login.sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("store");
        driver.findElement(By.xpath("//button[@class='btn']")).click();
        waitForLoad(driver);
       WebElement note;
     note = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(),'New password:')]")));
       String noteMessage=  note.getText().trim();
       Assert.assertEquals(noteMessage,"New password:");
    }


    @Test(priority = 2)
    public void failedTestCase()
    {
        Assert.assertTrue(false);
    }

    @AfterClass
    public void quitDriver()
    {
        driver.quit();
    }

    @AfterMethod
    public void captureScreen(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE); // capture screenshot file
            System.out.println("Path:"+Paths.get("").toAbsolutePath().toString());
            File target = new File(Paths.get("").
                    toAbsolutePath().toString() +
                    "/Screenshots/" + result.getName() + ".png");
            FileUtils.copyFile(source,target);
            System.out.println("screenshot captured");
        }
    }
    public static void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
}

