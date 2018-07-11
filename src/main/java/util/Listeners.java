package util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.*;
import util.logging.Log;

public class Listeners implements IInvokedMethodListener, ITestListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod() && ITestResult.FAILURE == result.getStatus()) {
            Log.testFailed(method.getTestMethod().getMethodName());
            Throwable throwable = result.getThrowable();
            String originalMessage = throwable.getMessage();
            try {
                String screenshotUrl = new Screenshot().makeScreenshot();
                String newMessage = "FAILED: " + originalMessage +
                        "\r\n *** Test artifacts  *** " +
                        "\r\nTest failed on URL: " + DriverConfig.getDriver().getCurrentUrl() + "\r\n" +
//                    "<a href=\"" + DriverConfig.getDriver().getCurrentUrl() + "\">Link to the failed page</a>\r\n" +
                        "Screenshot: " + screenshotUrl + "\r\n"
//                    "Screenshot: <a href=\"" + screenshotUrl + "\">" + screenshotUrl + "</a>" + "\r\n"
                        ;
                try {
                    FieldUtils.writeField(throwable, "detailMessage", newMessage, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (UnhandledAlertException e){
                Log.debagRecord("Not able to take a screenshot during <UnhandledAlertException>:");
                System.err.println(e.getMessage());
            }
        }

        if(method.isTestMethod() && ITestResult.SUCCESS == result.getStatus()){
            Log.testPassed("Test method: " + method.getTestMethod().getMethodName());
        }
    }


    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
