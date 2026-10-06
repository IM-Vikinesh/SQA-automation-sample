# SQA Automation Project BAT/IT/2324/F/058

> I built this project to learn Selenium WebDriver and test automation.
> It tests a real e-commerce website (Demo Web Shop) automatically.

## What this project does

This is a test automation framework that opens a browser, fills in forms, clicks buttons, and checks that everything works correctly — just like a manual tester would do, but automated and repeatable.

**What it can test:**
- Register new users on the website
- Log in with correct and incorrect passwords
- Search for products
- Add items to the shopping cart
- Change quantities and remove items
- Go through the full checkout process (billing → shipping → payment → confirm)
- Submit the contact us form
- Check account details after registration

## Tools I used

| Tool | What it does (in my own words) |
|------|--------------------------------|
| **Java 17** | The programming language. I already knew some Java from college, so this was comfortable. |
| **Maven** | Build tool that downloads libraries automatically. You just list what you need in pom.xml and it handles the rest. |
| **Selenium WebDriver 4.43.0** | The main library. It lets Java control a browser — open URLs, click things, type text, read page content. |
| **TestNG 7.10.2** | Test framework. Lets me write `@Test` methods, run them together, and assert results. Has better features than JUnit for automation (like listeners and XML suites). |
| **WebDriverManager 5.9.2** | Automatically downloads ChromeDriver/EdgeDriver. Before this, you had to manually download the driver file that matches your browser version — super annoying. |
| **ExtentReports 5.1.2** | Generates a nice HTML report after tests run. Shows which tests passed/failed with timestamps. |
| **Log4j 2.24.3** | Prints log messages to the console during test runs. Helps debug what's happening. |
| **Page Object Model** | A design pattern where each web page gets its own Java class. Keeps test code clean and reusable. |

## Project structure

I organized the code so that similar things are grouped together.

```
SqaAutomation/
├── pom.xml                         # Maven file — lists all libraries (dependencies)
├── .gitignore                      # Tells Git which files to ignore
├── README.md                       # This file
│
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── base/
    │   │   │   └── BaseTest.java        # Parent class: opens browser before test, closes it after
    │   │   │
    │   │   ├── factory/
    │   │   │   └── DriverFactory.java   # Creates Chrome or Edge browser driver
    │   │   │
    │   │   ├── pages/
    │   │   │   ├── HomePage.java        # Homepage: login link, register link, search box
    │   │   │   ├── LoginPage.java       # Login form with email/password
    │   │   │   ├── RegisterPage.java    # Registration form
    │   │   │   ├── ProductPage.java     # Product listing and details
    │   │   │   ├── CartPage.java        # Shopping cart page
    │   │   │   ├── CheckoutPage.java    # Checkout flow (billing, shipping, payment)
    │   │   │   └── ContactUsPage.java   # Contact form
    │   │   │
    │   │   ├── reports/
    │   │   │   ├── ExtentManager.java   # Sets up the HTML report
    │   │   │   └── ExtentListener.java  # Listens to test events (pass/fail) and updates the report
    │   │   │
    │   │   └── utilities/
    │   │       ├── ConfigReader.java    # Reads settings from config.properties
    │   │       ├── WaitUtility.java     # Helper for waiting (elements to load, etc.)
    │   │       ├── JavaScriptUtility.java # JS click/scroll when normal click doesn't work
    │   │       └── RandomDataUtility.java # Generates random emails/names so tests don't conflict
    │   │
    │   └── resources/
    │       ├── config.properties        # Settings: URL, browser choice, timeout
    │       └── log4j2.xml               # Log4j configuration
    │
    └── test/
        ├── java/
        │   └── tests/
        │       ├── RegistrationTests.java   # Tests for user registration
        │       ├── LoginTests.java          # Tests for login (valid, invalid, empty)
        │       ├── ProductTests.java        # Tests for search and product browsing
        │       ├── CartTests.java           # Tests for cart operations
        │       ├── CheckoutTests.java       # Tests for checkout process
        │       ├── ContactTests.java        # Tests for contact form
        │       └── AccountTests.java        # Tests for account verification
        │
        └── resources/
            └── testng.xml                  # TestNG suite config — defines test order
```

## How this project is organized (in more detail)

### Page Object Model (POM) — why I used it

I put each web page into its own Java class. For example, `LoginPage.java` holds:
- The email input field, password field, and login button (using `@FindBy` annotations)
- A `login()` method that types email, types password, and clicks login

