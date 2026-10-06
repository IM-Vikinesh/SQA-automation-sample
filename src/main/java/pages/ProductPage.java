package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/*
 * ProductPage handles product listing pages and product detail pages.
 *
 * One thing that confused me at first: when you search or browse a category,
 * you get a list of products. Clicking a product takes you to its detail page.
 * I handle both the listing and detail views in this single class.
 *
 * NOTE: List<WebElement> with PageFactory is cached (found once).
 * If the product list changes dynamically (AJAX), the cached list
 * might become stale. I use findElements() directly in those cases.
 */
public class ProductPage {

    WebDriver driver;

    // === Listing page elements ===
    @FindBy(css = ".product-item")
    List<WebElement> productItems;       // Each product in the grid

    @FindBy(css = ".product-title a")
    List<WebElement> productTitleLinks;  // Clickable product names

    @FindBy(css = ".product-box-add-to-cart-button")
    List<WebElement> addToCartButtons;   // "Add to cart" buttons on listing

    // === Detail page elements ===
    // This button appears on the individual product page
    @FindBy(css = "input.add-to-cart-button")
    WebElement addToCartDetailButton;

    @FindBy(css = ".product-name h1")
    WebElement productName;

    @FindBy(css = ".product-grid")
    WebElement productGrid;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Returns true if there are products in the search/browse results
    public boolean isProductListDisplayed() {
        return !productItems.isEmpty();
    }

    // How many products were found
    public int getProductCount() {
        return productItems.size();
    }

    // Click on a product title to go to its detail page
    public void openProduct(int index) {
        if (index < productTitleLinks.size()) {
            productTitleLinks.get(index).click();
        }
    }

    // Click "Add to cart" on a product from the listing grid
    public void addProductToCartFromListing(int index) {
        if (index < addToCartButtons.size()) {
            addToCartButtons.get(index).click();
        }
    }

    /*
     * Click "Add to cart" on the product detail page.
     * After adding, a notification bar appears at the top.
     * I wait for it to appear so I know the item was actually added.
     *
     * This explicit wait was added after I noticed the test sometimes
     * clicked "Add to cart" but went to the cart page too fast
     * before the item was actually added.
     */
    public void addToCartFromDetails() {
        addToCartDetailButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#bar-notification .content")));
    }

    // Get the product name from the detail page
    public String getProductName() {
        return productName.getText();
    }

    /*
     * Checks if we're on a product detail page.
     * I use driver.findElements() (returns empty list if not found)
     * instead of driver.findElement() (throws exception).
     * This way I don't need try-catch.
     */
    public boolean isOnProductDetailPage() {
        return driver.findElements(By.cssSelector(".product-details-page")).size() > 0;
    }
}
