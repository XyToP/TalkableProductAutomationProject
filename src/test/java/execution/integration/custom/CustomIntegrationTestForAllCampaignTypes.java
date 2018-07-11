package execution.integration.custom;

import common.cases.ClientSiteScenarios;
import common.cases.CommonScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import talkable.common.CampaignPlacement;
import talkable.common.CampaignType;
import util.EnvFactory;
import util.PropertyLoader;
import util.TestDataGenerator;
import util.logging.Log;

import static talkable.common.CampaignPlacement.PostPurchase;


/*Link to test scenario: https://docs.google.com/spreadsheets/d/1FLkr-T2s-mVnG770gLh4imwMnoO0vFtduYquM_49zzQ/edit#gid=0
* */
public class CustomIntegrationTestForAllCampaignTypes extends BaseTest{

    private static final String customerSiteUrl = PropertyLoader.loadEnvProperty("test.sites.simpleTestForAllCampaigns");
    private static final String siteName = PropertyLoader.loadProperty("sites.name.simpleTestForAllCampaigns");

    @BeforeClass
    public void setup() {
        this.driver.navigate().to(EnvFactory.getEnvUrl());
    }

    @Test
    public void login(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.login(EnvFactory.getCommonUser(), EnvFactory.getPassword());
    }

    @Test(dataProvider = "getTestData", dependsOnMethods = "login")
    public void createCampaign(CampaignType campaignType, CampaignPlacement campaignPlacement){
        String campaignName = "AUTO_TEST_" + TestDataGenerator.getRandomId();

        //open Talkable admin site:
        this.driver.navigate().to(EnvFactory.getAdminUrl());
        CommonScenarios.switchToIntegratedSiteByVisibleText(siteName);

        CommonScenarios.openCampaignsPage();
        CommonScenarios.deactivateAllCampaigns();

        //Create new campaign
        CommonScenarios.createNewCampaignFromCampaignsPage(campaignType, campaignPlacement);
        CommonScenarios.openCampaignRulesPage();
        CommonScenarios.setCampaignNameOnRulesPage(campaignName);
        CommonScenarios.launchCampaign();

        //Verification on Customer Site:
        if (ClientSiteScenarios.isCampaignOnCustomerSite(campaignType, campaignPlacement, getCustomerSiteUrl(campaignPlacement))){
            Log.testPassed(campaignPlacement.toString() + " is present on the site\r\n");
        }
        else{
            Assert.fail("FAILED: " + campaignType.toString() + ": " + campaignPlacement.toString() + " is not displayed on the customer site: " + getCustomerSiteUrl(campaignPlacement));
        }

        //re-open Talkable admin site:
        this.driver.navigate().to(EnvFactory.getAdminUrl());
        CommonScenarios.switchToIntegratedSiteByVisibleText(siteName);
        CommonScenarios.deactivateCampaign(campaignName);

        //Verify that campaign is inactive on the Customer Site:
        if (!ClientSiteScenarios.isCampaignOnCustomerSite(campaignType, campaignPlacement, getCustomerSiteUrl(campaignPlacement))){
            Log.testPassed(campaignPlacement.toString() + " is not displayed on the site\r\n");
        }
        else{
            Assert.fail("FAILED: " + campaignType.toString() + ": " + campaignPlacement.toString() + " is displayed on the customer site after deactivation");
        }
    }

    @DataProvider
    private Object[][] getTestData(){
        return new Object[][]{
                {CampaignType.Invite, CampaignPlacement.FloatingWidget},
                {CampaignType.Invite, CampaignPlacement.Standalone},
                {CampaignType.AdvocateDashboard, CampaignPlacement.FloatingWidget},
                {CampaignType.AdvocateDashboard, CampaignPlacement.Standalone},
                {CampaignType.Invite, PostPurchase},
                {CampaignType.AdvocateDashboard, PostPurchase}
//                {CampaignType.RewardGleam, CampaignPlacement.Gleam}
                //TODO: separate test should be added for Gleam
        };
    }

    private String getCustomerSiteUrl(CampaignPlacement placement){
        if(placement == PostPurchase){
            return customerSiteUrl + "pp.html";
        }
        return customerSiteUrl;
    }

}