**Why this helped me:**
- If the website changes a button ID, I only update one file, not 10 tests
- Tests are easy to read: `loginPage.login("a@b.com", "pass123")` is clear
- I can reuse the same page in multiple tests without copying code

### BaseTest — saves me from repeating code

Every test needs to:
1. Open a browser before the test starts
2. Close the browser after the test ends

I put this setup/teardown in `BaseTest.java` using TestNG's `@BeforeMethod` and `@AfterMethod`. All my test classes `extend BaseTest`, so they automatically get a browser opened and closed without me writing that code every time.

### DriverFactory — browser setup in one place

`DriverFactory.java` creates the browser driver. It checks `config.properties` to see if I want Chrome or Edge. It also handles:
- Setting up WebDriverManager (which auto-downloads the driver)
- Maximizing the window
- Setting an implicit wait (10 seconds — waits for elements to appear)

I made it a static factory so I can call `DriverFactory.getDriver()` from anywhere.

### Utilities — helpers I created

- **ConfigReader.java**: Reads `config.properties`. I put settings like the website URL and browser name here so I can change them without editing code.
- **WaitUtility.java**: Used for explicit waits — waiting for specific elements to be visible or clickable. The web is slow, so this was essential.
- **JavaScriptUtility.java**: Some buttons are covered by popups or banners, and normal Selenium clicks fail. JavaScript clicks bypass these issues.
- **RandomDataUtility.java**: Registration needs a unique email every time (the site rejects duplicates). I used `System.currentTimeMillis()` to create unique emails like `user1705000000000@test.com`.

### Reporting

**ExtentManager.java**: Creates the ExtentReports HTML report. Uses singleton pattern (only one report instance for the whole run).

**ExtentListener.java**: Implements TestNG's `ITestListener`. TestNG calls this whenever a test starts, passes, fails, or skips. It logs each event to the HTML report.

## How the tests work (step by step)

### RegistrationTests
1. Navigate to the register page
2. Fill in the form with random data
3. Submit and check for success message
4. For the duplicate email test: register once, log out, try registering with the same email, check for error

### LoginTests
- Register a new user, log out, then log in with the same credentials — verify success
- Try logging in with wrong email/password — verify error message
- Try with empty fields — verify staying on login page

### ProductTests
- Search for "computer" — verify products show up
- Search for gibberish — verify no products
- Navigate to books category, click first product — verify detail page opens

### CartTests
- Add a product to cart, go to cart — verify item count > 0
- Add then remove — verify cart is empty
- Add then update quantity to 3 — verify it shows "3"

### CheckoutTests (most complex one)
1. Register a new user
2. Add a product to cart
3. Go to cart, accept terms, click checkout
4. Fill billing address
5. Select shipping method
6. Select payment method
7. Confirm order
8. Verify "order completed" message

### ContactTests
- Fill contact form and submit — verify success message

### AccountTests
- Register and verify that the logged-in email shows correctly in the account section

## How to run the tests

**Important:** You MUST run tests through `testng.xml`. The `pom.xml` is already configured to use `src/test/resources/testng.xml` as the test suite file.

### What you need installed
- Java 17+ (`java -version` to check)
- Maven (`mvn -version` to check)
- A browser (Chrome or Edge)

### Run all tests

```bash
mvn clean test
or run src//test/resources/testng.xml directly
```


This runs everything defined in `src/test/resources/testng.xml`. All 7 test classes will execute.

### Run specific tests only

Edit `src/test/resources/testng.xml` and comment out the `<test>` blocks you don't want to run:

```xml
<!-- <test name="Cart Tests">
    <classes>
        <class name="tests.CartTests"/>
    </classes>
</test> -->
```

Then run `mvn clean test` again. Only the uncommented tests will run.

### Change browser

Edit `src/main/resources/config.properties`:

```properties
browser=edge
```

Options: `chrome` or `edge`

### View the test report

After running tests, open the `reports/` folder. You'll see an HTML file like:

```
reports/extent-report-20260627_103345.html
```

Open it in any browser to see which tests passed or failed.


## One more thing

If you're learning Selenium like I was, I hope this project helps you understand how everything fits together. The key concepts are:
- Page Object Model (organize code by page)
- WebDriverWait (don't use Thread.sleep)
- TestNG annotations (@BeforeMethod, @AfterMethod, @Test)
- Config files (keep settings separate from code)
- Good assertions (test what actually matters)

Feel free to use this as a reference for your own automation projects!
