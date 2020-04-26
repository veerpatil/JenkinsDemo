package com.autoamtiononthego;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumTest {

    WebDriver driver;
    @BeforeClass
    public void OpenUrl()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://admin-demo.virtocommerce.com/#/login");
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

