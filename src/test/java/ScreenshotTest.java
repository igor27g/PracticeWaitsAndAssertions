import TestHelpers.TestStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class ScreenshotTest {
    ChromeDriver driver;
    WebDriverWait wait;

    String fakeStore = "http://automationpractice.com/index.php";

    By firstProduct = By.cssSelector("ul[id='homefeatured']>li>div");
    By addToCartButton = By.cssSelector("p[id='add_to_cart']>button");
    By proceedToCheckout = By.cssSelector("a[title='Proceed to checkout']");

    @RegisterExtension
    TestStatus status = new TestStatus();

    @BeforeEach
    public void driverSetup(){

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to(fakeStore);


        wait = new WebDriverWait(driver,10);
    }

    @AfterEach
    public void driverClose(TestInfo info) throws IOException
    {
        if(status.isFailed){
            System.out.println("Test screenshot is available at: " +  takeScreenshot(info));
        }
        driver.close();
        driver.quit();
    }

    @Test
    public void makeScreenAddProductInCart() throws IOException {
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckout)).click();
        String path = "C:\\screenshots\\myExample.jpg";
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileHandler.copy(screenshot, new File(path));
    }

    @Test
    public void makeScrenButton() throws IOException {
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
        WebElement addToCartButton = driver.findElement(By.cssSelector("p[id='add_to_cart']>button"));
        File buttonScreen = addToCartButton.getScreenshotAs(OutputType.FILE);
        String path = "C:\\screenshots\\myExample2.jpg";
        FileHandler.copy(buttonScreen,new File(path));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckout)).click();
    }

    @Test
    public void makeScreenProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("wrongSelector"))));
    }

    private  String takeScreenshot(TestInfo info) throws  IOException {
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        String path = ("C:\\screenshots\\" + info.getDisplayName() + " " + formatter.format(timeNow) + " .png");
        FileHandler.copy(screenshot,new File(path));
        return path;
    }



}
