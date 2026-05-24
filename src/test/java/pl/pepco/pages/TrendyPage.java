package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class TrendyPage extends BasePage {
    private static final By TITLE = By.cssSelector("h1");
    private static final By PRODUCT_LINK = By.cssSelector("a[href*='/products/']");

    public TrendyPage(WebDriver driver) {
        super(driver);
    }

    public void openTrendy() {
        open("/trendy/oferta-gazetkowa");
    }

    public String getTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE)).getText();
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
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
            waitForTags(1);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", product);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }
}
