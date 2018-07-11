package execution.integration.shopify;

import common.cases.ClientSiteScenarios;
import common.cases.CommonScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import talkable.common.CampaignPlacement;
import talkable.common.CampaignType;
import util.EnvFactory;
import util.PropertyLoader;

import static talkable.common.CampaignPlacement.Standalone;
import static talkable.common.CampaignType.Invite;


public class ShopifyIntegrationTest extends BaseTest{

    private String user = PropertyLoader.loadProperty("talkable.user.shopifyIntegration");
    private String passwrd = EnvFactory.getPassword();
    private String shopifyUrl = PropertyLoader.loadEnvProperty("test.sites.shopifyIntegration");
    private String campaignPageUrl = shopifyUrl + "/pages/share";


    @Test
    public void integrateShopifySite(){
        String shopifyPlatform = "Shopify";
        CampaignType cType = Invite;
        CampaignPlacement placement = Standalone;

        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.login(user, passwrd);
        CommonScenarios.openCampaignsPage();
        CommonScenarios.deactivateAllCampaigns();

        CommonScenarios.openSiteSettingsPage();
        CommonScenarios.setUrlAndPlatformOnSiteSettings("www.temp.com", "Custom");
        CommonScenarios.setUrlAndPlatformOnSiteSettings(shopifyUrl, shopifyPlatform);
        CommonScenarios.openSiteIntegrationPage();
        CommonScenarios.installShopifyApp(user, passwrd);
        CommonScenarios.openCampaignsPageAndCreateCampaign(Invite, Standalone);
        CommonScenarios.launchIntegratedCampaign();
        Assert.assertEquals(
                ClientSiteScenarios.isCampaignOnCustomerSite(Invite, Standalone, campaignPageUrl),
                true,
                "FAILED: <"+cType+"."+placement+">Campaign is not displayed in Shopify Shop <" + shopifyUrl + ">."
        );

        CommonScenarios.navigateToAdminUrl();
        CommonScenarios.openCampaignsPage();
        CommonScenarios.deactivateAllCampaigns();
        Assert.assertEquals(
                ClientSiteScenarios.isCampaignOnCustomerSite(Invite, Standalone, campaignPageUrl),
                false,
                "FAILED: <"+cType+"."+placement+">Campaign is displayed in Shopify Shop <" + shopifyUrl + "> when it was deactivated."
        );

        CommonScenarios.navigateToAdminUrl();
        CommonScenarios.openSiteSettingsPage();
        CommonScenarios.setUrlAndPlatformOnSiteSettings("www.temp.com", "Custom");
    }
}
