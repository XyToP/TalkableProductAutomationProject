package execution.reports.previous.customer.upload;

import common.cases.CommonScenarios;
import common.cases.functionalities.ReportsScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import talkable.common.elements.pagination.Pagination;
import talkable.talkableSite.headerFrame.Header;
import talkable.talkableSite.reports.previousCustomersReport.PreviousCustomersReportPage;
import util.EnvFactory;

/*Link to test scenario: https://docs.google.com/spreadsheets/d/1ONGHch4nOJ28fGgNG4Hz3THlX1NWn8KxwM74fBSLngM/
 * */
public class PreviousCustomerUploadTesting extends BaseTest{
//    private static final String fileName = "testDataForExistingCustomersReport.csv";

    @Test
    public void login(){
        driver.navigate().to(EnvFactory.getEnvUrl());
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.loginAndCreateNewSite(EnvFactory.getReportsUser(), EnvFactory.getPassword());
    }

    @Test(dependsOnMethods = "login")
    public void openExistingCustomerReport(){
//        CommonScenarios
//                .createNewSite("ECR_" + TestDataGenerator.getRandomId(), "www.test.com")
//                .header.clickReportsButton()
//                .openExistingCustomerReport();
        new Header()
                .clickReportsButton()
                .openExistingCustomerReport();
    }

    @Test(dataProvider = "getTestData", dependsOnMethods = "openExistingCustomerReport")
    public void uploadFile(String fileName, String expectedProgress, String expectedUploadedEmails, String expectedStatus){
        ReportsScenarios
                .previousCustomerUploadTesting(fileName, expectedProgress, expectedUploadedEmails, expectedStatus);
        driver.navigate().to("https://admin.talkable.com/sites/ecr-563089371/previous_customers");
    }

    @Test(dependsOnMethods = "uploadFile")
    public void searchEmail(){
        PreviousCustomersReportPage report = new PreviousCustomersReportPage();
        String email = report.getFirstEmailValue();
        String total = report
                .searchEmail(email)
                .getTotalValue();
        Assert.assertEquals(total, "Total: 1", "FAILED: Incorrect total value");
        Assert.assertEquals(report.getFirstEmailValue(), email, "FAILED: Incorrect email is returned");
    }

    @Test(dependsOnMethods = "uploadFile")
    public void verifyPagination(){
        Pagination pagination = new PreviousCustomersReportPage()
                .searchEmail("")
                .getUpperPaginationForCsvListsTable();

        CommonScenarios.verifyPagination(pagination);
        CommonScenarios.verifyPagination(new PreviousCustomersReportPage().getUpperPaginationForCustomersList());
    }


    @DataProvider
    private Object[][] getTestData() /*throws IOException*/ {
//        TestDataConverter data = new TestDataConverter(fileName);
//        return data.getDataFromFile();
        return new Object[][]{
                {"01_positiveSet.csv", "100%", "30", "Completed"},
                {"02_positiveSetForDuplicatedRecordsTesting.csv", "100%", "1", "Completed"},
                {"03_positiveWithDuplicates.csv", "100%", "3", "Completed"},
                {"04_positiveAndNegative.csv", "100%", "2", "Completed"},
                {"05_negativeWithMultipleTests.csv", "0%", "0", "Failed"},
                {"08_zipTest.csv.zip" ,"100%", "8", "Completed"}
        };

    }


}
