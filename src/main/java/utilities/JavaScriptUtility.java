package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * JavaScriptUtility uses JavaScriptExecutor to perform actions
 * that normal Selenium WebDriver can't handle.
 *
 * Why I needed this:
 * 1. Some buttons are covered by overlays (popups, banners, loading screens)
 *    — normal .click() throws ElementClickInterceptedException
 * 2. Some elements are outside the visible viewport — need to scroll first
 *
 * JavaScript click bypasses overlay issues by directly executing
 * the click event in the browser DOM.
 *
 * Casting WebDriver to JavascriptExecutor works because all browser
 * drivers (ChromeDriver, EdgeDriver) implement this interface.
 */
public class JavaScriptUtility {

    private JavascriptExecutor js;

    public JavaScriptUtility(WebDriver driver) {
        this.js = (JavascriptExecutor) driver;
    }

    // Force-click an element using JavaScript (bypasses overlays)
    public void clickElement(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // Scroll page so element is visible (useful for elements below the fold)
    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
