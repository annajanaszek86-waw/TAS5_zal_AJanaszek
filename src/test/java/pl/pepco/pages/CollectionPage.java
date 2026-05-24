package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CollectionPage extends BasePage {
    private static final By TITLE = By.cssSelector("h1");
    private static final By PRODUCT_LINK = By.cssSelector("a[href*='/products/']");

    public CollectionPage(WebDriver driver) {
        super(driver);
    }

    public void openCollection(String path) {
        open(path);
    }

    public String getTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE)).getText();
        } catch (WebDriverException ignored) {
            return null;
        }
    }

    public boolean areProductsVisible() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_LINK));
            return !driver.findElements(PRODUCT_LINK).isEmpty();
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String clickFirstProduct() {
        List<WebElement> products = driver.findElements(PRODUCT_LINK);
        if (products.isEmpty()) {
            return null;
        }
        String productUrl = products.get(0).getAttribute("href");
        products.get(0).click();
        return productUrl;
    }

    public boolean clickFilter(String filterName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., '" + filterName + "')]"))).click();
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }
}
