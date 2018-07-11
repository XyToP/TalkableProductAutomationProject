package execution.campaign.placements;

import common.cases.CommonScenarios;
import customerSite.talkableFrame.commonPages.advocateSharePage.invite.AdvocateSharePageForInvite;
import customerSite.talkableFrame.commonPages.advocateSignupPage.AdvocateSignupPage;
import customerSite.talkableFrame.floatingWidget.advocateTrigerWidget.AdvocateTriggerWidgetFrame;
import execution.BaseTest;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.testng.Assert;
import org.testng.annotations.*;
import talkable.common.CampaignPlacement;
import talkable.talkableSite.headerFrame.Header;
import util.DriverConfig;
import util.EnvFactory;
import util.PropertyLoader;
import util.logging.Log;


public class BasePlacementsTest extends BaseTest{

//    private static final String siteName = PropertyLoader.loadProperty("sites.name.campaign.placements.test");
    protected static final String SITE_URL = PropertyLoader.loadEnvProperty("test.sites.campaign.placements");
    protected static final String USER_EMAIL = PropertyLoader.loadProperty("talkable.user.campaign.placements");

    static final String page1 = "index.html";
    static final String page2 = "page2.html";
    static final String page3 = "pp1.html";
    static final String page4 = "pp2.html";

    @BeforeSuite
    public void precondition(){
        //login to Talkable
        driver.navigate().to(EnvFactory.getEnvUrl());
        CommonScenarios.login(USER_EMAIL, EnvFactory.getPassword());
    }





//    @AfterSuite
    public void logout(){
        new Header().logout();
    }

    void positiveVerificationOnSite(CampaignPlacement placement, String page){
        DriverConfig.getDriver().navigate().to(SITE_URL + page);
        try {
            initializedCampaign(placement);
        }
        catch (NoSuchElementException e){
            Assert.fail("FAILED: Campaign <" + placement + "> is not found on page: " + page);
        }
        Log.testPassed(placement.toString() + " is displayed on page: " + page );
    }

    void negativeVerificationOnSite(CampaignPlacement placement, String page){
        DriverConfig.getDriver().navigate().to(SITE_URL + page);
        try{
            initializedCampaign(placement);
            Assert.fail("FAILED: " + placement.toString() + " campaign is displayed on " + page);
        }catch (NotFoundException e){
            Log.testPassed(placement.toString() + " is not displayed on page: " + page );
        }
    }

    private void initializedCampaign(CampaignPlacement placement){
        switch (placement){
            case FloatingWidget:
                new AdvocateTriggerWidgetFrame();
                break;
            case Standalone:
                new AdvocateSignupPage();
                break;
            case PostPurchase:
                new AdvocateSharePageForInvite();
                break;
        }
    }

}
