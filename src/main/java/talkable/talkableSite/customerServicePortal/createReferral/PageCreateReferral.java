package talkable.talkableSite.customerServicePortal.createReferral;


import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.customerServicePortal.AbstractCustomerServicePortalPage;
import talkable.talkableSite.customerServicePortal.pendingReferrals.ReferralDetailsPage;
import talkable.talkableSite.customerServicePortal.personLookup.PersonLookupPage;
import util.WaitFactory;

public class PageCreateReferral extends AbstractCustomerServicePortalPage {

    private static final By firstRowCampaignCheckBoxLctr = By.xpath("//li[1]/div[contains(@class,'campaign-check')]/div");
    private static final By firstRowCampaignNameLctr = By.xpath("//li[1]/div[contains(@class,'campaign-data')]/a");

    private Element advocateEmailInput = new Element(By.xpath("//input[@name='advocate_email']"), "'Advocate's Email' field");
    private Element orderSubtotalInput = new Element(By.xpath("//input[@name='friend_origin_subtotal']"), "'Order Subtotal' field'");
    private Element searchCampaignField = new Element(By.xpath("//div[@class='campaignfilter']//input"), "'Search Campaign' field");
    private Element createReferralBtn = new Element(By.xpath("//button[@type = 'submit']"), "'Create Referral' button");

    public ReferralDetailsPage createReferralNew(String advocateEmail, int orderSubtotal, String campaignName){
        advocateEmailInput.sendKeys(advocateEmail);
        orderSubtotalInput.sendKeys(String.valueOf(orderSubtotal));
        searchCampaignField.sendKeys(campaignName);
        WaitFactory.waitUntilVisibilityOfElementLocated(firstRowCampaignNameLctr, 3);
        new Element(firstRowCampaignCheckBoxLctr, "First campaign from search result").click();
        createReferralBtn.click();

        return new ReferralDetailsPage();
    }

    public void createReferralFriendPurchaseExists(){
        //method should be completed
    }
}
