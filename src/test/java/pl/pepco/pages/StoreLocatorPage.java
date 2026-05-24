package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.text.Normalizer;
import java.util.List;

public class StoreLocatorPage extends BasePage {
    private static final By SEARCH_INPUT = By.cssSelector("input[placeholder*='lokalizacj'], input[type='search'], input[name='search']");
    private static final By MAP = By.cssSelector("#map, .map-container, [class*='map']");
    private static final By STORE_RESULT = By.cssSelector(
            "[class*='store'], [class*='locator'], [class*='shop'], [class*='address'], li, article"
    );
    private static final By STORE_RESULT_LINK = By.cssSelector(
            "[class*='store'] a, [class*='locator'] a, [class*='shop'] a, a[href*='store-locator']"
    );

    public StoreLocatorPage(WebDriver driver) {
        super(driver);
    }

    public void openLocator() {
        open("/store-locator");
    }

    public boolean searchStore(String city) {
        try {
            WebElement search = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
            search.clear();
            search.sendKeys(city);
            waitForTags(2);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String getSearchValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT)).getAttribute("value");
    }

    public boolean isMapVisible() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(MAP));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean hasSearchResultForCity(String city) {
        try {
            String expectedCity = normalize(city);
            return wait.until(driver -> driver.findElements(STORE_RESULT).stream()
                    .map(WebElement::getText)
                    .map(this::normalize)
                    .anyMatch(text -> text.contains(expectedCity)));
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean clickFirstStoreResult() {
        try {
            List<WebElement> results = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(STORE_RESULT_LINK, 0));
            results.get(0).click();
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}
