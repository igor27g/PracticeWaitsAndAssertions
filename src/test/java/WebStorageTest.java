import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebStorageTest {

    ChromeDriver driver;
    WebDriverWait wait;

    String fakeStore = "http://automationpractice.com/index.php";


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
    public void driverClose()
    {
        driver.close();
        driver.quit();
    }

    @Test
    public void checkLocalStorageIsEmpty() {
        LocalStorage local = driver.getLocalStorage();
        if(local.size() == 0) {
            local.setItem("key1", "value1");
            System.out.println(local.size());
        }
        Assertions.assertEquals(1,local.size(), "Wrong size of local storage");
    }

    @Test
    public void checkLocalStorageJS() {
        LocalStorage local = driver.getLocalStorage();
        local.setItem("key1", "value1");
        Assertions.assertTrue(local.keySet().contains("key1"),"Local storage doesn't contain key");
    }

    @Test
    public void keyRemovedTest() {
        SessionStorage session = driver.getSessionStorage();
        session.setItem("key1", "value1");
        Set<String> keys = session.keySet();
        String keyDelete = "";
        for (String key:keys) {
            if(key.contains("key1")){
                keyDelete = key;
            }
        }
        String removed = session.removeItem(keyDelete);
        session.size();
    }

    @Test
    public void takeKeyUsingIndex() {
        ((JavascriptExecutor)driver).executeScript("localStorage.setItem(arguments[0],arguments[1]);", "key1", "Value1");
        ((JavascriptExecutor)driver).executeScript("localStorage.setItem(arguments[0],arguments[1]);", "key2", "Value2");
         String indexValue = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.key(arguments[0]);",1);
         System.out.println(indexValue);
    }


}
