package pl.pepco.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.PageFactory;
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
    private static final By COOKIE_BANNER = By.cssSelector("#onetrust-banner-sdk, #onetrust-group-container");
    private static final By LOADER = By.cssSelector(
            ".loader, .loading, .spinner, [class*='loader'], [class*='loading'], [aria-busy='true']"
    );

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final List<String> capturedDmpRequests = new ArrayList<>();
    private final List<Integer> capturedDmpVisitPixelStatuses = new ArrayList<>();
    private final List<Integer> capturedDmpCheckCookiePixelStatuses = new ArrayList<>();

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open(String path) {
        driver.get(BASE_URL + path);
    }

    public boolean acceptCookies() {
        try {
            return acceptAllCookies();
        } catch (WebDriverException acceptFailed) {
            try {
                clickCookieButton(By.cssSelector(".onetrust-close-btn-handler"));
                return true;
            } catch (WebDriverException closeFailed) {
                return false;
            }
        }
    }

    public boolean acceptAllCookies() {
        try {
            clickCookieButton(By.id("onetrust-accept-btn-handler"));
            return true;
        } catch (TimeoutException ignored) {
            return driver.findElements(COOKIE_BANNER).stream().noneMatch(WebElement::isDisplayed);
        }
    }

    private void clickCookieButton(By selector) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(selector));
        try {
            button.click();
        } catch (WebDriverException clickIntercepted) {
            button.sendKeys(Keys.ENTER);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(COOKIE_BANNER));
    }

    public void waitForTags(int timeoutSeconds) {
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        pageWait.until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
        ));
    }

    public void waitUntilPageSettled(int timeoutSeconds) {
        WebDriverWait pageWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        pageWait.until(webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
        ));
        pageWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        pageWait.until(webDriver -> webDriver.findElements(COOKIE_BANNER).stream().noneMatch(WebElement::isDisplayed));
        pageWait.until(webDriver -> webDriver.findElements(LOADER).stream().noneMatch(WebElement::isDisplayed));
    }

    public boolean waitForDmpRequests(int timeoutSeconds) {
        try {
            WebDriverWait tagWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return tagWait.until(webDriver -> !getDmpRequests().isEmpty());
        } catch (WebDriverException ignored) {
            return false;
        }
    }

    protected void click(WebElement element) {
        scrollIntoView(element);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
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
        processDmpLogs();
        return new ArrayList<>(capturedDmpRequests);
    }

    public List<Integer> getDmpVisitPixelStatuses() {
        processDmpLogs();
        captureDmpStatusesFromPerformanceEntries();
        return new ArrayList<>(capturedDmpVisitPixelStatuses);
    }

    public List<Integer> getDmpCheckCookiePixelStatuses() {
        processDmpLogs();
        captureDmpStatusesFromPerformanceEntries();
        return new ArrayList<>(capturedDmpCheckCookiePixelStatuses);
    }

    private void processDmpLogs() {
        LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);

        for (LogEntry entry : logs) {
            try {
                JsonObject root = JsonParser.parseString(entry.getMessage()).getAsJsonObject();
                JsonObject message = root.getAsJsonObject("message");
                String method = message.get("method").getAsString();
                JsonObject params = message.getAsJsonObject("params");

                if ("Network.requestWillBeSent".equals(method)) {
                    String url = params.getAsJsonObject("request").get("url").getAsString();
                    String decoded = URLDecoder.decode(url, StandardCharsets.UTF_8);
                    if (decoded.contains("mediarithmics.com") && !capturedDmpRequests.contains(decoded)) {
                        capturedDmpRequests.add(decoded);
                    }
                    continue;
                }

                if ("Network.responseReceived".equals(method)) {
                    JsonObject response = params.getAsJsonObject("response");
                    String url = URLDecoder.decode(response.get("url").getAsString(), StandardCharsets.UTF_8);
                    if (url.contains("events.mediarithmics.com/v1/visits/pixel")) {
                        int status = response.get("status").getAsInt();
                        if (!capturedDmpVisitPixelStatuses.contains(status)) {
                            capturedDmpVisitPixelStatuses.add(status);
                        }
                    }
                    if (url.contains("events.mediarithmics.com/v1/check_cookie/pixel")) {
                        int status = response.get("status").getAsInt();
                        if (!capturedDmpCheckCookiePixelStatuses.contains(status)) {
                            capturedDmpCheckCookiePixelStatuses.add(status);
                        }
                    }
                }
            } catch (RuntimeException ignored) {
                // Chrome performance logs contain many event shapes; unrelated entries are skipped.
            }
        }
    }

    public boolean checkPixelFired() {
        return getDmpRequests().stream()
                .anyMatch(request -> request.contains("events.mediarithmics.com/v1/visits/pixel")
                        || request.contains("events.mediarithmics.com/v1/check_cookie/pixel"));
    }

    public boolean checkVisitPixelFired() {
        return getDmpRequests().stream()
                .anyMatch(request -> request.contains("events.mediarithmics.com/v1/visits/pixel"));
    }

    public boolean checkCookiePixelFired() {
        return getDmpRequests().stream()
                .anyMatch(request -> request.contains("events.mediarithmics.com/v1/check_cookie/pixel"));
    }

    public List<String> getDmpSiteTokens() {
        return getDmpRequests().stream()
                .map(this::extractSiteToken)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .toList();
    }

    @SuppressWarnings("unchecked")
    private void captureDmpStatusesFromPerformanceEntries() {
        Object result = ((JavascriptExecutor) driver).executeScript("""
                return performance.getEntriesByType('resource')
                    .filter(entry => entry.name.includes('events.mediarithmics.com/v1/visits/pixel')
                        || entry.name.includes('events.mediarithmics.com/v1/check_cookie/pixel'))
                    .map(entry => ({ name: entry.name, status: entry.responseStatus || 0 }));
                """);

        if (!(result instanceof List<?> entries)) {
            return;
        }

        for (Object entry : entries) {
            if (!(entry instanceof java.util.Map<?, ?> resource)) {
                continue;
            }

            Object nameValue = resource.get("name");
            Object statusValue = resource.get("status");
            if (!(nameValue instanceof String name) || !(statusValue instanceof Number statusNumber)) {
                continue;
            }

            int status = statusNumber.intValue();
            if (status <= 0) {
                continue;
            }

            if (name.contains("events.mediarithmics.com/v1/visits/pixel")
                    && !capturedDmpVisitPixelStatuses.contains(status)) {
                capturedDmpVisitPixelStatuses.add(status);
            }
            if (name.contains("events.mediarithmics.com/v1/check_cookie/pixel")
                    && !capturedDmpCheckCookiePixelStatuses.contains(status)) {
                capturedDmpCheckCookiePixelStatuses.add(status);
            }
        }
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

    private Optional<String> extractSiteToken(String request) {
        String marker = "$site_token=";
        int tokenIndex = request.indexOf(marker);
        if (tokenIndex < 0) {
            return Optional.empty();
        }

        return Optional.of(request.substring(tokenIndex + marker.length()).split("&", 2)[0]);
    }
}
