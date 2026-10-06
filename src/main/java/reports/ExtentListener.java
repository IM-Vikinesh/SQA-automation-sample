package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/*
 * ExtentListener hooks into TestNG's lifecycle and logs test results
 * to ExtentReports. It implements ITestListener — TestNG calls these
 * methods automatically when test events happen.
 *
 * I used ThreadLocal<ExtentTest> because if I ever run tests in parallel
 * (multiple threads), each thread needs its own ExtentTest object.
 * Without ThreadLocal, parallel tests would overwrite each other's
 * report entries. Currently my tests run sequentially, but this
 * future-proofs the framework.
 */
public class ExtentListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        ExtentTest extentTest = extent.createTest(className + " — " + testName);
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test failed: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test skipped");
    }

    /*
     * onFinish is called after ALL tests complete.
     * extent.flush() writes everything to the HTML file.
     * Without this, the report file would be empty/broken.
     */
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
