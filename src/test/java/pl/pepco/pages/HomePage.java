package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class HomePage extends BasePage {
    @FindBy(css = "a.logo, img[alt*='Pepco'], img[alt*='pepco']")
    private WebElement logo;

    private static final By NEWSLETTER_EMAIL = By.cssSelector(
            "#newsletter-email, input[aria-label*='e-mail' i], input[placeholder*='e-mail' i]"
    );
    private static final By NEWSLETTER_CONSENT = By.cssSelector("#subscriberEmail");
    private static final By NEWSLETTER_CONSENT_LABEL = By.cssSelector("label[for='subscriberEmail']");
    private static final By NEWSLETTER_SUBMIT = By.xpath(
            "//form[.//*[@id='newsletter-email']]//button[@type='submit']"
    );
    private static final By NEWSLETTER_MESSAGE = By.xpath(
            "//*[contains(normalize-space(.), 'Prawie gotowe') " +
                    "or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'potwierdzeniem')]"
    );
    private static final By HERO_PREVIOUS_BUTTON = By.cssSelector(
            "main .home > div:first-child button:nth-of-type(1), main div.interaction-button button:nth-of-type(1)"
    );
    private static final By HERO_NEXT_BUTTON = By.cssSelector(
            "main .home > div:first-child button:nth-of-type(2), main div.interaction-button button:nth-of-type(2)"
    );
    private static final By SHOPPABLE_IMAGE_CTA = By.cssSelector(
            "main > div > div:nth-of-type(5) > div a button, main div:nth-of-type(5) > div button"
    );
    private static final By PRODUCT_PIN = By.xpath(
            "((//main//button[contains(@aria-label, 'View') and contains(@aria-label, 'details')]//*[local-name()='path']) " +
                    "| (//main/div[1]/div/main/div[5]/section/div[2]/div/button/div[2]//*[local-name()='path']))[1]"
    );
    private static final By PINNED_PRODUCT_LINK = By.xpath(
            "(//*[starts-with(@id, 'product-details-')]//a[contains(@href, '/products/')])[1]"
    );

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openHome() {
        open("/");
    }

    public boolean isLogoVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logo));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean goToCategory(String categoryName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nav//a[contains(., '" + categoryName + "')]"))).click();
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean subscribeToNewsletter(String email) {
        try {
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(NEWSLETTER_EMAIL));
            scrollIntoView(emailInput);
            emailInput.clear();
            emailInput.sendKeys(email);
            acceptNewsletterConsentIfPresent();
            wait.until(webDriver -> !emailInput.getAttribute("value").isBlank());
            wait.until(webDriver -> driver.findElement(NEWSLETTER_CONSENT).isSelected());
            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(NEWSLETTER_SUBMIT));
            click(submit);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean isNewsletterMessageVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(NEWSLETTER_MESSAGE));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean moveHeroCarousel() {
        try {
            clickIfPresent(HERO_PREVIOUS_BUTTON);
            clickIfPresent(HERO_PREVIOUS_BUTTON);
            clickIfPresent(HERO_NEXT_BUTTON);
            clickIfPresent(HERO_NEXT_BUTTON);
            clickIfPresent(HERO_NEXT_BUTTON);
            clickIfPresent(HERO_NEXT_BUTTON);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean openShoppableImageFromCta() {
        try {
            WebElement cta = wait.until(ExpectedConditions.elementToBeClickable(SHOPPABLE_IMAGE_CTA));
            click(cta);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean openPinnedProductDetails() {
        try {
            WebElement pin = wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_PIN));
            click(pin);
            WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(PINNED_PRODUCT_LINK));
            click(productLink);
            wait.until(ExpectedConditions.urlContains("/products/"));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    private void acceptNewsletterConsentIfPresent() {
        List<WebElement> checkboxes = driver.findElements(NEWSLETTER_CONSENT);
        if (checkboxes.isEmpty() || checkboxes.get(0).isSelected()) {
            return;
        }

        driver.findElements(NEWSLETTER_CONSENT_LABEL).stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .findFirst()
                .ifPresentOrElse(this::click, () -> click(checkboxes.get(0)));
    }

    private void clickIfPresent(By selector) {
        List<WebElement> elements = driver.findElements(selector);
        if (!elements.isEmpty() && elements.get(0).isDisplayed() && elements.get(0).isEnabled()) {
            click(elements.get(0));
        }
    }
}
