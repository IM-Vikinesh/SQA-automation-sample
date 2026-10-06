package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegisterPage;
import utilities.RandomDataUtility;

/*
 * AccountTests — verifies that account information is correct
 * after registration/login.
 *
 * After registering, Demo Web Shop automatically logs you in.
 * The top of the page shows your email in an "account" link.
 * This test checks that the displayed email matches what you registered with.
 *
 * This is important because if the site showed the wrong email,
 * it would be a serious bug (security/privacy issue).
 */
public class AccountTests extends BaseTest {

    /*
     * Register a user and verify the account email is correct.
     *
     * Steps:
     * 1. Register with a random email
     * 2. After registration, we're automatically logged in
     * 3. Check that the account link shows "Logged in" status
     * 4. Check that the email in the account link matches the registered email
     */
    @Test
    public void myAccountPageTest() {
        String email = RandomDataUtility.generateEmail();

        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register("Test", "User", email, "testpass123");
        registerPage.clickContinue();

        // After registration, user should be logged in automatically
        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after registration");
        // The email shown in the account link should match what we registered with
        Assert.assertEquals(homePage.getLoggedInEmail(), email,
                "Account email should match registered email");
    }
}
