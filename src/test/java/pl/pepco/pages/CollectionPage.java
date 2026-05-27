package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CollectionPage extends BasePage {
    private static final By PRODUCT_LINK = By.cssSelector("a[href*='/products/']");

    @FindBy(css = "h1")
    private WebElement title;

    @FindBy(css = "a[href*='/products/']")
    private List<WebElement> productLinks;

    public CollectionPage(WebDriver driver) {
        super(driver);
    }

    public void openCollection(String path) {
        open(path);
    }

    public String getTitle() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(title)).getText();
        } catch (WebDriverException ignored) {
            return null;
        }
    }

    public boolean areProductsVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(productLinks));
            return !productLinks.isEmpty();
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String clickFirstProduct() {
        return clickProduct(0);
    }

    public String clickProduct(int index) {
        List<WebElement> products = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(PRODUCT_LINK, index));
        if (products.size() <= index) {
            return null;
        }
        String productUrl = products.get(index).getAttribute("href");
        click(products.get(index));
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
