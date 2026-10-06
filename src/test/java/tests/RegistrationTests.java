package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegisterPage;
import utilities.RandomDataUtility;

/*
 * RegistrationTests — tests for user registration on Demo Web Shop.
 *
 * Two test cases:
 * 1. validRegistrationTest: register with valid random data, expect success
 * 2. existingEmailRegistrationTest: register once, try again with same email, expect error
 *
 * Both tests extend BaseTest, which handles opening/closing the browser.
 * Each @Test method runs in its own browser session (thanks to @BeforeMethod/@AfterMethod).
 */
public class RegistrationTests extends BaseTest {

    /*
     * Register a new user with random data and verify success.
     *
     * Steps:
     * 1. Go to home page, click Register link
     * 2. Fill the registration form with random name, email, and password
     * 3. Submit the form
     * 4. Check that the success message "Your registration completed" appears
     */
    @Test
    public void validRegistrationTest() {
        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register(
                RandomDataUtility.generateName(),
                "User",
                RandomDataUtility.generateEmail(),
                "testpass123"
        );

        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration completed message should be displayed");
    }

    /*
     * Try registering with an email that's already taken.
     *
     * The website should show an error because the email already exists
     * in the system. This tests the validation logic on the server side.
     *
     * Steps:
     * 1. Register a user with a specific email
     * 2. Click "Continue" to go back to home page
     * 3. Log out
     * 4. Go to Register page again
     * 5. Try registering with the SAME email
     * 6. Assert that the error message is displayed
     */
    @Test
    public void existingEmailRegistrationTest() {
        // Generate email ONCE and reuse it for both registrations
        String email = RandomDataUtility.generateEmail();

        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("Test", "User", email, "testpass123");
        registerPage.clickContinue();

        // Log out so we can try registering again
        homePage.logout();

        // Try registering with the same email
        homePage.clickRegister();
        registerPage.register("Test", "User", email, "testpass123");

        Assert.assertTrue(registerPage.isErrorDisplayed(),
                "Error message should be displayed for existing email registration");
    }
}
