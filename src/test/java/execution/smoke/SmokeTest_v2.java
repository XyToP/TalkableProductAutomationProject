
package execution.smoke;

import common.cases.ClientSiteScenarios;
import common.cases.CommonScenarios;
import execution.BaseTest;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import util.EnvFactory;
import util.PropertyLoader;
import util.Screenshot;
import util.TestDataGenerator;
import util.logging.Log;

import java.util.Arrays;

import static talkable.common.CampaignPlacement.FloatingWidget;
import static talkable.common.CampaignType.Invite;
import static talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules.CouponCodeType.MultiUse;
import static talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules.DiscountType.FixedAmount;
import static talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules.DiscountType.Percentage;
import static talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules.IncentiveType.AdvocateSignupIncentive;
import static talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules.IncentiveType.FriendIncentive_NewCustomer;


@Listeners(util.Listeners.class)
public class SmokeTest_v2 extends BaseTest{

    private static final String SITE_URL = PropertyLoader.loadEnvProperty("test.sites.smoke.test");
    private static final String siteName = PropertyLoader.loadProperty("sites.name.smokeTest");

    private static final String liveStatusActive = "Status: Live";
    private static final String liveStatusTest = "Status: Test";
//    private static final String liveStatusDisabled = "Status: Disabled";
    private static final String expectedAdvocateOffersCount = "Total: 1";
    private static final String campaignName = "AUTO_TEST_" + TestDataGenerator.getRandomId();

    /*Link to test scenario: https://drive.google.com/open?id=1rnq3vo9qQ25vtTwPF7hwXRt7zMBiK28VuAyPc50_X7s
    * */
    @Test
    public void createAndActivateCampaign(){

        //Test data:
        String campaignDesc = "Campaign for smoke test";
        String advocateOfferDeadlineDate = "10/18/2020";
        String endTime = "05:48";
        String friendDeadline = "10/21/2020";

        CommonScenarios.acceptCookiesUsage();
        //1. Login to Talkable.
        CommonScenarios.login(
                EnvFactory.getCommonUser(),
                EnvFactory.getPassword()
        );
        CommonScenarios.switchToSiteByVisibleText(siteName);

        // 2. Verify site Name
        Assert.assertEquals(
                CommonScenarios.getSiteNameFromHeader(),
                siteName,
                "FAILED: Incorrect site name"
        );
        // Deactivate all campaigns
        CommonScenarios.deactivateAllCampaigns();


        // 3. Add new Campaign (Type = FW )
        CommonScenarios.initiateCampaignCreation(Invite, FloatingWidget);
        Assert.assertEquals(CommonScenarios.getCampaignStatusFromMenu(), liveStatusTest, "FAILED: Incorrect campaign status");

        // 4. Navigate to Rules page
        // 5. Modify Campaign name
        // 6. Set Campaign description
        CommonScenarios.openCampaignRulesPage();
        CommonScenarios.setCampaignNameAndDescriptionOnRulesPage(campaignName, campaignDesc);

        // 7. Set Advocate/Friend Offer deadline
        CommonScenarios.setDeadlinesOnRulesPage(
                advocateOfferDeadlineDate,
                endTime,
                friendDeadline,
                endTime);

        // 8. Add incentive (Type = Sign Up)
        /*todo:The issue with 'Changes saved' notification should be investigated after adding of video recorder to the selenoid served.
        For now the message is not returned on bastion when test is running on Selenoid. (For local execution works fine.)
        Link to related JIRA task:

        * */
        try {
            CommonScenarios.addNewIncentive(AdvocateSignupIncentive, 25, FixedAmount, MultiUse);
        }catch (TimeoutException e){
            System.out.println(" ----- DEBAG.Smoke_Test_v2.addNewIncentive ---- ");
            System.err.println("DEBAG: Timeout exception were thrown during adding of incentive.");
            System.err.println("Screenshot: " + new Screenshot().makeScreenshot());
            System.err.println("  Exception message: \r\n" +  e.getMessage() + "\r\n" + Arrays.toString(e.getStackTrace()));
            System.out.println(" ----- End DEBAG.Smoke_Test_v2 ---- ");
        }

        try {
            CommonScenarios.addNewIncentive(FriendIncentive_NewCustomer, 10, Percentage, MultiUse);
        }catch (TimeoutException e){
            System.out.println(" ----- DEBAG.Smoke_Test_v2.addNewIncentive ---- ");
            System.err.println("DEBAG: Timeout exception were thrown during adding of incentive.");
            System.err.println("Screenshot: " + new Screenshot().makeScreenshot());
            System.err.println("  Exception message: \r\n" +  e.getMessage() + "\r\n" + Arrays.toString(e.getStackTrace()));
            System.out.println(" ----- End DEBAG.Smoke_Test_v2 ---- ");
        }

        // 11. Launch Campaign
        CommonScenarios.launchCampaign();
        Assert.assertEquals(CommonScenarios.getCampaignStatusFromMenu(), liveStatusActive, "FAILED: Campaign is not activated");

        // 12. Create test offer
        CommonScenarios.createTestOfferNewAffiliateMember("test" + System.currentTimeMillis() + "@test.com");
        CommonScenarios.assertAdvocateOffersCountOnCampaignDetailsPage(expectedAdvocateOffersCount);
//        Assert.assertEquals(
//                CommonScenarios.getAdvocateOfferTotalCountFromCampaignDetailsPage(),
//                expectedAdvocateOffersCount,
//                "FAILED: Incorrect Offers count on Campaign Details page");

        // 13. Verify campaign on site
        if(ClientSiteScenarios.isCampaignOnCustomerSite(Invite, FloatingWidget, SITE_URL)){
            Log.testPassed("Campaign is present on Customer site.\r\n");
        }else {
            Assert.fail("FAILED: Advocate trigger widget is not displayed on Customer Site.\r\n");
        }

    }

    @Test(dependsOnMethods = "createAndActivateCampaign")
    public void deactivateCampaign(){
        //Open Talkable
        CommonScenarios.navigateToAdminUrl();
        CommonScenarios.deactivateAllCampaigns();

        //Verify that campaign is not displayed on site
        if(ClientSiteScenarios.isCampaignOnCustomerSite(Invite, FloatingWidget, SITE_URL)){
            Assert.fail("FAILED: Floating Widget is displayed after deactivation. Test Failed");
        }else {
            Log.testPassed("Campaign is present on Customer site.\r\n");
        }
    }

}