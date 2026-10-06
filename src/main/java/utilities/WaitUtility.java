package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/*
 * WaitUtility provides helper methods for explicit waits.
 *
 * Why explicit waits over implicit waits?
 * - Implicit wait (set in DriverFactory) applies to ALL findElement() calls
 * - Explicit wait applies to SPECIFIC elements/conditions
 *
 * I use explicit waits when I need to wait for something specific
 * before interacting with it, like waiting for a button to be clickable.
 *
 * Don't mix implicit and explicit waits with Thread.sleep() —
 * sleep() always waits the full duration, while WebDriverWait returns
 * as soon as the condition is met.
 */
public class WaitUtility {

    private WebDriverWait wait;

    // Reads timeout from config.properties (default is 20 seconds)
    public WaitUtility(WebDriver driver) {
        long timeout = Long.parseLong(ConfigReader.getProperty("timeout"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    // Wait until an element is visible on the page
    public void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait until an element is clickable (visible + enabled)
    public void waitForClickability(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait for the page to finish loading (document.readyState == "complete")
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
}
