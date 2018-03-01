package execution.reports;

import common.cases.ReportsScenarios;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import talkable.talkableSite.headerFrame.Header;
import talkable.talkableSite.reports.previousCustomersReport.PreviousCustomersReportPage;
import talkable.talkableSite.reports.reportsPage.ReportsPage;
import util.DriverConfig;
import util.TestDataConverter;

import java.io.IOException;

public class PreviousCustomerUploadTesting extends ReportsScenarios{
    private static final String userEmail = "maxim.laba@talkable.com";
    private static final String userPassword = "Password@1";
    private static final String fileName = "testDataForExistingCustomersReport.csv";


    @BeforeClass
    public void setup(){
        WebDriver driver = new DriverConfig().getDriver();
        driver.navigate().to("https://talkable.com"); //"https://void.talkable.com"
        Header header = login(userEmail, userPassword);
        //Navigate to Existing Customers Report
        ReportsPage reportsPage = header.clickReportsButton();
        PreviousCustomersReportPage previousCustomersReportPage = reportsPage.openExistingCustomerReport();

    }


    @Test(dataProvider = "getTestData")
    public void testing(String fileName, String expectedProgress, String expectedUploadedEmails, String expectedStatus){
        previousCustomerUploadTesting(fileName, expectedProgress, expectedUploadedEmails, expectedStatus);
    }


    @DataProvider
    private Object[][] getTestData() throws IOException {
        TestDataConverter data = new TestDataConverter(fileName);
        return data.getDataFromFile();
    }
}
