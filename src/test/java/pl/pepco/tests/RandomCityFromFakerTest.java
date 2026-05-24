package pl.pepco.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import pl.pepco.pages.StoreLocatorPage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomCityFromFakerTest extends BaseTest {
    @Test
    void randomCityFromFaker() {
        StoreLocatorPage page = new StoreLocatorPage(driver);
        page.openLocator();
        page.acceptCookies();
        page.waitForTags(3);

        String city = new Faker(Locale.forLanguageTag("pl-PL"))
                .options()
                .option("Warszawa", "Krakow", "Gdansk", "Wroclaw", "Poznan", "Lodz");
        System.out.println("Testuje miasto: " + city);

        assertTrue(page.searchStore(city), "Nie udalo sie wpisac miasta w wyszukiwarke");

        String value = page.getSearchValue();
        assertFalse(value.isBlank(), "Pole wyszukiwania jest puste");
        assertTrue(page.searchValueContainsCity(city), "Pole wyszukiwania nie zawiera miasta: " + city);
        assertTrue(page.hasSearchResultForCity(city), "Brak wynikow wyszukiwania dla miasta: " + city);
        assertTrue(page.openFirstStoreDetailsForCity(city), "Nie udalo sie otworzyc sklepu dla miasta: " + city);
        assertTrue(page.setFirstStoreAsYourStore(), "Nie udalo sie ustawic sklepu jako Twoj");

        page.waitForTags(2);
        assertFalse(driver.getCurrentUrl().isBlank(), "Po kliknieciu sklepu URL jest pusty");
        takeScreenshot("random_city_favorite_store");
        System.out.println("Wpisano: " + value);
    }
}
