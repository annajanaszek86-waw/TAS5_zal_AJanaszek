package pl.pepco.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.pepco.pages.StoreLocatorPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreLocatorCsvTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/test_data/store_locator_data.csv", numLinesToSkip = 1)
    void searchStoreByCityFromCsv(String name, String city, String expectedText) {
        StoreLocatorPage page = new StoreLocatorPage(driver);
        page.openLocator();
        page.acceptCookies();
        page.waitForTags(3);

        WebElement search = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("store-locator-search-input")));
        search.clear();
        search.sendKeys(city);
        page.waitForTags(3);

        String pageSource = driver.getPageSource().toLowerCase();
        assertTrue(pageSource.contains(expectedText.toLowerCase().substring(0, Math.min(6, expectedText.length()))));

        takeScreenshot("store_locator_csv_" + name);
        System.out.println("Testuje losowe miasto: " + city);
    }
}
