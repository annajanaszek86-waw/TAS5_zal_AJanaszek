package pl.pepco.tests;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.pepco.pages.BasePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DmpEventsTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "home_page, /",
            "collection_page, /collections/dom/meble/kosze-i-pudelka"
    })
    void mediarithmicsDiagnostic(String name, String path) {
        BasePage page = new BasePage(driver);
        page.open(path);
        page.acceptCookies();
        page.waitForTags(8);

        List<String> requests = page.getDmpRequests();
        System.out.println("[" + name + "] Liczba requestow Mediarithmics: " + requests.size());
        requests.stream().limit(5).forEach(request -> System.out.println(request.substring(0, Math.min(300, request.length()))));

        takeScreenshot("dmp_diagnostic_" + name);
        Assumptions.assumeFalse(requests.isEmpty(), "[" + name + "] Brak requestow Mediarithmics w logach Selenium");
        assertFalse(requests.isEmpty());
    }
}
