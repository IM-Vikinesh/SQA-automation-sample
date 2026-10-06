package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/*
 * ContactUsPage handles the /contactus form.
 *
 * This is the simplest page in the project — just name, email,
 * enquiry text, and a submit button. The success message uses
 * the same ".result" class as the registration page.
 *
 * I noticed that Demo Web Shop reuses CSS classes across pages
 * (like ".result" for success messages), which makes locators
 * consistent but also means I need to be careful I'm on the
 * right page when using these elements.
 */
public class ContactUsPage {

    WebDriver driver;

    @FindBy(id = "FullName")
    WebElement nameInput;

    @FindBy(id = "Email")
    WebElement emailInput;

    @FindBy(id = "Enquiry")
    WebElement enquiryInput;

    @FindBy(css = ".contact-us-button")
    WebElement submitButton;

    // Success message shown after submitting ("Your enquiry has been sent")
    @FindBy(css = ".result")
    WebElement successMessage;

    public ContactUsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Fill the form and submit
    public void submitEnquiry(String name, String email, String enquiry) {
        nameInput.sendKeys(name);
        emailInput.sendKeys(email);
        enquiryInput.sendKeys(enquiry);
        submitButton.click();
    }

    // Check if the success message appeared
    public boolean isEnquirySent() {
        return successMessage.isDisplayed();
    }
}
