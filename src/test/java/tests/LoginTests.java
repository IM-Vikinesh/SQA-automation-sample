package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import utilities.RandomDataUtility;

/*
 * LoginTests — tests for the login functionality.
 *
 * Three scenarios:
 * 1. Valid login (register first, then log in with the same credentials)
 * 2. Invalid login (wrong email + password combo)
 * 3. Empty credentials (leave both fields blank)
 *
 * For the valid login test, I need to register a user first because
 * I don't have a pre-existing test account. The registration + login
 * flow tests both features together.
 */
public class LoginTests extends BaseTest {

    /*
     * Register a user, log out, then log back in with the same credentials.
     *
     * This is the "happy path" — everything should work smoothly.
     * I check two things:
     * 1. The account link is visible (meaning I'm logged in)
     * 2. The email shown on the account link matches what I used to log in
     */
    @Test
    public void validLoginTest() {
        String email = RandomDataUtility.generateEmail();
        String password = "testpass123";

        // Step 1: Register a new user
        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("Test", "User", email, password);
        registerPage.clickContinue();

        // Step 2: Log out
        homePage.logout();

        // Step 3: Log in with the same credentials
        homePage.clickLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        // Step 4: Verify login was successful
        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after valid login");
        Assert.assertEquals(homePage.getLoggedInEmail(), email,
                "Logged in email should match");
    }

    /*
     * Try logging in with credentials that don't exist in the system.
     * The site should show an error message.
     *
     * I don't need to register first — I just go directly to the login
     * page and submit a random email + password.
     */
    @Test
    public void invalidLoginTest() {
        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("invalid@test.com", "wrongpass");

        Assert.assertTrue(loginPage.verifyLoginError(),
                "Error message should be displayed for invalid login");
    }

    /*
     * Submit the login form with both fields empty.
     *
     * The site should NOT let me through with blank credentials.
     * I check that the URL still contains "login" (we didn't redirect).
     *
     * This is a basic form validation test.
     */
    @Test
    public void emptyCredentialsTest() {
        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "");

        boolean remainedOnLogin = driver.getCurrentUrl().contains("login");
        Assert.assertTrue(remainedOnLogin,
                "User should remain on login page with empty credentials");
    }
}
