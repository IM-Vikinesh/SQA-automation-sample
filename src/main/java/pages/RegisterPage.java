package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/*
 * RegisterPage handles the user registration form.
 *
 * The Demo Web Shop has a standard registration form with:
 * gender, first name, last name, email, password, confirm password.
 *
 * I always use gender-male in tests for simplicity — this website
 * requires you to pick a gender before submitting.
 */
public class RegisterPage {

    WebDriver driver;

    @FindBy(id = "gender-male")
    WebElement genderMale;

    @FindBy(id = "FirstName")
    WebElement firstNameInput;

    @FindBy(id = "LastName")
    WebElement lastNameInput;

    @FindBy(id = "Email")
    WebElement emailInput;

    @FindBy(id = "Password")
    WebElement passwordInput;

    @FindBy(id = "ConfirmPassword")
    WebElement confirmPasswordInput;

    @FindBy(id = "register-button")
    WebElement registerButton;

    // Shows "Your registration completed" on success
    @FindBy(css = ".result")
    WebElement successMessage;

    // Button that appears after successful registration
    @FindBy(css = ".register-continue-button")
    WebElement continueButton;

    // Shows error if you try to register with an existing email
    @FindBy(css = ".message-error")
    WebElement errorMessage;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /*
     * register() fills the entire registration form and submits it.
     *
     * I made the password the same for confirm password to avoid
     * a "passwords don't match" error. This was something I learned
     * the hard way when I first wrote the tests!
     */
    public void register(String firstName, String lastName, String email, String password) {
        genderMale.click();
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        confirmPasswordInput.sendKeys(password);
        registerButton.click();
    }

    // Check if the success message appeared after registration
    public boolean isRegistrationSuccessful() {
        return successMessage.isDisplayed();
    }

    // Click "Continue" button after successful registration (goes to home page)
    public void clickContinue() {
        continueButton.click();
    }

    // Check if error message appeared (used for duplicate email test)
    public boolean isErrorDisplayed() {
        return errorMessage.isDisplayed();
    }
}
