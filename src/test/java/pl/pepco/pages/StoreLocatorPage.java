package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
    private static final By STORE_HEADER = By.xpath("//h2[contains(normalize-space(.), 'Pepco')]");
    private static final By SET_AS_YOUR_STORE_BUTTON = By.xpath(
            "//button[contains(normalize-space(.), 'Ustaw jako')]"
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
            search.sendKeys(Keys.ENTER);
            wait.until(driver -> city.equals(getSearchValue()));
            waitForTags(3);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String getSearchValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT)).getAttribute("value");
    }

    public boolean searchValueContainsCity(String city) {
        return normalize(getSearchValue()).contains(normalize(city));
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
            click(results.get(0));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean openFirstStoreDetails() {
        try {
            List<WebElement> stores = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(STORE_HEADER, 0));
            click(stores.get(0));
            waitForTags(1);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean openFirstStoreDetailsForCity(String city) {
        try {
            String expectedCity = normalize(city);
            WebElement store = wait.until(driver -> driver.findElements(STORE_HEADER).stream()
                    .filter(element -> normalize(element.getText()).contains(expectedCity))
                    .findFirst()
                    .orElse(null));
            click(store);
            waitForTags(1);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean setFirstStoreAsYourStore() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(SET_AS_YOUR_STORE_BUTTON));
            click(button);
            waitForTags(1);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    private void click(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            element.click();
        } catch (WebDriverException ignored) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("\u0142", "l")
                .replace("\u0141", "l")
                .toLowerCase();
    }
}
