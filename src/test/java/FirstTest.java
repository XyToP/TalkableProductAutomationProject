
import common.cases.CommonScenarios;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import talkable.talkableSite.IntegrationInstructionPage.IntegrationInstructionPage;
import talkable.talkableSite.campaign.pages.campaignRulesPage.IncentiveTile;
import talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules;
import talkable.talkableSite.campaignsPage.PageCampaigns;
import talkable.talkableSite.headerFrame.Header;
import talkable.talkableSite.siteDashboardPage.SiteDashboardPage;
import util.DriverConfig;


public class FirstTest {

    WebDriver driver;

    CommonScenarios commonScenarios = new CommonScenarios();

    private String siteName = "automationSite";

    @BeforeClass
    public void setup(){
        driver = new DriverConfig().getDriver();
        driver.navigate().to("https://void.talkable.com");
    }

    @Test
    public void test1_login(){
        commonScenarios.login("maxim.laba@talkable.com", "Password@1");
        driver.navigate().to("https://admin.void.talkable.com/sites/custom2501/campaigns/45519/edit#/incentives");

    }

    @Test
    public void test2(){
        PageCampaignRules campaignRules = new PageCampaignRules();
        campaignRules.createNewIncentive(PageCampaignRules.IncentiveType.AdvocateReferralIncentive, 1, PageCampaignRules.DiscountType.Percentage, PageCampaignRules.CouponCodeType.MultiUse);
        IncentiveTile incentive = campaignRules.getIncentiveTile(PageCampaignRules.IncentiveType.AdvocateReferralIncentive, "1% multi-use coupon code");
        System.out.println(incentive.getValue());
        System.out.println(incentive.getIdentifier());
        incentive.delete();
    }








}
