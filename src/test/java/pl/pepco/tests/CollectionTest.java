package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.pepco.pages.CollectionPage;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("pl.pepco.testdata.CollectionData#collections")
    void collectionProducts(String name, String path, String expectedTitle) {
        CollectionPage page = new CollectionPage(driver);
        page.openCollection(path);
        page.acceptCookies();
        page.waitForTags(2);

        String title = page.getTitle();
        assertNotNull(title, "[" + name + "] Brak tytulu");
        assertTrue(
                normalize(title).contains(normalize(expectedTitle)),
                "[" + name + "] Oczekiwany tytul: " + expectedTitle + ", aktualny tytul: " + title
        );
        assertTrue(page.areProductsVisible(), "[" + name + "] Brak produktow");

        takeScreenshot("collection_" + name);
    }

    @Test
    void clickProductOpensPdp() {
        CollectionPage page = new CollectionPage(driver);
        page.openCollection("/collections/dom/oswietlenie");
        page.acceptCookies();
        page.waitForTags(2);

        page.clickFirstProduct();
        page.waitForTags(2);

        assertTrue(driver.getCurrentUrl().contains("/products/"));
        takeScreenshot("pdp_test");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pl.pepco.testdata.CollectionData#collections")
    void priceFilterVisible(String name, String path, String expectedTitle) {
        CollectionPage page = new CollectionPage(driver);
        page.openCollection(path);
        page.acceptCookies();
        page.waitForTags(2);

        assertTrue(page.clickFilter("Przedział cenowy"), "[" + name + "] Brak filtra ceny");
        takeScreenshot("filter_" + name);
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("\u0142", "l")
                .replace("\u0141", "l")
                .toLowerCase();
    }
}
