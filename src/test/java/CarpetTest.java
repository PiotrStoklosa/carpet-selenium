import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CarpetTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final Random random = new Random();

    private final List<String> availableMaterials = List.of("wool", "cotton", "polypropylene");
    private final List<String> availableShapes = List.of("rectangle", "square", "circle");

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Piotr\\OneDrive\\Pulpit\\Studia\\e-business\\task6\\carpet-selenium\\msedgedriver.exe");
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
    }

    @Test
    public void testLogoVisibility() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement img = driver.findElement(By.cssSelector("a.active img"));

        assertTrue(img.isDisplayed());
    }

    @Test
    public void testLogoClick() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement link = driver.findElement(By.cssSelector("a.active"));
        link.click();
        String currentUrl = driver.getCurrentUrl();

        assertEquals("http://localhost:3000/", currentUrl);
    }

    @Test
    public void testShoppingCartButton() {
        driver.get("http://localhost:3000");
        WebElement button = driver.findElement(By.cssSelector("[data-testid='ShoppingCartIcon']"));
        button.click();
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:3000/cart", currentUrl);
    }

    @Test
    public void testShoppingCartEmpty() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement button = driver.findElement(By.cssSelector("[data-testid='ShoppingCartIcon']"));
        button.click();

        WebElement emptyCartMessage = driver.findElement(By.xpath("//p[contains(text(), 'Your cart is empty')]"));
        assertTrue(emptyCartMessage.isDisplayed());

    }

    @Test
    public void CartShouldDisplayProperAmountOfItemsOnTheMainPage() {
        driver.get("http://localhost:3000");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 3; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            WebElement button = driver.findElement(By.xpath("//p//button"));
            button.click();
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

        WebElement badgeNumberElement = driver.findElement(By.xpath("//span[contains(@class, 'MuiBadge-badge')]"));
        String badgeNumberText = badgeNumberElement.getText();
        int badgeNumber = Integer.parseInt(badgeNumberText);
        assertEquals(3, badgeNumber, "The Cart icon should display 3 items");

    }

    @Test
    public void CartShouldBeEmptyWhenAddingAndRemovingItem() {
        driver.get("http://localhost:3000");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 3; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            WebElement button = driver.findElement(By.xpath("//p//button"));
            button.click();
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();
        for (int i = 0; i < 3; i++) {
            WebElement minus = driver.findElement(By.xpath("//ul//li//button"));
            minus.click();
        }
        WebElement emptyCartMessage = driver.findElement(By.xpath("//p[contains(text(), 'Your cart is empty')]"));
        assertTrue(emptyCartMessage.isDisplayed(), "The message 'Your cart is empty' should be displayed");
    }

    @Test
    public void afterByingThePageShouldBeRedirectedToThankYouPage() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
        carpetLink.click();
        WebElement button = driver.findElement(By.xpath("//p//button"));
        button.click();
        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement kupButton = driver.findElement(By.xpath("//button[contains(text(), 'KUP')]"));
        kupButton.click();

        String currentUrl = driver.getCurrentUrl();

        assertEquals("http://localhost:3000/thankyou", currentUrl);

    }

    @Test
    public void allAddedItemsShouldBeVisibleInCart() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            WebElement button = driver.findElement(By.xpath("//p//button"));
            button.click();
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        List<WebElement> items = driver.findElements(By.xpath("//p[contains(text(), 'Quantity')]"));

        assertEquals(items.size(), 4);

    }

    @Test
    public void onlyIndividualItemsAreVisibleInTheCart() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            int quantity = random.nextInt(6) + 5;
            for (int j = 0; j < quantity; j++) {
                WebElement button = driver.findElement(By.xpath("//p//button"));
                button.click();
            }
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        List<WebElement> items = driver.findElements(By.xpath("//p[contains(text(), 'Quantity')]"));

        assertEquals(items.size(), 4);

    }

    @Test
    public void addingItemsProvidesProperPrice() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
        carpetLink.click();
        WebElement priceOfOneCarpetWithCurrency = driver.findElement(By.xpath("//div[contains(., ' PLN')]"));


        int priceOfOneCarpet = Integer.parseInt(Arrays.stream(priceOfOneCarpetWithCurrency.getText().split("\n")).filter(s -> s.contains("PLN")).findFirst().orElse("0 PLN").split(" ")[0]);

        int quantity = random.nextInt(6) + 5;
        for (int j = 0; j < quantity; j++) {
            WebElement button = driver.findElement(By.xpath("//p//button"));
            button.click();
        }
        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement carpet = driver.findElement(By.xpath("//p[contains(text(), 'Quantity')]"));

        int actualPrice = Integer.parseInt(carpet.getText().split(" ")[2]);

        assertEquals(priceOfOneCarpet * quantity, actualPrice);

    }

    @Test
    public void afterIncreasingAmountInCartValuesUpdate() {

        driver.get("http://localhost:3000");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));

        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
        carpetLink.click();
        WebElement button = driver.findElement(By.xpath("//p//button"));
        button.click();
        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();


        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement carpet = driver.findElement(By.xpath("//p[contains(text(), 'Quantity')]"));
        int oldPrice = Integer.parseInt(carpet.getText().split(" ")[2]);
        int oldQuantity = Integer.parseInt(carpet.getText().split(" ")[1].split(",")[0]);


        WebElement plus = driver.findElement(By.cssSelector("svg[data-testid='AddIcon']"));
        plus.click();

        carpet = driver.findElement(By.xpath("//p[contains(text(), 'Quantity')]"));
        int newPrice = Integer.parseInt(carpet.getText().split(" ")[2]);
        int newQuantity = Integer.parseInt(carpet.getText().split(" ")[1].split(",")[0]);

        assertTrue(oldPrice != newPrice);
        assertTrue(oldQuantity != newQuantity);

    }

    @Test
    public void afterDecreasingAmountInCartValuesUpdate() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 2; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
            carpetLink.click();
            WebElement button = driver.findElement(By.xpath("//p//button"));
            button.click();
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }


        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement carpet = driver.findElement(By.xpath("//p[contains(text(), 'Quantity')]"));
        int oldPrice = Integer.parseInt(carpet.getText().split(" ")[2]);
        int oldQuantity = Integer.parseInt(carpet.getText().split(" ")[1].split(",")[0]);


        WebElement minus = driver.findElement(By.cssSelector("svg[data-testid='RemoveIcon']"));
        minus.click();

        carpet = driver.findElement(By.xpath("//p[contains(text(), 'Quantity')]"));
        int newPrice = Integer.parseInt(carpet.getText().split(" ")[2]);
        int newQuantity = Integer.parseInt(carpet.getText().split(" ")[1].split(",")[0]);

        assertTrue(oldPrice != newPrice);
        assertTrue(oldQuantity != newQuantity);

    }

    @Test
    public void cartSumCheck() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        int correctSum = 0;

        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            int quantity = random.nextInt(6) + 5;
            for (int j = 0; j < quantity; j++) {
                WebElement button = driver.findElement(By.xpath("//p//button"));
                button.click();
            }

            WebElement priceOfOneCarpetWithCurrency = driver.findElement(By.xpath("//div[contains(., ' PLN')]"));
            int priceOfOneCarpet = Integer.parseInt(Arrays.stream(priceOfOneCarpetWithCurrency.getText().split("\n")).filter(s -> s.contains("PLN")).findFirst().orElse("0 PLN").split(" ")[0]);

            correctSum += priceOfOneCarpet * quantity;

            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        List<WebElement> total = driver.findElements(By.xpath("//p"));

        assertEquals(correctSum, Integer.valueOf(total.get(total.size() - 1).getText().split(" ")[0]));

    }

    @Test
    public void afterAddingCarpetCorrectItemIsInTheCart() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(2);
        carpetLink.click();
        WebElement button = driver.findElement(By.xpath("//p//button"));
        button.click();

        WebElement titleElement = driver.findElement(By.xpath("//h4"));
        String title = titleElement.getText();

        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement cartItem = driver.findElement(By.xpath("//ul//li//div//span"));

        String cartItemTitle = cartItem.getText().split(",")[0];

        assertEquals(title, cartItemTitle);


    }


    @Test
    public void afterBuyingCartShouldBeEmpty() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
        carpetLink.click();
        WebElement button = driver.findElement(By.xpath("//p//button"));
        button.click();
        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        WebElement kupButton = driver.findElement(By.xpath("//button[contains(text(), 'KUP')]"));
        kupButton.click();

        button = driver.findElement(By.cssSelector("[data-testid='ShoppingCartIcon']"));
        button.click();

        WebElement emptyCartMessage = driver.findElement(By.xpath("//p[contains(text(), 'Your cart is empty')]"));
        assertTrue(emptyCartMessage.isDisplayed());

    }

    @Test
    public void buyingNotPossibleIfCartIsEmpty() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement button = driver.findElement(By.cssSelector("[data-testid='ShoppingCartIcon']"));
        button.click();

        WebElement emptyCartMessage = driver.findElement(By.xpath("//p[contains(text(), 'Your cart is empty')]"));
        assertTrue(emptyCartMessage.isDisplayed());

        try {
            driver.findElement(By.xpath("//button[contains(text(), 'KUP')]"));
            fail();
        } catch (NoSuchElementException ignored) {
        }

    }


    @Test
    public void shapeIsAvailable() {


        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            boolean found = false;
            for (String shape : availableShapes) {
                try {
                    driver.findElement(By.xpath("//div[contains(., '" + shape + "')]"));
                    found = true;
                    break;
                } catch (NoSuchElementException ignored) {
                }
            }
            if (!found) {
                fail();
            }
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

    }

    @Test
    public void materialIsAvailable() {


        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();
            boolean found = false;
            for (String material : availableMaterials) {
                try {
                    driver.findElement(By.xpath("//div[contains(., '" + material + "')]"));
                    found = true;
                    break;
                } catch (NoSuchElementException ignored) {
                }
            }
            if (!found) {
                fail();
            }
            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

    }

    @Test
    public void testCarpetImageVisibility() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        for (int i = 0; i < 4; i++) {
            WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(i);
            carpetLink.click();

            WebElement img = driver.findElement(By.xpath("//img[@src='/pictures/" + (i + 1) + ".jpg']"));

            assertTrue(img.isDisplayed());

            WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
            arrowBackIcon.click();
        }

    }

    @Test
    public void formShouldContainsShippingAddressEmailAndBlikFields() {

        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href^='/item/']")));
        WebElement carpetLink = driver.findElements(By.cssSelector("a[href^='/item/']")).get(0);
        carpetLink.click();
        WebElement button = driver.findElement(By.xpath("//p//button"));
        button.click();
        WebElement arrowBackIcon = driver.findElement(By.cssSelector("svg[data-testid='ArrowBackIcon']"));
        arrowBackIcon.click();

        WebElement cart = driver.findElement(By.xpath("//a//button"));
        cart.click();

        driver.findElement(By.xpath("//div[contains(@class, 'MuiFormControl-root') and .//span[contains(text(), 'Shipping Address')]]/div/input"));
        driver.findElement(By.xpath("//div[contains(@class, 'MuiFormControl-root') and .//span[contains(text(), 'Email')]]/div/input"));
        driver.findElement(By.xpath("//div[contains(@class, 'MuiFormControl-root') and .//span[contains(text(), 'Blik')]]/div/input"));

    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
