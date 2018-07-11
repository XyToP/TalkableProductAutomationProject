package external.shopify.shop;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.integrationPage.IntegrationPage;

public class ShopifyShopLoginPage extends AbstractElementsContainer{
    private static final By emailLctr = By.xpath("//*[@id = 'Login']");
    private static final By pswrdFieldLctr = By.xpath("//*[@id = 'Password']");
    private static final By loginBtnLctr = By.xpath("//*[@id = 'LoginSubmit']");

    private Element emailField = new Element(emailLctr, "Email field");
    private Element passwordField = new Element(pswrdFieldLctr, "Password field");
    private Element loginBtn = new Element(loginBtnLctr, "Login button");

    private void populateCredentials(String email, String password){
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        loginBtn.click();
    }

    public IntegrationPage enterCredentialToIntegrateTalkable(String email, String password){
        populateCredentials(email, password);
        return new IntegrationPage();
    }
}
