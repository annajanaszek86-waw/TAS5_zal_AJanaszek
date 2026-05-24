package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.logging.LogType;
import pl.pepco.pages.BasePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediarithmicsPixelStatusTest extends BaseTest {
    @Test
    void mediarithmicsVisitsPixelAfterConsent() {
        BasePage page = new BasePage(driver);
        page.open("/");
        page.waitForTags(4);
        page.acceptCookies();

        driver.manage().logs().get(LogType.PERFORMANCE);
        page.waitForTags(8);

        List<String> requests = page.getDmpRequests();
        List<String> visits = requests.stream()
                .filter(request -> request.contains("events.mediarithmics.com/v1/visits/pixel"))
                .toList();

        System.out.println("Requestow Mediarithmics: " + requests.size());
        requests.stream().limit(5).forEach(request -> System.out.println(request.substring(0, Math.min(300, request.length()))));

        assertFalse(requests.isEmpty(), "Brak requestow Mediarithmics");
        assertFalse(visits.isEmpty(), "Brak visits/pixel po akceptacji zgody");
        assertTrue(visits.get(0).contains("page_path"));
        assertTrue(visits.get(0).contains("page_type"));

        takeScreenshot("dmp_detector_visits_pixel");
    }
}
