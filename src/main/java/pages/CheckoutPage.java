package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

/*
 * CheckoutPage handles the entire checkout process.
 *
 * THIS WAS THE HARDEST PAGE TO IMPLEMENT.
 * The Demo Web Shop checkout has 5 steps:
 *   1. Billing address
 *   2. Shipping address (often disabled if "ship to same address" is checked)
 *   3. Shipping method
 *   4. Payment method
 *   5. Payment info
 *   6. Confirm order
 *
 * The tricky part: between each step, there's an AJAX loading overlay
 * that covers the page. If you try to click the next button while
 * the overlay is visible, Selenium throws ElementClickInterceptedException.
 *
 * I created waitForOverlay() and clickSafely() to handle this.
 * clickSafely() first tries normal click, then falls back to JavaScript click.
 */
public class CheckoutPage {

    WebDriver driver;
    WebDriverWait wait;

    // === Billing address fields ===
    @FindBy(id = "BillingNewAddress_FirstName")
    WebElement billingFirstName;

    @FindBy(id = "BillingNewAddress_LastName")
    WebElement billingLastName;

    @FindBy(id = "BillingNewAddress_Email")
    WebElement billingEmail;

    @FindBy(id = "BillingNewAddress_CountryId")
    WebElement billingCountry;  // Dropdown/Select element

    @FindBy(id = "BillingNewAddress_City")
    WebElement billingCity;

    @FindBy(id = "BillingNewAddress_Address1")
    WebElement billingAddress1;

    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    WebElement billingZip;

    @FindBy(id = "BillingNewAddress_PhoneNumber")
    WebElement billingPhone;

    @FindBy(id = "BillingNewAddress_StateProvinceId")
    WebElement billingState;  // Dropdown — may not appear for all countries

    // === Checkout step navigation ===
    @FindBy(id = "ShipToSameAddress")
    WebElement shipToSameAddress;

    @FindBy(css = "input.new-address-next-step-button")
    WebElement billingContinue;

    @FindBy(css = "input.shipping-method-next-step-button")
    WebElement shippingMethodContinue;

    @FindBy(css = "input.payment-method-next-step-button")
    WebElement paymentMethodContinue;

    @FindBy(css = "input.payment-info-next-step-button")
    WebElement paymentInfoContinue;

    @FindBy(css = "input.confirm-order-next-step-button")
    WebElement confirmButton;

    // === Order confirmation ===
    @FindBy(css = ".order-completed .title")
    WebElement orderSuccessMessage;

