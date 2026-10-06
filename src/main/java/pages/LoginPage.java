package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/*
 * LoginPage handles the login form: /login page on Demo Web Shop.
 *
 * I used @FindBy with CSS selectors and IDs instead of XPath
 * because IDs are the fastest locator and CSS is cleaner to read.
 */
public class LoginPage {

    WebDriver driver;

    @FindBy(id = "Email")
    WebElement emailInput;

    @FindBy(id = "Password")
    WebElement passwordInput;

    // The login button has class "login-button" — used CSS selector
    @FindBy(css = ".login-button")
    WebElement loginButton;

    // Error message that shows up on invalid login
    @FindBy(css = ".message-error")
    WebElement loginError;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /*
     * login() types the email and password, then clicks the login button.
     *
     * Note: I don't clear() the fields here because the login page
     * loads with empty fields. For registration I do clear some fields
     * because they might have pre-filled values.
     */
    public void login(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    /*
     * Returns true if the error message is displayed.
     * Used in invalid login tests to verify the site rejects bad credentials.
     */
    public boolean verifyLoginError() {
        return loginError.isDisplayed();
    }
}
