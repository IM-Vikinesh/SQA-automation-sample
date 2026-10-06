package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/*
 * CartPage represents the shopping cart page (/cart).
 *
 * This was one of the simpler pages to implement. The cart has:
 * - Rows for each product (with checkbox, quantity input, etc.)
 * - Update cart button
 * - Terms of service checkbox (required before checkout)
 * - Checkout button
 *
 * NOTE: List<WebElement> fields are CACHED with PageFactory.
 * If the cart contents change after interacting (e.g., removing an item),
 * the list won't update automatically. I call driver.findElements()
 * directly in isCartEmpty() instead of using the cached list for that reason.
 */
public class CartPage {

    WebDriver driver;

    @FindBy(css = ".cart-item-row")
    List<WebElement> cartItems;          // Each product row in the cart

    @FindBy(css = "input[name='removefromcart']")
    List<WebElement> removeCheckboxes;   // Checkboxes to select items for removal

    @FindBy(css = ".qty-input")
    List<WebElement> quantityInputs;     // Quantity text fields

    @FindBy(css = ".update-cart-button")
    WebElement updateCartButton;         // Updates cart after changes

    @FindBy(id = "termsofservice")
    WebElement termsCheckbox;            // Must be checked before checkout

    @FindBy(id = "checkout")
    WebElement checkoutButton;           // Proceed to checkout

    @FindBy(css = ".order-summary-content")
    WebElement orderSummaryContent;      // Shows "Your Shopping Cart is empty!" when empty

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Returns the number of items currently in the cart
    public int getCartItemCount() {
        return cartItems.size();
    }

    /*
     * isCartEmpty() checks if the order summary text says the cart is empty.
     * I use getText().contains() instead of equals() because the text
     * might have extra whitespace or characters.
     *
     * I used to check cartItems.size() == 0, but the list was cached
     * from PageFactory and didn't update after removal. Using the
     * summary text was more reliable.
     */
    public boolean isCartEmpty() {
        return orderSummaryContent.getText().contains("Your Shopping Cart is empty!");
    }

    // Check a product's remove checkbox and click "Update cart"
    public void removeProduct(int index) {
        if (index < removeCheckboxes.size()) {
            removeCheckboxes.get(index).click();
            updateCartButton.click();  // Cart refreshes after this
        }
    }

    // Change the quantity of a product and update the cart
    public void updateQuantity(int index, String quantity) {
        if (index < quantityInputs.size()) {
            quantityInputs.get(index).clear();
            quantityInputs.get(index).sendKeys(quantity);
            updateCartButton.click();
        }
    }

    // Accept terms of service (required before checkout)
    public void acceptTerms() {
        termsCheckbox.click();
    }

    // Proceed to checkout
    public void clickCheckout() {
        checkoutButton.click();
    }
}
