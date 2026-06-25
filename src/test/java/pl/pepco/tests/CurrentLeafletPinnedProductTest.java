package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import pl.pepco.pages.TrendyPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrentLeafletPinnedProductTest extends BaseTest {
    @Test
    void currentLeafletPinnedProductOpensPdp() {
        TrendyPage page = new TrendyPage(driver);
        page.openTrendy();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(page.clickCurrentLeafletPinnedProduct(), "Nie udalo sie otworzyc produktu z pinu na zdjeciu");
        assertTrue(driver.getCurrentUrl().contains("/products/"), "Nie otwarto strony produktu");

        takeScreenshot("current_leaflet_pinned_product");
    }
}
