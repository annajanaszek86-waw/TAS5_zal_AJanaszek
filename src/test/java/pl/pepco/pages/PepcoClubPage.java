package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PepcoClubPage extends BasePage {
    private static final By TITLE = By.cssSelector("h1");
    public static final By APP_STORE = By.xpath("//a[contains(@href,'apple.com')]");
    public static final By GOOGLE_PLAY = By.xpath("//a[contains(@href,'play.google.com')]");

    public PepcoClubPage(WebDriver driver) {
        super(driver);
    }

    public void openClub() {
        open("/pepco-club");
    }

    public boolean isTitleVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public String getLink(By selector) {
        return driver.findElements(selector).stream()
                .findFirst()
                .map(element -> element.getAttribute("href"))
                .orElse(null);
    }

    public boolean hasLink(By selector) {
        return !driver.findElements(selector).isEmpty();
    }
}
