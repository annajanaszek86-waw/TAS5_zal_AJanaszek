package pl.pepco.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.pepco.pages.CollectionPage;
import pl.pepco.pages.ProductPage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("pl.pepco.testdata.CollectionData#returnPolicyCollections")
    void thirdProductOpensReturnPolicy(String name, String path, String expectedUrlPart) {
        CollectionPage page = new CollectionPage(driver);
        page.openCollection(path);
        page.acceptCookies();
        page.waitForTags(2);

        String productUrl = page.clickProduct(2);
        assertNotNull(productUrl, "[" + name + "] Brak trzeciego produktu w kolekcji");
        assertTrue(driver.getCurrentUrl().contains("/products/"), "Nie otwarto strony produktu");

        ProductPage productPage = new ProductPage(driver);
        assertTrue(productPage.clickReturnInfo(), "Brak linku 30 dni na zwrot");
        assertTrue(productPage.openReturnRules(expectedUrlPart), "Nie otwarto strony zasad zwrotu");
        assertTrue(driver.getCurrentUrl().contains(expectedUrlPart));
        productPage.acceptCookies();
        productPage.waitUntilPageSettled(10);

        takeScreenshot(name);
    }
}
