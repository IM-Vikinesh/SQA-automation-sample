package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;

/*
 * ProductTests — tests for searching and browsing products.
 *
 * Three scenarios:
 * 1. Search for a product that exists ("computer") — expect results
 * 2. Search for gibberish — expect NO results
 * 3. Browse a category and click a product — expect detail page
 *
 * I used driver.get() to navigate directly in test 3 instead of
 * clicking through the UI. It's faster and more reliable.
 */
public class ProductTests extends BaseTest {

    /*
     * Search for "computer" — a product that definitely exists
     * on Demo Web Shop. Verify that at least one product is shown.
     */
    @Test
    public void searchExistingProductTest() {
        HomePage homePage = new HomePage(driver);
        homePage.search("computer");

        ProductPage productPage = new ProductPage(driver);
        Assert.assertTrue(productPage.isProductListDisplayed(),
                "Products should be displayed when searching for existing product");
    }

    /*
     * Search for something that doesn't exist.
     * The site should show zero products.
     *
     * isProductListDisplayed() returns false when the product
     * items list is empty — I test the NEGATIVE case here.
     */
    @Test
    public void searchInvalidProductTest() {
        HomePage homePage = new HomePage(driver);
        homePage.search("xyz123invalid");

        ProductPage productPage = new ProductPage(driver);
        Assert.assertFalse(productPage.isProductListDisplayed(),
                "No products should be displayed for invalid product search");
    }

    /*
     * Navigate to the Books category, click the first product,
     * and verify we end up on a product detail page.
     *
     * I use driver.get() with the direct URL instead of clicking
     * through the menu. This is a common shortcut in test automation
     * when the navigation isn't what we're testing.
     */
    @Test
    public void viewProductDetailsTest() {
        driver.get("https://demowebshop.tricentis.com/books");

        ProductPage productPage = new ProductPage(driver);
        productPage.openProduct(0);  // Click the first product in the list

        Assert.assertTrue(productPage.isOnProductDetailPage(),
                "Should navigate to product detail page after clicking view product");
    }
}
