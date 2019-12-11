import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class RepeatWaitsAndAssertions {


    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Actions actions;

    String fakeStore = "http://automationpractice.com/index.php";

    By firstProduct = By.cssSelector("ul[id='homefeatured']>li>div");
    By addToCartButton = By.cssSelector("p[id='add_to_cart']>button");
    By proceedToCheckout = By.cssSelector("a[title='Proceed to checkout']");
    By iconTrash = By.className("icon-trash");
    By emptyCart = By.cssSelector("p[class='alert alert-warning']");

    By signInButton = By.cssSelector("a[title='Log in to your customer account']");
    By emailAddressLogin = By.id("email");
    By passwordLogin = By.id("passwd");
    By submitLoginButton = By.cssSelector("button[name='SubmitLogin']");
    By authenticationAlert = By.xpath(".//div[@id='center_column']/div/ol/li");


    String expectedAlertInEmptyCart = "Your shopping cart is empty.";
    String noExistentUser = "xtadsadads@ci.pl";
    String wrongPassword = "wrongPassword";
    String expectedAlertAuthenticationMessage;
    String alertMessage;





    @BeforeEach
    public void driverSetup(){


        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to(fakeStore);

        wait = new WebDriverWait(driver,10);
        js = (JavascriptExecutor)driver;
        actions = new Actions(driver);

    }

    @AfterEach
    public void driverClose()
    {
        driver.close();
        driver.quit();
    }


    @Test
    public void deleteProductFromCart(){
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckout)).click();
        wait.until(ExpectedConditions.elementToBeClickable(iconTrash)).click();
        WebElement emptyCartAlert =wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCart));

        Assertions.assertAll("Add one product to cart and after delete it",
                () -> Assertions.assertEquals( "http://automationpractice.com/index.php?controller=order", driver.getCurrentUrl(), "Wrong website url"),
                () -> Assertions.assertEquals(expectedAlertInEmptyCart, emptyCartAlert.getText(), "Wrong alert of empty cart")
        );

    }

    @Test
    public void logInNonExistentUser() {
        loginInUser(noExistentUser,wrongPassword);
        alertMessage = getAlertMessage();
        expectedAlertAuthenticationMessage = "Authentication failed.";
        Assertions.assertEquals(expectedAlertAuthenticationMessage, alertMessage, "Error meassage is not correct");
    }

    private void loginInUser(String noExistentUser,String wrongPassword ) {
        driver.findElement(signInButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailAddressLogin)).sendKeys(noExistentUser);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordLogin)).sendKeys(wrongPassword);
        driver.findElement(submitLoginButton).click();
    }

    private  String getAlertMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(authenticationAlert)).getText();
    }

    @Test
    public void scrollToOrders() {
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,2500)");
        driver.findElement(By.cssSelector("a[title='My orders']")).click();
        Assertions.assertEquals("http://automationpractice.com/index.php?controller=authentication&back=history", driver.getCurrentUrl(), "Wrong url address");
    }

    @Test
    public void scrollToSpecials() {
        WebElement element = driver.findElement(By.cssSelector("a[title='Specials']"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",element);
        element.click();
        Assertions.assertEquals("http://automationpractice.com/index.php?controller=prices-drop", driver.getCurrentUrl(),"Wrong url address");
    }

    @Test
    public void selectByDropdownList() {
        driver.findElement(By.id("contact-link")).click();
        WebElement dropdownElement = driver.findElement(By.id("id_contact"));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByIndex(1);
    }

    @Test
    public void selectByDropdownList2() {
        driver.findElement(By.id("contact-link")).click();
        WebElement dropdownElement = driver.findElement(By.id("id_contact"));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByValue("1");
    }

    @Test
    public void selectProductusingActions() {
        List<WebElement> listProduct = driver.findElements(By.cssSelector("ul[id='homefeatured']>li"));
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,500)");
        WebElement product2 = listProduct.get(1);
        actions.click(product2).build().perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("h1[itemprop='name']"))));
        String titleProduct = driver.findElement(By.cssSelector("h1[itemprop='name']")).getText();
        Assertions.assertEquals("Blouse", titleProduct, "Wrong title product");
    }


    @Test
    public void checkProductInCart() {
        List<WebElement> listProduct = driver.findElements(By.cssSelector("ul[id='homefeatured']>li"));
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,500)");
        WebElement product2 = listProduct.get(1);
        actions.click(product2).build().perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("h1[itemprop='name']"))));
        WebElement cart = driver.findElement(By.cssSelector("a[href='http://automationpractice.com/index.php?controller=order']"));
        actions.click(cart).build().perform();
        WebElement buttonCart = driver.findElement(By.cssSelector("a[id='button_order_cart']"));
        Assertions.assertTrue(buttonCart.isDisplayed());
    }

}
