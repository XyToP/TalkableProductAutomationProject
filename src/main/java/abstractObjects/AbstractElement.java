package abstractObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import util.DriverConfig;
import util.WaitFactory;
import util.logging.Log;

import java.util.ArrayList;

public abstract class AbstractElement implements DrivenElement{

    private WebElement webElement;
    private By locator;
    private WebDriver driver = DriverConfig.getDriver();
    protected WebDriverWait wait = WaitFactory.getExplicitWait();
    private Actions actions = new Actions(driver);


    protected void setWebElement(By locator)
    {
        try {
            webElement = driver.findElement(locator);
            this.locator = locator;
        }
        catch (NoSuchElementException e){
            Assert.fail("FAILED Assert:" + e.getMessage());
        }
    }

    public void setWebElement(WebElement webElement){
        this.webElement = webElement;
    }

    public void click()
    {
        if(locator != null){
            setWebElement(locator);
        }
        actions.moveToElement(this.webElement).perform();
            this.webElement.click();
//        doubleClickForTimeout(this.webElement);

        Log.clickMsg(this);
    }

//    private void doubleClickForTimeout(WebElement element){
//        try{
//            element.click();
//        }catch (TimeoutException e){
//            System.out.println("DEBAG: Timeout error received during click() to " + this.getClass().getName());
//            element = driver.findElement(locator);
//            element.click();
//            System.out.println("DEBAG: Successfully clicked during second attempt");
//        }
//    }

    public void sendKeys(String value)
    {
        this.webElement.sendKeys(value);
        Log.enterValueMsg(value, this);
    }

    public void enterTextAndClickENTER(String value){
        this.webElement.sendKeys(value + Keys.RETURN);
        Log.enterValueAndClickEnterMsg(value, this);
    }

    public String getText(){
        return this.webElement.getText();
    }

    public boolean isEnabled(){
        if(!webElement.isEnabled()){
            Log.webElementIsNotActiveMsg(this);
        }

        return webElement.isEnabled();
    }

    public ArrayList<DrivenElement> getElements(){
        ArrayList<DrivenElement> output = new ArrayList<>();
        for (WebElement element:
                driver.findElements(locator)) {
            try {
                AbstractElement abstractElement = this.getClass().newInstance();
                abstractElement.setWebElement(element);
                output.add(abstractElement);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
        return output;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public By getLocator(){
        return this.locator;
    }

    protected WebDriver getDriver(){
        return this.driver;
    }

    public void moveMouseOver(){
        Actions builder = new Actions(driver);
        builder.moveToElement(webElement).build().perform();
        Log.moveMouseOverMsg(this);
    }

    public void clear(){
        webElement.clear();
        Log.elementClearedMsg(this);
    }

    public String getAttribute(String attributeName){
        return this.webElement.getAttribute(attributeName);
    }

}

