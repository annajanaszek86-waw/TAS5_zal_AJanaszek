package pl.pepco.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasePage {
    protected static final String BASE_URL = "https://pepco.pl";

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String path) {
        driver.get(BASE_URL + path);
    }

    public boolean acceptCookies() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            waitForTags(1);
            try {
                button.click();
            } catch (WebDriverException ignored) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }
            waitForTags(2);
            return true;
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    public void waitForTags(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Path takeScreenshot(String name) {
        try {
            Files.createDirectories(Path.of("screenshots"));
            Path path = Path.of("screenshots", name + ".png");
            byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            Files.write(path, screenshot);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Could not save screenshot: " + name, e);
        }
    }

    public List<String> getDmpRequests() {
        List<String> requests = new ArrayList<>();
        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);

        for (LogEntry entry : logs) {
            try {
                JsonObject root = JsonParser.parseString(entry.getMessage()).getAsJsonObject();
                JsonObject message = root.getAsJsonObject("message");
                if (!"Network.requestWillBeSent".equals(message.get("method").getAsString())) {
                    continue;
                }
                String url = message.getAsJsonObject("params").getAsJsonObject("request").get("url").getAsString();
                String decoded = URLDecoder.decode(url, StandardCharsets.UTF_8);
                if (decoded.contains("mediarithmics.com")) {
                    requests.add(decoded);
                }
            } catch (RuntimeException ignored) {
                // Chrome performance logs contain many event shapes; unrelated entries are skipped.
            }
        }

        return requests;
    }

    public boolean checkPixelFired() {
        return getDmpRequests().stream()
                .anyMatch(request -> request.contains("events.mediarithmics.com/v1/visits/pixel"));
    }

    public Optional<String> getDmpPagePath() {
        return getDmpRequests().stream()
                .filter(request -> request.contains("events.mediarithmics.com/v1/visits/pixel"))
                .map(this::extractPagePath)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<String> extractPagePath(String request) {
        int pageIndex = request.indexOf("page=");
        if (pageIndex < 0) {
            return Optional.empty();
        }

        String value = request.substring(pageIndex + 5).split("&", 2)[0];
        if (value.startsWith("jso-")) {
            value = value.substring(4);
        }

        try {
            JsonObject page = JsonParser.parseString(value).getAsJsonObject();
            return Optional.ofNullable(page.get("page_path")).map(element -> element.getAsString());
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }
}
