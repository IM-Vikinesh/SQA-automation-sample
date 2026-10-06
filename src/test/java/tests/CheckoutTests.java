package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.ProductPage;
import pages.RegisterPage;
import utilities.RandomDataUtility;

/*
 * CheckoutTests — tests for the full checkout flow.
 *
 * The successfulCheckoutTest is the MOST COMPLEX test in this project.
 * It simulates a real customer journey:
 *   Register -> Login -> Browse -> Add to cart -> Checkout -> Confirm
 *
 * The checkoutEmptyCartTest is a simple edge case.
 *
 * I chose "Sri Lanka" as the country because Demo Web Shop supports it
 * and it doesn't require selecting a state (simplifies the test).
 */
public class CheckoutTests extends BaseTest {

    /*
     * Full end-to-end checkout test.
     *
     * This test covers the ENTIRE purchase flow from registration
     * to order confirmation. It's long but covers a critical path.
     *
     * Steps:
     * 1. Register a new user (need an account to checkout)
     * 2. Browse to Books category
     * 3. Open a product and click "Add to cart"
     * 4. Go to cart, accept terms, click checkout
     * 5. Fill billing address (Sri Lanka to avoid state dropdown)
     * 6. Continue through billing -> shipping method -> payment method -> payment info
     * 7. Confirm the order
     * 8. Verify the "Your order has been successfully processed" message
     */
    @Test
    public void successfulCheckoutTest() {
        String email = RandomDataUtility.generateEmail();
        String name = RandomDataUtility.generateName();

        // Step 1: Register
        HomePage homePage = new HomePage(driver);
        homePage.clickRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.register(name, "User", email, "testpass123");
        registerPage.clickContinue();

        // Step 2: Add product to cart
        driver.get("https://demowebshop.tricentis.com/books");

        ProductPage productPage = new ProductPage(driver);
        productPage.openProduct(0);
        productPage.addToCartFromDetails();

        // Step 3: Go to cart and proceed to checkout
        driver.get("https://demowebshop.tricentis.com/cart");
        CartPage cartPage = new CartPage(driver);
        cartPage.acceptTerms();
        cartPage.clickCheckout();

        // Step 4: Fill checkout steps
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Billing address — I use Sri Lanka because it doesn't have states,
        // which simplifies the billing form (state dropdown is skipped)
        checkoutPage.fillBillingAddress(
                name, "User", email,
                "Sri Lanka", "Eastern", "Batticaloa", "123 Main St",
                "10001", "1234567890"
        );
        checkoutPage.clickBillingContinue();
        checkoutPage.clickShippingMethodContinue();
        checkoutPage.clickPaymentMethodContinue();
        checkoutPage.clickPaymentInfoContinue();
        checkoutPage.confirmOrder();

        // Step 5: Verify order completed
        Assert.assertTrue(checkoutPage.isOrderCompleted(),
                "Order confirmation message should be displayed after successful checkout");
    }

    /*
     * Edge case: go to cart when it's empty.
     * The site should show "Your Shopping Cart is empty!" message.
     *
     * This is a simple test but it checks that the cart page
     * handles the empty state correctly.
     */
    @Test
    public void checkoutEmptyCartTest() {
        driver.get("https://demowebshop.tricentis.com/cart");
        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Empty cart message should be displayed when cart is empty");
    }
}
