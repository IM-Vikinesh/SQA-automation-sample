package base;

import factory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utilities.ConfigReader;

/*
 * BaseTest is the parent class for ALL test classes in this project.
 *
 * I made this so every test automatically gets:
 * - A browser opened before the test starts (@BeforeMethod)
 * - The browser closed after the test ends (@AfterMethod)
 *
 * @Listeners connects ExtentReports to TestNG so it knows when tests
 * start/pass/fail and can generate the HTML report.
 *
 * Why a parent class? Because writing setup/teardown in every single
 * test class would be super repetitive. Extending this saves me from
 * copying code everywhere.
 */
@Listeners(reports.ExtentListener.class)
public class BaseTest {

    /*
     * protected means child classes (like LoginTests) can access it directly.
     * That's how they get access to the browser.
     */
    protected WebDriver driver;

    /*
     * @BeforeMethod runs BEFORE every @Test method.
     * For 10 tests, this runs 10 times — each test gets a fresh browser.
     * This is important so tests don't affect each other (no leftover cookies).
     */
    @BeforeMethod
    public void setUp() {
        // Read browser name from config.properties (default is "chrome")
        String browser = ConfigReader.getProperty("browser");
        driver = DriverFactory.initializeDriver(browser);
        // Navigate to the website we're testing
        driver.get(ConfigReader.getProperty("url"));
    }

    /*
     * @AfterMethod runs AFTER every @Test method.
     * Quits the browser to free up memory.
     * The null check prevents errors if setUp() failed and driver was never created.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
