package StepDefinition;

import io.cucumber.java.en.*;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.*;

public class EcommerceShopping {
    private WebDriver driver;
    private WebDriverWait wait;

    //Browser is initialized parametrically based on input from feature file (e.g., "chrome", "firefox").
    @Given("User opens the {string} browser and goes to the login page")
    public void openBrowser(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
                options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browserName);
        }
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.n11.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.className("btnSignIn"))).click();
    }
    //Login with credentials taken from environment variables (MY_APP_USERNAME, MY_APP_PASSWORD).
    @And("User enters valid credentials and submits")
    public void enterCredentialsAndSubmit() {
        String email = System.getenv("MY_APP_USERNAME");
        String password = System.getenv("MY_APP_PASSWORD");
        if (email == null || password == null) {
            throw new RuntimeException("Environment variables for credentials are missing!");
        }
        enterTextById("email", email);
        enterTextById("password", password);
        clickById("loginButton");
    }
    @When("User searches for {string}")
    public void searchProduct(String keyword) {
        enterTextById("searchData", keyword);
        driver.findElement(By.id("searchData")).sendKeys(Keys.ENTER);
    }
    @And("User sets price range from {string} to {string}")
    public void setPriceRange(String minPrice, String maxPrice) {
        enterTextById("minPrice", minPrice);
        enterTextById("maxPrice", maxPrice);
        clickById("priceSearchButton");
    }
    //Attempts to close cookie pop-up if it exists.
    @And("User closes the popup if it appears")
    public void closePopupIfPresent() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script =
                    "let elements = document.querySelectorAll('div');" +
                            "for (let el of elements) {" +
                            "  if (el.innerText.trim() === 'Tümünü Kabul Et') {" +
                            "    el.click(); return true;" +
                            "  } } return false;";
            js.executeScript(script);
        } catch (Exception ignored) {
        }
    }

    // Selects a random product from the last row by scrolling to the bottom.
    @Then("User scrolls to bottom and clicks a random product from bottom row")
    public void selectRandomProductFromLastRow() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        WebElement listingUl = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("listingUl")));
        List<WebElement> products = listingUl.findElements(By.tagName("li"));
        if (products.isEmpty()) {
            throw new AssertionError("No products found on the listing page!");
        }
        int startIndex = Math.max(0, products.size() - 4);
        int randomIndex = new Random().nextInt(Math.min(4, products.size() - startIndex)) + startIndex;
        products.get(randomIndex).click();
    }

    // Adds product to cart; tries to click button, falls back to JavaScript if needed.
    @And("User adds product to cart from the lowest rated seller")
    public void addToCartFromLowestRatedSeller() {
        try {
            WebElement addToCartBtn = driver.findElement(By.cssSelector(".b-b-add.unf-green-btn"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
            Thread.sleep(500);
            addToCartBtn.click();
        } catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement addToCartBtn = driver.findElement(By.cssSelector(".b-b-add.unf-green-btn"));
            js.executeScript("arguments[0].click();", addToCartBtn);
        } catch (InterruptedException ignored) {}
    }

    //Validates that the product has been added to the cart.
    @And("Checking that the product has been added to the cart")
    public void checkProductInCart() {
        driver.findElement(By.cssSelector(".myBasketHolder")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".box__title")));
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".box__title"));

        Assert.assertFalse("The basket is empty!", cartItems.isEmpty());
        System.out.println("Product found in cart: " + cartItems.size());
    }
    // --- Utility Methods ---
    private void enterTextById(String id, CharSequence text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        element.clear();
        element.sendKeys(text);
    }
    private void clickById(String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        element.click();
    }

    //Takes screenshot on test failure. Closes the browser session.
    @After
    public void tearDown(Scenario scenario) {
        if (driver != null && scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            System.out.println("Test failed. Screenshot captured.");
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
