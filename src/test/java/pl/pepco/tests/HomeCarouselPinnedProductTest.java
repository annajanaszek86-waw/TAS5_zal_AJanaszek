package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import pl.pepco.pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeCarouselPinnedProductTest extends BaseTest {
    @Test
    void carouselCtaPinnedProductOpensPdp() {
        HomePage page = new HomePage(driver);
        page.openHome();
        page.waitForTags(10);
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(page.moveHeroCarousel(), "Nie udalo sie przejsc po karuzeli na stronie glownej");
        assertTrue(page.openShoppableImageFromCta(), "Nie udalo sie kliknac CTA z sekcji na stronie glownej");
        assertTrue(page.openPinnedProductDetails(), "Nie udalo sie otworzyc produktu z pinu na zdjeciu");
        assertTrue(driver.getCurrentUrl().contains("/products/"), "Nie otwarto strony produktu");

        takeScreenshot("home_carousel_pinned_product");
    }
}
