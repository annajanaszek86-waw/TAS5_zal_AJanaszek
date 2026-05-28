package pl.pepco.tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import pl.pepco.pages.HomePage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewsletterFakerTest extends BaseTest {
    @Test
    void entersNewsletterEmailFromFaker() {
        HomePage page = new HomePage(driver);
        page.openHome();
        page.waitForTags(10);
        page.acceptCookies();
        page.waitForTags(10);

        String email = new Faker(Locale.forLanguageTag("pl-PL"))
                .internet()
                .emailAddress();
        email = java.text.Normalizer.normalize(email, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("ł", "l")
                .replace("Ł", "L");
        System.out.println("Newsletter email: " + email);

        assertTrue(page.enterNewsletterEmail(email), "Nie udalo sie wpisac maila do newslettera");
        assertEquals(email, page.getNewsletterEmailValue(), "Pole newslettera zawiera inny adres e-mail");

        takeScreenshot("newsletter_faker_email_entered");
    }
}