    @FindBy(css = ".order-completed .order-number")
    WebElement orderNumber;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        // Using 15 second timeout here instead of 10 because
        // checkout is slower (page transitions with AJAX)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    /*
     * Wait for the AJAX loading overlay to disappear.
     * Each checkout step shows a loading overlay during transition.
     * If I don't wait for it to disappear, the next click fails.
     *
     * I catch Exception because sometimes the overlay doesn't appear
     * at all (depends on server speed), and I don't want to fail
     * the test waiting for something that isn't there.
     */
    private void waitForOverlay() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".ajax-loading-block-window")));
        } catch (Exception ignored) {}
    }

    /*
     * clickSafely() tries to click normally first.
     * If that fails (usually because of overlay or other element
     * intercepting the click), it falls back to JavaScript click.
     *
     * I learned this trick after spending 2 hours debugging
     * "ElementClickInterceptedException" errors. JavaScript click
     * forces the click in the browser, bypassing any overlays.
     */
    private void clickSafely(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /*
     * fillBillingAddress() fills all billing fields.
     *
     * I clear() ALL fields first because sometimes the form
     * has pre-filled values (if the user is logged in).
     *
     * The State dropdown only appears for countries that have states
     * (like USA). For "Sri Lanka" it doesn't appear, so I put
     * the state selection in a try-catch to avoid failures.
     */
    public void fillBillingAddress(String firstName, String lastName, String email,
                                    String country, String state, String city,
                                    String address, String zip, String phone) {
        billingFirstName.clear();
        billingFirstName.sendKeys(firstName);
        billingLastName.clear();
        billingLastName.sendKeys(lastName);
        billingEmail.clear();
        billingEmail.sendKeys(email);
        new Select(billingCountry).selectByVisibleText(country);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("BillingNewAddress_StateProvinceId")));
            new Select(billingState).selectByVisibleText(state);
        } catch (Exception ignored) {
            // State dropdown doesn't exist for this country — that's OK
        }
        billingCity.clear();
        billingCity.sendKeys(city);
        billingAddress1.clear();
        billingAddress1.sendKeys(address);
        billingZip.clear();
        billingZip.sendKeys(zip);
        billingPhone.clear();
        billingPhone.sendKeys(phone);
    }

    /*
     * Clicks the billing continue button and handles the shipping
     * address step that may appear next.
     */
    public void clickBillingContinue() {
        ensureShipToSameAddress();
        clickSafely(billingContinue);
        waitForOverlay();
        handleNextStep();
    }

    // Make sure "Ship to same address" is checked before continuing
    private void ensureShipToSameAddress() {
        try {
            if (!shipToSameAddress.isSelected()) {
                clickSafely(shipToSameAddress);
            }
        } catch (Exception ignored) {}
    }

    /*
     * After billing continues, sometimes a Shipping Address step appears.
     * This handles it automatically.
     *
     * I used Thread.sleep(1500) here which is NOT ideal.
     * I tried explicit waits but the shipping step was inconsistent.
     * TODO: replace this sleep with a proper wait when I figure out
     * a reliable condition to check.
     */
    private void handleNextStep() {
        waitForOverlay();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}
        if (isShippingAddressStepVisible()) {
            List<WebElement> btns = driver.findElements(By.cssSelector("input.new-address-next-step-button"));
            for (WebElement btn : btns) {
                if (btn.isDisplayed()) {
                    clickSafely(btn);
                    waitForOverlay();
                    break;
                }
            }
        }
    }

    // Check if the shipping address step is currently visible
    private boolean isShippingAddressStepVisible() {
        return !driver.findElements(By.cssSelector("#checkout-step-shipping .new-address-next-step-button")).isEmpty();
    }

    /*
     * Select a shipping method by index.
     * Index 0 = "Ground" (the cheapest option, which is what I always pick).
     */
    public void selectShippingMethod(int index) {
        waitForOverlay();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#checkout-step-shipping-method .method-list")));
        List<WebElement> options = driver.findElements(By.cssSelector("input[name='shippingoption']"));
        if (index < options.size()) {
            clickSafely(options.get(index));
        }
    }

    public void clickShippingMethodContinue() {
        selectShippingMethod(0);  // Select first shipping option (Ground)
        waitForOverlay();
        clickSafely(shippingMethodContinue);
        waitForOverlay();
    }

    /*
     * Select a payment method by index.
     * Index 0 = "Cash on delivery" (simplest option for tests).
     */
    public void selectPaymentMethod(int index) {
        waitForOverlay();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#checkout-step-payment-method")));
        List<WebElement> methods = driver.findElements(By.cssSelector("input[name='paymentmethod']"));
        if (index < methods.size()) {
            clickSafely(methods.get(index));
        }
    }

    public void clickPaymentMethodContinue() {
        selectPaymentMethod(0);  // Select first payment method (COD)
        waitForOverlay();
        clickSafely(paymentMethodContinue);
        waitForOverlay();
    }

    public void clickPaymentInfoContinue() {
        waitForOverlay();
        clickSafely(paymentInfoContinue);
        waitForOverlay();
    }

    public void confirmOrder() {
        waitForOverlay();
        clickSafely(confirmButton);
    }

    public boolean isOrderCompleted() {
        waitForOverlay();
        return orderSuccessMessage.isDisplayed();
    }
}
