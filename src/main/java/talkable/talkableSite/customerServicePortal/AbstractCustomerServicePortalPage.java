package talkable.talkableSite.customerServicePortal;

import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.AbstractTalkableSitePage;
import talkable.talkableSite.customerServicePortal.blacklistingPage.BlacklistingPage;
import talkable.talkableSite.customerServicePortal.createReferral.PageCreateReferral;
import talkable.talkableSite.customerServicePortal.pendingReferrals.ReferralsPage;
import talkable.talkableSite.customerServicePortal.personLookup.PersonLookupPage;
import util.logging.Log;

public class AbstractCustomerServicePortalPage extends AbstractTalkableSitePage{
    private static final By createReferralTabLctr = By.xpath("//a[text()='Create referral']");
    private static final By personLookupTabLctr = By.xpath("//a[text()='Person lookup']");
    private static final By pendingReferralsLctr = By.xpath("//ul[@class='CSP-sidebar']/li[2]/a");
    private static final By blacklistingLctr = By.xpath("//a[text()='Blacklisting']");

    private Element createReferralTab = new Element(createReferralTabLctr, "'Create Referral' tab" );
    private Element personLookupTab = new Element(personLookupTabLctr, "'Person Lookup' tab" );
    private Element pendingReferralsTab = new Element(pendingReferralsLctr, "'Pending Referrals' tab");
    private Element blacklistingTab = new Element(blacklistingLctr, "Blacklisting tab");


    public PageCreateReferral openCreateReferralPage(){
        createReferralTab.click();
        PageCreateReferral page = new PageCreateReferral();
        Log.logRecord("Create Referral page is opened on CSP");
        return page;
    }

    public PersonLookupPage openPersonLookupPage(){
        personLookupTab.click();
        PersonLookupPage page = new PersonLookupPage();
        Log.logRecord("Person Lookup page is opened on CSP");
        return page;
    }

    public ReferralsPage openPendingReferralsPage(){
        pendingReferralsTab.click();
        ReferralsPage page = new ReferralsPage();
        Log.logRecord("Pending Referrals page is opened on CSP");
        return page;
    }

    public BlacklistingPage openBlacklistingPage(){
        blacklistingTab.click();
        BlacklistingPage page  = new BlacklistingPage();
        Log.logRecord("Blacklisting page is opened on CSP");
        return page;
    }

}
