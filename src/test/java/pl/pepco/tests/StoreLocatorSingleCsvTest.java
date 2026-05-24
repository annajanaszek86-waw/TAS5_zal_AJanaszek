package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import pl.pepco.pages.StoreLocatorPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoreLocatorSingleCsvTest extends BaseTest {
    @Test
    void searchStoreBySingleCityFromCsv() throws IOException {
        String[] row = Files.readAllLines(Path.of("src/test/resources/test_data/store_locator_data.csv"))
                .stream()
                .skip(1)
                .findFirst()
                .orElseThrow()
                .split(",");

        String name = row[0];
        String city = row[1];
        String expectedText = row[2];

        StoreLocatorPage page = new StoreLocatorPage(driver);
        page.openLocator();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(page.searchStore(city), "[" + name + "] Nie udalo sie wpisac miasta w wyszukiwarke");

        String value = page.getSearchValue();
        assertFalse(value.isBlank(), "[" + name + "] Pole wyszukiwania jest puste");
        assertTrue(page.searchValueContainsCity(expectedText), "[" + name + "] Pole wyszukiwania nie zawiera miasta: " + city);
        assertTrue(page.hasSearchResultForCity(expectedText), "[" + name + "] Brak wynikow wyszukiwania dla miasta: " + city);
        assertTrue(page.openFirstStoreDetailsForCity(expectedText), "[" + name + "] Nie udalo sie otworzyc sklepu dla miasta: " + city);
        assertTrue(page.setFirstStoreAsYourStore(), "[" + name + "] Nie udalo sie ustawic sklepu jako Twoj");

        takeScreenshot("store_locator_single_csv_favorite_" + name);
        System.out.println("Testuje jedno miasto z CSV: " + city);
    }
}
