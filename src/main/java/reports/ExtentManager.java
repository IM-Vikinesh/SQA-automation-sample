package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * ExtentManager creates and manages the ExtentReports HTML report.
 *
 * Singleton pattern: only ONE report instance for the entire test run.
 * If I created a new report in every test class, each would overwrite
 * the previous one — I learned this the hard way.
 *
 * The report filename includes a timestamp so each run creates a new file
 * in the reports/ folder (history is preserved).
 */
public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            // Timestamp ensures unique filenames (e.g., extent-report-20260627_103345.html)
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/reports/extent-report-" + timestamp + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("SQA Automation Test Report");
            sparkReporter.config().setDocumentTitle("SQA Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Browser", "Chrome/Edge");
        }
        return extent;
    }
}
