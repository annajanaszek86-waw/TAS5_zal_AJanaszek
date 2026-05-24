package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import pl.pepco.pages.TrendyPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TrendyTest extends BaseTest {
    @Test
    void trendyPageOpens() {
        TrendyPage page = new TrendyPage(driver);
        page.openTrendy();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(driver.getCurrentUrl().contains("oferta-gazetkowa"));
        takeScreenshot("trendy_page");
    }

    @Test
    void productsAreVisible() {
        TrendyPage page = new TrendyPage(driver);
        page.openTrendy();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(page.areProductsVisible());
        takeScreenshot("trendy_products");
    }

    @Test
    void clickProductOpensPdp() {
        TrendyPage page = new TrendyPage(driver);
        page.openTrendy();
        page.acceptCookies();
        page.waitForTags(3);

        boolean clicked = page.clickProduct(0);
        page.waitForTags(3);

        assertTrue(clicked, "Nie udalo sie kliknac produktu");
        assertTrue(driver.getCurrentUrl().contains("/products/"));
    }
}
