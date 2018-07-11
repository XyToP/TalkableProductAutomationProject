package talkable.loginPage;


import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.headerFrame.Header;


public class LoginPage extends AbstractElementsContainer {
    private static final By topErrorMsgLctr = By.xpath("//*[@class = 'base-form-notice-right-part']/p");

    //Elements
    private ElmntEmailInput emailInput;
    private ElmntPasswordInput passwordInput;
    private ElmntLoginButton loginButton;



    public LoginPage(){
        //initiate web containers
        emailInput = new ElmntEmailInput();
        passwordInput = new ElmntPasswordInput();
        loginButton = new ElmntLoginButton();

    }

    public Header submitLoginForm(String email, String password){
        //fill Email
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
        return new Header();
    }

    public String getTopErrorMessageString(){
        return new Element(topErrorMsgLctr).getText();
    }





}
