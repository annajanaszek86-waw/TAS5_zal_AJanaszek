package pl.pepco.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import pl.pepco.pages.HomePage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsletterFakerTest extends BaseTest {
    @Test
    void subscribesToNewsletterWithFakerEmail() {
        HomePage page = new HomePage(driver);
        page.openHome();
        page.waitForTags(10);
        page.acceptCookies();
        page.waitForTags(10);

        String email = new Faker(Locale.forLanguageTag("pl-PL"))
                .internet()
                .emailAddress();
        System.out.println("Newsletter email: " + email);

        assertTrue(page.subscribeToNewsletter(email), "Nie udalo sie zapisac do newslettera");
        takeScreenshot("newsletter_faker_form_submitted");

        assertTrue(page.isNewsletterMessageVisible(), "Brak komunikatu po zapisie do newslettera");
        page.waitUntilPageSettled(10);

        takeScreenshot("newsletter_faker_confirmation_message");
    }
}
