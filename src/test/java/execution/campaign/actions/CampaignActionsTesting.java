package execution.campaign.actions;

import common.cases.CommonScenarios;
import common.cases.functionalities.CspScenarios;
import common.cases.functionalities.ReportsScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.EnvFactory;
import util.PropertyLoader;
import util.TestDataGenerator;
import util.logging.Log;

import static talkable.common.CampaignPlacement.FloatingWidget;
import static talkable.common.CampaignPlacement.PostPurchase;
import static talkable.common.CampaignType.AdvocateDashboard;
import static talkable.common.CampaignType.Invite;
import static talkable.talkableSite.campaignsPage.Table.Status.TEST;


public class CampaignActionsTesting extends BaseTest{

    private String user = PropertyLoader.loadProperty("talkable.user.campaignActionsTest");
    private String pswrd = EnvFactory.getPassword();

    @Test
    public void login(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.loginAndCreateNewSite(user, pswrd);
//        CommonScenarios.login(
//                PropertyLoader.loadProperty("talkable.user.campaignActionsTest"),
//                EnvFactory.getPassword()
//        );
    }

    @Test(dependsOnMethods = "login")
    public void copyCampaign(){
        String campaignName = "TestCampaign" + TestDataGenerator.getRandomId();
        String campaignDesc = "Some campaign description";

        CommonScenarios.openCampaignsPage();
//        CommonScenarios.deleteAllCampaignsWithStatus(LIVE);
        CommonScenarios.createNewCampaignFromCampaignsPage(AdvocateDashboard, FloatingWidget);
        CommonScenarios.openCampaignRulesPage();
        CommonScenarios.setCampaignNameOnRulesPage(campaignName);
        CommonScenarios.setCampaignDescriptionOnRulesPage(campaignDesc);

        CommonScenarios.switchRedirectOnExpiredClaimCheckboxOnRulesPage();
        CommonScenarios.switchPlainTextVersionCheckboxOnRulesPage();
        CommonScenarios.switchUseFacebookAppIdCheckboxOnRulesPage();
        CommonScenarios.launchCampaign();

        CommonScenarios.copyCampaignFromDetailsPage();
        Assert.assertEquals(
                CommonScenarios.getCampaignNameFromNavigationMenu(),
                "Copy of " + campaignName,
                "FAILED: Incorrect campaign name of the copied campaign.");
        Assert.assertEquals(
                CommonScenarios.getCampaignPlacementFromNavigationMenu(),
                FloatingWidget,
                "FAILED: Incorrect placement of the copied campaign.");
        CommonScenarios.openCampaignRulesPage();
        Assert.assertEquals(
                CommonScenarios.getCampaignDescriptionOnRulesPage(),
                campaignDesc,
                "FAILED: Incorrect description of the copied campaign."
        );
        Assert.assertEquals(
                CommonScenarios.getPlainTextVersionCheckboxOnRulesPage(),
                "on",
                "FAILED: Incorrect 'Enable Plain Text version for all email views' value in copied campaign.");
        Assert.assertEquals(
                CommonScenarios.getRedirectOnExpiredClaimCheckbox(),
                "on",
                "FAILED: Incorrect 'Redirect a Friend to a Site URL when his offer is expired' value in copied campaign.");
        Assert.assertEquals(
                CommonScenarios.getUseFacebookAppIdCheckboxValueFromRulesPage(),
                "on",
                "FAILED: Incorrect 'Use Facebook App Share instead of sharer.php' value in copied campaign."
        );
        Log.testPassed("Campaign successfully copied.");
    }

    @Test(dependsOnMethods = "login")
    public void flushCampaignData(){
        String campName = "FlushDataTesting_" + TestDataGenerator.getRandomId();
        String advocate = "advocate." + TestDataGenerator.getRandomId() + "@test.com";

        CommonScenarios.navigateToAdminUrl();
        CommonScenarios.openCampaignsPage();
//        CommonScenarios.deleteAllCampaignsWithStatus(TEST);
        CommonScenarios.createNewCampaignFromCampaignsPage(Invite, PostPurchase);
        CommonScenarios.openCampaignRulesPage();
        CommonScenarios.setCampaignNameOnRulesPage(campName);
        CommonScenarios.openCampaignDetailsPage();
        //setup test data:
        CommonScenarios.createTestOfferNewPurchase();
        CommonScenarios.assertAdvocateOffersCountOnCampaignDetailsPage("Total: 1");

        //todo: temp solution till new CSP will be deployed on Prod
        CommonScenarios.openDashboardPage();
        String newCspUrl = driver.getCurrentUrl() + "/customer_service_portal";
        driver.navigate().to(newCspUrl);
        //end

        CspScenarios.createNewReferral(advocate, 177, campName);
        CommonScenarios.openCampaignDetailsPageFor(campName, TEST);
        CommonScenarios.flushCampaignDataFromDetailsPage();

        CommonScenarios.assertAdvocateOffersCountOnCampaignDetailsPage("Total: 0");
        ReportsScenarios.openReferralsReport();
        Assert.assertEquals(
                ReportsScenarios.getReferralsReportTotal(),
                "Not found",
                "FAILED: Incorrect total rows count on the Referrals Report.");

        ReportsScenarios.openRewardsReport();
        Assert.assertEquals(
                ReportsScenarios.getRewardsReportTotal(),
                "Not found",
                "FAILED: Incorrect total rows count on the Rewards Report.");

    }



}
