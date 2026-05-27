package pl.pepco.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import pl.pepco.pages.PepcoClubPage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PepcoClubTest extends BaseTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("pl.pepco.testdata.PepcoClubData#appStoreLinks")
    void appStorePageOpensAfterClick(String name, By selector, String expectedUrlPart, String screenshotName) {
        PepcoClubPage page = new PepcoClubPage(driver);
        page.openClub();
        page.acceptCookies();
        page.waitForTags(3);

        assertTrue(driver.getCurrentUrl().contains("pepco-club"), "[" + name + "] Nie otwarto strony Pepco Club");
        assertTrue(page.isTitleVisible(), "[" + name + "] Brak tytulu strony Pepco Club");
        assertTrue(page.clickLink(selector), "[" + name + "] Nie udalo sie kliknac linku sklepu");

        switchToNewestWindow();
        page.waitForTags(10);

        assertTrue(driver.getCurrentUrl().contains(expectedUrlPart), "[" + name + "] Niepoprawny URL sklepu");
        takeScreenshot(screenshotName);
    }

    private void switchToNewestWindow() {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(handles.size() - 1));
    }
}
