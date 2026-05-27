package pl.pepco.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;

import java.nio.file.Path;
import java.util.Map;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (useIncognito()) {
            options.addArguments("--incognito");
        }
        options.addArguments("--start-maximized");
        options.setCapability("goog:loggingPrefs", Map.of(LogType.PERFORMANCE, "ALL"));
        options.setExperimentalOption("perfLoggingPrefs", Map.of("enableNetwork", true));
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected Path takeScreenshot(String name) {
        return new pl.pepco.pages.BasePage(driver).takeScreenshot(name);
    }

    protected boolean useIncognito() {
        return true;
    }
}
