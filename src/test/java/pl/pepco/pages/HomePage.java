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
                    "or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'potwierdzeniem') " +
                    "or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'dziekujemy') " +
                    "or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'dziękujemy')]"
    );
    private static final By HERO_PREVIOUS_BUTTON = By.cssSelector(
            "main button[aria-label*='previous' i], main button[aria-label*='poprzed' i], " +
                    "main button[aria-label*='prev' i], main div.interaction-button button:first-of-type"
    );
    private static final By HERO_NEXT_BUTTON = By.cssSelector(
            "main button[aria-label*='next' i], main button[aria-label*='nastep' i], " +
                    "main button[aria-label*='następ' i], main div.interaction-button button:last-of-type"
    );
    private static final By SHOPPABLE_IMAGE_CTA = By.cssSelector(
            "main > div > div:nth-of-type(5) > div a button, main div:nth-of-type(5) > div button, " +
                    "main a[href*='/products/'] button, main section button[aria-label*='product' i]"
    );
    private static final By PRODUCT_PIN = By.xpath(
            "(//main//button[contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'view') " +
                    "and contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'details')])[1]"
    );
    private static final By PINNED_PRODUCT_LINK = By.cssSelector(
            "[id^='product-details-'] a[href*='/products/'], main a[href*='/products/']"
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

    public boolean enterNewsletterEmail(String email) {
        try {
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(NEWSLETTER_EMAIL));
            scrollIntoView(emailInput);
            emailInput.clear();
            emailInput.sendKeys(email);
            acceptNewsletterConsentIfPresent();
            wait.until(webDriver -> email.equals(emailInput.getAttribute("value")));
            wait.until(webDriver -> driver.findElement(NEWSLETTER_CONSENT).isSelected());
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String getNewsletterEmailValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(NEWSLETTER_EMAIL)).getAttribute("value");
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
            if (driver.getCurrentUrl().contains("/products/")) {
                return true;
            }
            clickIfPresent(PRODUCT_PIN);
            String productUrl = wait.until(webDriver -> (String) ((org.openqa.selenium.JavascriptExecutor) webDriver)
                    .executeScript("""
                            const links = [...document.querySelectorAll("[id^='product-details-'] a[href*='/products/'], main a[href*='/products/']")]
                                .filter(link => link.offsetWidth > 0 && link.offsetHeight > 0);
                            return links.length ? links[0].href : null;
                            """));
            driver.get(productUrl);
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
            try {
                click(elements.get(0));
            } catch (WebDriverException ignored) {
                // Optional controls on the home page can be present in DOM but not interactable.
            }
        }
    }
}
