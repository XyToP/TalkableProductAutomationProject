package common.cases;

import customerSite.talkableFrame.commonPages.advocateSharePage.advocateDashboard.AdSharePageForAdDashboard;
import customerSite.talkableFrame.commonPages.advocateSharePage.invite.AdvocateSharePageForInvite;
import customerSite.talkableFrame.commonPages.advocateSignupPage.AdvocateSignupPage;
import customerSite.talkableFrame.floatingWidget.advocateSharePage.AdvocateSharePageFW;
import customerSite.talkableFrame.floatingWidget.advocateSignupPage.AdvocateSignupPageFW;
import customerSite.talkableFrame.floatingWidget.advocateTrigerWidget.AdvocateTriggerWidgetFrame;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import talkable.common.CampaignPlacement;
import talkable.common.CampaignType;
import util.DriverConfig;
import util.WaitFactory;

public class ClientSiteScenarios {

    /*Scenario to create share link by Advocate
    Precondition: Page with Talkable FloatingWidgete should be opened
     * 1. Click FW
     * 2. Enter advocate values(@advocateName, @advocateEmail) and click Invite Friend
     * 3. Copy share link.
     * Returns: Share link.
      * */
    public static String completeAdvocateOfferForFloatingWidget(String advocateName, String advocateEmail){
        AdvocateTriggerWidgetFrame advocateTriggerWidget = new AdvocateTriggerWidgetFrame();
        AdvocateSignupPageFW advocateSignupPageFW = advocateTriggerWidget.click();
        AdvocateSharePageFW advocateSharePageFW = advocateSignupPageFW.submitForm(advocateName, advocateEmail);
        return advocateSharePageFW.getShareLink();
    }

    /*Technical method to setup new driver instace with clean cookies.
    * Please note: New driver instance will be applied for all further scenarios*/
    public static WebDriver setupDriverWithCleanCookies(WebDriver driver){
        driver.manage().deleteAllCookies();
        driver.quit();
        DriverConfig.cleanWebDriver();

        return DriverConfig.getDriver();
    }

    /*Verification that campaign is present on customer site
    * 1. Open site link.
    * 2. Check campaign.
    */
    public static boolean isCampaignOnCustomerSite(CampaignType campaignType, CampaignPlacement placement, String siteLink){
        DriverConfig.getDriver().navigate().to(siteLink);
        switch (placement){
            case FloatingWidget:
                return isFloatingPresent(placement);
            case Standalone:
                return isStandalonePresent(placement);
            case PostPurchase:
                return isPostPurchasePresent(campaignType, placement);
            case Gleam:
                return isGleamPresent(placement);
            default:
                Assert.fail("FAILED: Unknown placement type" + placement);
                return false;
        }
    }

    private static boolean isFloatingPresent(CampaignPlacement placement){
        try{
            WaitFactory.waitUntilVisibilityOfElementLocated(AdvocateTriggerWidgetFrame.getFrameLocator(), 3);
//            new AdvocateTriggerWidgetFrame();
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    private static boolean isStandalonePresent(CampaignPlacement placement){
        try{
//            new AdvocateSignupPage();
            WaitFactory.waitUntilVisibilityOfElementLocated(AdvocateSignupPage.getFrameLocator(), 5);
            return true;
        }catch(TimeoutException e){
            return false;
        }
    }

    private static boolean isPostPurchasePresent(CampaignType campaignType, CampaignPlacement placement){
        try {
            switch (campaignType){
                case Invite:
//                    new AdvocateSharePageForInvite();
                    WaitFactory.waitUntilVisibilityOfElementLocated(AdvocateSharePageForInvite.getFrameLocator(), 5);
                    return true;
                case AdvocateDashboard:
                    WaitFactory.waitUntilVisibilityOfElementLocated(AdSharePageForAdDashboard.getFrameLocator(), 5);
//                    new AdSharePageForAdDashboard();
                    return true;
                case RewardGleam:
                    Assert.fail("FAILED: Post Purc hase placement is not applicable for Gleam Campaign type.");
                    return false;
                default:
                    Assert.fail("FAILED: Unknown campaign type: <" + campaignType + ">.");
                    return false;
            }
        }catch(NoSuchElementException | TimeoutException e){
            return false;
        }
    }



    private static boolean isGleamPresent(CampaignPlacement placement){
        Assert.fail("Gleam verification is not yet implemented");
        return false;
    }
}
