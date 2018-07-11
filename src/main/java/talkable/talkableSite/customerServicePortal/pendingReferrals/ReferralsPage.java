package talkable.talkableSite.customerServicePortal.pendingReferrals;

import abstractObjects.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import talkable.talkableSite.customerServicePortal.AbstractCustomerServicePortalPage;
import talkable.talkableSite.customerServicePortal.personLookup.PersonInfoSection;
import talkable.talkableSite.customerServicePortal.personLookup.PersonLookupPage;
import util.WaitFactory;
import util.logging.Log;

import java.util.ArrayList;

public class ReferralsPage extends AbstractCustomerServicePortalPage{

    private static final By rowsLctr = By.xpath("//tr[@class='CSP-row']");
    private static final By pendingReferralsCountLctr = By.xpath("//h2");
    private static final By approveAllBtnLctr = By.xpath("//button[contains(@data-action, 'approve all')]");
    private static final By seeDetailsForActionedReferralLctr = By.xpath("//tbody//a[contains(text(), 'See details')]");

    private Element pendingReferralsCount = new Element(pendingReferralsCountLctr);


    public ReferralsPage(){
        if(!pendingReferralsCount.getText().equals("Pending Referrals — 0")){
            getRows();
        }
    }


    public ReferralsPage approveAllReferrals(){
        if(pendingReferralsCount.getText().equals("Pending Referrals — 0")){
            Assert.fail("FAILED: 'Approve All' button is not available on Pending Referrals page when pending referral count = '0'");
        }
        new Element(approveAllBtnLctr, "'Approve All' button").click();
        new ConfirmationPopup().confirm();
        WaitFactory.waitUntilTextToBePresentInElement(
                pendingReferralsCountLctr,
                "Pending Referrals — 0",
                5);
        Log.logRecord("All pending referrals were approved.");
        return new ReferralsPage();
    }

    public String getPendingReferralsCount(){
        return pendingReferralsCount.getText().substring(13);
    }

    @Deprecated
    public PersonInfoSection clickSeeDetailsForActionedReferral(){
        new Element(seeDetailsForActionedReferralLctr, "'See Details' link").click();
        return new PersonLookupPage().getPersonInfoSection();
    }


    private ArrayList<ReferralRow> getRows(){
        ArrayList<ReferralRow> rows = new ArrayList<>();
        for (WebElement wElement :
                driver.findElements(rowsLctr)) {
            rows.add(new ReferralRow(wElement));
        }
        return rows;
    }

    private ReferralRow findRowBy(String advocateEmail){
        for (ReferralRow row :
                getRows()) {
            if (row.getAdvocateEmail().equals(advocateEmail)){
                return row;
            }
        }
        Assert.fail("FAILED: Referral row with advocate email <" + advocateEmail + "> was not found on the Pending Referrals page.");
        return null;
    }


    public ReferralDetailsPage approveReferral(String advocateEmail){
        findRowBy(advocateEmail).approve();
        waitSaving();
        Log.logRecord("Referral was approved. Advocate email <" + advocateEmail + ">.");
        return new ReferralDetailsPage();
    }

    public ReferralDetailsPage voidReferral(String advocateEmail){
        findRowBy(advocateEmail).voidReferral();
        waitSaving();
        Log.logRecord("Referral was voided. Advocate email <" + advocateEmail + ">.");
        return new ReferralDetailsPage();
    }



    class ReferralRow{
        private final By adEmailLctr = By.xpath("./td[2]/span");
        private final By frEmailLctr = By.xpath("./td[3]/span");
        private final By apprroveActionBtnLctr = By.xpath(".//div[@class='CSP-row-actions-buttons']/div[1]/button");
        private final By voidActionBtnLctr = By.xpath(".//div[@class='CSP-row-actions-buttons']/div[2]/button");

        private WebElement parentElement;
        private Element approveBtn;
        private Element voidBtn;


        ReferralRow(WebElement parent){
            parentElement = parent;
            setElements();
        }

        void setElements(){
            approveBtn = new Element(parentElement.findElement(apprroveActionBtnLctr));
            voidBtn = new Element(parentElement.findElement(voidActionBtnLctr));
        }

        void approve(){
            approveBtn.click();
        }

        void voidReferral(){
            voidBtn.click();
        }

        String getAdvocateEmail(){
            return new Element(parentElement.findElement(adEmailLctr)).getText();
        }

        String getFriendEmail(){
            return new Element(parentElement.findElement(frEmailLctr)).getText();
        }

    }

}
