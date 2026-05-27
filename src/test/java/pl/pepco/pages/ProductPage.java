package pl.pepco.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {
    private static final By RETURN_INFO_LINK = By.xpath(
            "(//*[contains(normalize-space(.), '30 dni na zwrot')]/ancestor-or-self::a)[1]"
    );

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean clickReturnInfo() {
        try {
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(RETURN_INFO_LINK));
            click(link);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public boolean openReturnRules(String expectedUrlPart) {
        try {
            if (driver.getCurrentUrl().contains(expectedUrlPart)) {
                return true;
            }
            By returnRulesLink = By.xpath("(//a[contains(@href, '" + expectedUrlPart + "')])[1]");
            if (expectedUrlPart.contains("pepco-privacy")) {
                returnRulesLink = By.xpath("(//a[contains(normalize-space(.), 'Regulamin')])[1]");
            }
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(returnRulesLink));
            click(link);
            wait.until(ExpectedConditions.urlContains(expectedUrlPart));
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }
}
