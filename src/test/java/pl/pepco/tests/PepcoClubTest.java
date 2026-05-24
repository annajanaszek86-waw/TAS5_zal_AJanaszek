package pl.pepco.tests;

import org.junit.jupiter.api.Test;
import pl.pepco.pages.PepcoClubPage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PepcoClubTest extends BaseTest {
    @Test
    void pepcoClubPageOpens() {
        PepcoClubPage page = new PepcoClubPage(driver);
        page.openClub();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(driver.getCurrentUrl().contains("pepco-club"));
        assertTrue(page.isTitleVisible());
        takeScreenshot("pepco_club_page");
    }

    @Test
    void appStoreLinkIsVisible() {
        PepcoClubPage page = openClubPage();
        String href = page.getLink(PepcoClubPage.APP_STORE);
        assertNotNull(href);
        assertTrue(href.contains("apple.com"));
        takeScreenshot("pepco_club_app_store");
    }

    @Test
    void googlePlayLinkIsVisible() {
        PepcoClubPage page = openClubPage();
        String href = page.getLink(PepcoClubPage.GOOGLE_PLAY);
        assertNotNull(href);
        assertTrue(href.contains("play.google.com"));
        takeScreenshot("pepco_club_google_play");
    }

    @Test
    void appStoreLinkIsActive() throws IOException, InterruptedException {
        PepcoClubPage page = openClubPage();
        String href = page.getLink(PepcoClubPage.APP_STORE);
        assertNotNull(href);
        assertTrue(href.contains("apple.com"));
        assertTrue(statusCode(href) < 400);
    }

    @Test
    void googlePlayLinkIsActive() throws IOException, InterruptedException {
        PepcoClubPage page = openClubPage();
        String href = page.getLink(PepcoClubPage.GOOGLE_PLAY);
        assertNotNull(href);
        assertTrue(href.contains("play.google.com"));
        assertTrue(statusCode(href) < 400);
    }

    private PepcoClubPage openClubPage() {
        PepcoClubPage page = new PepcoClubPage(driver);
        page.openClub();
        page.acceptCookies();
        page.waitForTags(3);
        return page;
    }

    private int statusCode(String href) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(href))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();
    }
}
