package pl.pepco.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pl.pepco.pages.BasePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmpEventsTest extends BaseTest {
    @Override
    protected boolean useIncognito() {
        return false;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("pl.pepco.testdata.DmpData#pages")
    void mediarithmicsDiagnostic(String name, String path) {
        BasePage page = new BasePage(driver);
        page.open(path);
        boolean cookiesAccepted = page.acceptAllCookies();
        System.out.println("[" + name + "] Cookies zaakceptowane lub juz zapisane: " + cookiesAccepted);
        driver.navigate().refresh();
        page.waitForTags(20);
        page.waitForDmpRequests(20);

        List<String> requests = page.getDmpRequests();
        List<Integer> visitPixelStatuses = page.getDmpVisitPixelStatuses();
        List<Integer> checkCookiePixelStatuses = page.getDmpCheckCookiePixelStatuses();
        List<String> siteTokens = page.getDmpSiteTokens();
        boolean pixelFired = page.checkPixelFired();
        boolean visitPixelFired = page.checkVisitPixelFired();
        boolean checkCookiePixelFired = page.checkCookiePixelFired();
        System.out.println("[" + name + "] Liczba requestow Mediarithmics: " + requests.size());
        System.out.println("[" + name + "] Status piksla Mediarithmics: " + (pixelFired ? "ODPALONY" : "NIE ODPALONY"));
        System.out.println("[" + name + "] visits/pixel: " + (visitPixelFired ? "ODPALONY" : "NIE ODPALONY"));
        System.out.println("[" + name + "] check_cookie/pixel: " + (checkCookiePixelFired ? "ODPALONY" : "NIE ODPALONY"));
        System.out.println("[" + name + "] Statusy HTTP visits/pixel: " + visitPixelStatuses);
        System.out.println("[" + name + "] Statusy HTTP check_cookie/pixel: " + checkCookiePixelStatuses);
        if (visitPixelStatuses.isEmpty() && checkCookiePixelStatuses.isEmpty()) {
            System.out.println("[" + name + "] Status HTTP piksla: niedostepny w logach Selenium");
        }
        System.out.println("[" + name + "] site_token: " + siteTokens);
        requests.stream().limit(5).forEach(request -> System.out.println(request.substring(0, Math.min(300, request.length()))));

        takeScreenshot("dmp_diagnostic_" + name);
        assertFalse(requests.isEmpty(), "[" + name + "] Brak requestow Mediarithmics w logach Selenium");
        assertTrue(pixelFired, "[" + name + "] Piksel Mediarithmics nie odpalil sie");
        assertTrue(siteTokens.contains("pepco-24-pl"), "[" + name + "] Brak site_token=pepco-24-pl");
        if (!visitPixelStatuses.isEmpty() || !checkCookiePixelStatuses.isEmpty()) {
            assertTrue(
                    visitPixelStatuses.contains(200) || checkCookiePixelStatuses.contains(200),
                    "[" + name + "] Piksel Mediarithmics nie zwrocil statusu 200"
            );
        }
    }
}
