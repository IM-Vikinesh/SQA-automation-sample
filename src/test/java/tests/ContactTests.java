package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactUsPage;
import utilities.RandomDataUtility;

/*
 * ContactTests — tests for the Contact Us form.
 *
 * This is the simplest test class in the project.
 * Just fill the form and check for the success message.
 *
 * The Contact Us form at /contactus requires:
 * - Full name
 * - Email
 * - Enquiry message
 */
public class ContactTests extends BaseTest {

    /*
     * Submit a contact enquiry with random data.
     * Verify that the success message appears after submission.
     */
    @Test
    public void submitContactFormTest() {
        // Navigate directly to the contact page
        driver.get("https://demowebshop.tricentis.com/contactus");

        ContactUsPage contactUsPage = new ContactUsPage(driver);
        contactUsPage.submitEnquiry(
                RandomDataUtility.generateName(),
                RandomDataUtility.generateEmail(),
                "This is a test enquiry."
        );

        Assert.assertTrue(contactUsPage.isEnquirySent(),
                "Success message should be displayed after submitting contact form");
    }
}
