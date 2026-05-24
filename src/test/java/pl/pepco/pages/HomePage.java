package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    private static final By LOGO = By.cssSelector("a.logo, img[alt*='Pepco'], img[alt*='pepco']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openHome() {
        open("/");
    }

    public boolean isLogoVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(LOGO));
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
}
