package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/*
 * HomePage represents the main page of Demo Web Shop.
 *
 * Each page of the website gets its own class (Page Object Model).
 * This keeps things organized — if the login link changes from
 * linkText "Log in" to something else, I only fix it here.
 */
public class HomePage {

    WebDriver driver;

    // === Top navigation links ===
    @FindBy(linkText = "Log in")
    WebElement loginLink;

    @FindBy(linkText = "Register")
    WebElement registerLink;

    @FindBy(css = "a.ico-logout")
    WebElement logoutLink;

    // Shows the logged-in user's email (only visible when logged in)
    @FindBy(css = "a.account")
    WebElement accountLink;

    // === Search bar ===
    @FindBy(css = ".search-box-text")
    WebElement searchInput;

    @FindBy(css = ".search-box-button")
    WebElement searchButton;

    /*
     * Constructor: takes the WebDriver and initializes all @FindBy elements.
     *
     * PageFactory.initElements() uses lazy initialization — it doesn't
     * find elements right away. It creates proxies that find the element
     * only when you interact with them (click, sendKeys, getText).
     * This means elements are always fresh, avoiding stale element exceptions.
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // === Navigation methods ===

    public void clickLogin() {
        loginLink.click();
    }

    public void clickRegister() {
        registerLink.click();
    }

    public void logout() {
        logoutLink.click();
    }

    /*
     * isLoggedIn() checks if the account link is visible.
     * The account link only shows up after successful login,
     * so this is my way of checking login state.
     *
     * isDisplayed() returns true if the element exists and is visible.
     * It throws NoSuchElementException if not found — but since I
     * have implicit wait set to 10 seconds, Selenium will wait
     * and retry before throwing.
     */
    public boolean isLoggedIn() {
        return accountLink.isDisplayed();
    }

    // Returns the email displayed on the account link
    public String getLoggedInEmail() {
        return accountLink.getText();
    }

    /*
     * Types a search query and clicks the search button.
     * clear() first because the search box sometimes has placeholder text.
     */
    public void search(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
        searchButton.click();
    }
}
