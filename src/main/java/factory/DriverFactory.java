package factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.time.Duration;

/*
 * DriverFactory handles ALL browser setup in one place.
 *
 * I used a factory pattern here — instead of writing browser setup code
 * in every test class, I just call DriverFactory.initializeDriver("chrome").
 *
 * WebDriverManager auto-downloads the correct chromedriver/edgedriver
 * that matches your installed browser version. Before I found this library,
 * I had to manually download drivers and update paths — it was painful.
 */
public class DriverFactory {

    /*
     * Static because we only need ONE driver for the whole test session.
     * If I created multiple drivers, multiple browser windows would open.
     */
    private static WebDriver driver;

    public static WebDriver initializeDriver(String browser) {
        // Chrome setup
        if (browser.equalsIgnoreCase("chrome")) {
            // Check if user specified a custom chromedriver path in config
            // (I have a manual path on my machine, but WebDriverManager is the fallback)
            String chromePath = utilities.ConfigReader.getProperty("chromedriver.path");
            if (chromePath != null && !chromePath.isEmpty()) {
                System.setProperty("webdriver.chrome.driver", chromePath);
            } else {
                // Auto-download and setup — this is the easier way
                WebDriverManager.chromedriver().setup();
            }
            driver = new ChromeDriver();
        }
        // Edge setup
        else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        // Common settings for both browsers:
        driver.manage().window().maximize();
        /*
         * Implicit wait: tells Selenium "wait up to 10 seconds before saying
         * element not found". This is a global setting that applies to all
         * driver.findElement() calls.
         *
         * I also use explicit waits (WebDriverWait) in some places for more
         * precise timing — see WaitUtility.java.
         */
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    /*
     * Static getter so other classes can access the driver without
     * having to pass it around. Useful in utility classes.
     */
    public static WebDriver getDriver() {
        return driver;
    }
}
