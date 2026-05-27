package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PepcoClubPage extends BasePage {
    public static final By APP_STORE = By.xpath("//a[contains(@href,'apple.com')]");
    public static final By GOOGLE_PLAY = By.xpath("//a[contains(@href,'play.google.com')]");

    @FindBy(css = "h1")
    private WebElement title;

    public PepcoClubPage(WebDriver driver) {
        super(driver);
    }

    public void openClub() {
        open("/pepco-club");
    }

    public boolean isTitleVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOf(title));
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

    public boolean clickLink(By selector) {
        try {
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(selector));
            click(link);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean hasLink(By selector) {
        return !driver.findElements(selector).isEmpty();
    }
}
