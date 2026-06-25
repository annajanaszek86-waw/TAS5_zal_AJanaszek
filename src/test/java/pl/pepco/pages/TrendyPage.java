package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class TrendyPage extends BasePage {
    private static final By PRODUCT_LINK = By.cssSelector("a[href*='/products/']");
    private static final By CURRENT_LEAFLET_PIN = By.cssSelector(
            "main div:nth-of-type(5) section div:nth-of-type(2) button:nth-of-type(2), " +
                    "main div:nth-of-type(5) button:nth-of-type(2)"
    );

    @FindBy(css = "h1")
    private WebElement title;

    public TrendyPage(WebDriver driver) {
        super(driver);
    }

    public void openTrendy() {
        open("/trendy/oferta-gazetkowa");
    }

    public String getTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(title)).getText();
        } catch (WebDriverException ignored) {
            return null;
        }
    }

    public List<String> getProductLinks() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_LINK));
            return driver.findElements(PRODUCT_LINK).stream()
                    .map(element -> element.getAttribute("href"))
                    .toList();
        } catch (WebDriverException ignored) {
            return List.of();
        }
    }

    public boolean areProductsVisible() {
        return !getProductLinks().isEmpty();
    }

    public boolean clickProduct(int index) {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_LINK));
            if (index >= products.size()) {
                return false;
            }
            WebElement product = products.get(index);
            click(product);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean clickCurrentLeafletPinnedProduct() {
        try {
            WebElement pin = wait.until(ExpectedConditions.elementToBeClickable(CURRENT_LEAFLET_PIN));
            click(pin);
            wait.until(ExpectedConditions.urlContains("/products/"));
            return true;
        } catch (WebDriverException pinClickFailed) {
            return clickProduct(0);
        }
    }
}
