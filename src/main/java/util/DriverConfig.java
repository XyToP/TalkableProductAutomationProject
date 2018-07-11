package util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Parameters;
import util.logging.Log;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class DriverConfig {

    //URL for selenium
    private static final String SELENOID_URL = "http://selenoid.tkbl:4444//wd/hub";

    private static WebDriver driver;

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    /*The flag indicates whether the test will be executed on Local or Remote machine.
    !!! Please note always put the flag to TRUE before creation of pull request to origin/master !!!*/
    private static final boolean remoteExecution = ExecutionVariables.remoteExecution();

    @Parameters()
    private WebDriver setNewLocalDriver()
    {
        System.out.println("LOG - Util: Start creation of new Local WebDriver");
        final File file = new File(PropertyLoader.loadProperty("path.mac.webDriver"));
        System.setProperty(PropertyLoader.loadProperty("webDriver"), file.getAbsolutePath());

        driver = new ChromeDriver(); //launch local webDriver

        WaitFactory.setDefaultImplicitlyWait();

        System.out.println("LOG - Util: New Local WebDriver is created");

        return driver;
    }



    private static ThreadLocal<WebDriver> setNewRemoteDriver(){
        System.out.println("LOG - Util: Start creation of new Remote WebDriver");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("66.0");

//        capabilities.setBrowserName("firefox");
//        capabilities.setVersion("60.0");
        capabilities.setCapability("enableVNC", true);
//        capabilities.setCapability("enableVideo", true);

        driver = null;
        URL url = null;
        try {
             url = new URL(SELENOID_URL);
        } catch (MalformedURLException e) {
            System.err.println("Exception found");
            e.printStackTrace();
        }
            driver = new RemoteWebDriver(
                    url,
                    capabilities
            );
        System.out.println("DEBAG: Driver created");

        //setup default wait
        if(driver != null) {
            WaitFactory.setDefaultImplicitlyWait();
        }
        tlDriver.set(driver);
        resizeBrowser(tlDriver.get());

        System.out.println("LOG - Util: New Remote WebDriver created");

//        return driver;
        return tlDriver;
    }

    public static WebDriver getDriver(){
        if (driver == null){
            if(remoteExecution) {
                driver = DriverConfig.setNewRemoteDriver().get(); //for remote testing
            }else {
                driver = new DriverConfig().setNewLocalDriver(); //for local testing
            }
        }
        return driver;
    }

    public static void cleanWebDriver(){
        WaitFactory.cleanWait();
        driver = null;
    }

    private static void resizeBrowser(WebDriver driver) {
        Dimension d = new Dimension(1300,1200);
//Resize current window to the set dimension
        driver.manage().window().setSize(d);
    }

    //to be refactored

    public static String switchToUnknownWindow(String parentHandle){
        String switchedChildHandle = null;
        for(String childHandle : driver.getWindowHandles()){
            switchToUnknownWindow(parentHandle, childHandle);
            switchedChildHandle = childHandle;
        }
        return switchedChildHandle;
    }

    private static void switchToUnknownWindow(String parentHandle, String childHandle){
        if (!childHandle.equals(parentHandle)){
            driver.switchTo().window(childHandle);
            Log.switchedToWindowMsg(childHandle);
        }
    }

    public static void switchToWindow(String windowHandle){
        driver.switchTo().window(windowHandle);
        Log.switchedToWindowMsg(windowHandle);
    }



}
