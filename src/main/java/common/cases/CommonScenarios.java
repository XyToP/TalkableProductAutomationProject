package common.cases;

import api.objects.Site;
import org.testng.Assert;
import talkable.common.elements.WeUseCookieMsg;
import talkable.common.elements.pagination.Pagination;
import talkable.talkableSite.IntegrationInstructionPage.IntegrationInstructionPage;
import talkable.addYourSitePage.AddSitePage;
import talkable.common.CampaignPlacement;
import talkable.common.CampaignType;
import talkable.talkableSite.campaign.pages.campaignRulesPage.PageCampaignRules;
import talkable.talkableSite.campaign.pages.detailsPage.CampaignDetailsPage;
import talkable.talkableSite.campaign.pages.campaignNavigationMenu.CampaignNavigationMenu;
import talkable.talkableSite.campaign.pages.editorPage.SimpleEditorPage;
import talkable.talkableSite.campaign.pages.multiCampaignEditor.PageMultiCampaignEditor;
import talkable.talkableSite.campaignsPage.PageCampaigns;
import talkable.talkableSite.campaignsPage.Table;
import talkable.talkableSite.customerServicePortal.personLookup.PersonLookupPage;
import talkable.talkableSite.integrationPage.IntegrationPage;
import talkable.talkableSite.reports.newAffiliateMember.PageNewAffiliateMember;
import talkable.talkableSite.reports.purchases.createNewPurchasePage.CreateNewPurchasePage;
import talkable.talkableSite.createNewCampaignPage.CreateNewCampaignPage;
import talkable.talkableSite.headerFrame.Header;
import talkable.homePage.HomePage;
import talkable.talkableSite.campaign.pages.launchCampaignPage.LaunchCampaignPage;
import talkable.loginPage.LoginPage;
import talkable.talkableSite.siteDashboardPage.SiteDashboardPage;
import talkable.talkableSite.siteSettings.ElmntUnsavedChangesPopup;
import talkable.talkableSite.siteSettings.SiteSettingsPage;
import talkable.talkableSite.siteSettings.basic.SiteSettingsBasicTab;
import talkable.talkableSite.siteSettings.contacts.SiteSettingsContactsTab;
import talkable.userRegistration.chosePlatformPage.ChosePlatformPage;
import talkable.userRegistration.createAccountPage.CreateAccountPage;
import util.DriverConfig;
import util.EnvFactory;
import util.TestDataGenerator;
import util.logging.Log;

import static talkable.talkableSite.campaignsPage.Table.Status.DISABLED;
import static talkable.talkableSite.campaignsPage.Table.Status.LIVE;
import static talkable.userRegistration.chosePlatformPage.ChosePlatformPage.PlatformType.OTHER;

/*Class to allocate common scenarios in Talkable.
 * */
public class CommonScenarios {

    private static final String liveStatusActive = "Status: Live";
    private static final String liveStatusTest = "Status: Test";
    private static final String liveStatusDisabled = "Status: Disabled";

    /***
     *Scenario to login into Talkable
     * Precondition: Talkable home page should be opened
     * 1. Click 'Login' button --> Login page is opened
     * 2. Populate login and password and click Login
     * Post-condition: User logged to Talkable. Header should be available for further actions
     * */
    public static Header login(String email, String password) {
        HomePage homePage = new HomePage();
        homePage.clickLoginButton();
        return submitLoginForm(email, password);
    }

    /***
     *Scenario to login and create new Site. And create test account If it is not yet created on the env.
     * Precondition: Talkable home page should be opened
     * 1. Click 'Login' button --> Login page is opened.
     * 2. Populate login and password and click Login.
     * 3. Verify if user logged.
     *      If yes --> Create new Site.
     *      else --> register new account with site
     * Post-condition: User logged to Talkable. New site is created. Header should be available for further actions.
     * */
    public static Header loginAndCreateNewSite(String email, String password){
        //data:
        String defaultSite = "automation-site-" + TestDataGenerator.getRandomId();
        String defaultUrl = "www." + defaultSite + ".com";

        try{
            login(email, password);
            createNewSite(defaultSite, defaultUrl);
        }catch (AssertionError e){
            //verify error message:
            Assert.assertEquals(
                    new LoginPage().getTopErrorMessageString(),
                    "Email/Password combination is not valid",
                    "FAILED: Incorrect error message on the login page."
            );
            Log.logRecord("User <" + email + "> is not yet registered on <" + EnvFactory.getEnvType() + ">. Registration is started...");
            DriverConfig.getDriver().navigate().to(
                    EnvFactory.getRegistrationURL()
            );
            registerNewAccountWithSite(
                    email,
                    password,
                    defaultSite,
                    defaultUrl,
                    OTHER
            );
        }
        return new Header();
    }

    public static Header submitLoginForm(String email, String password){
        Header header = new LoginPage().submitLoginForm(email, password);
        Log.logRecord("User logged into Talkable. Email = <" + email + ">");
        return header;
    }

    public static void acceptCookiesUsage(){
        new WeUseCookieMsg()
                .accept();
    }

    public static void logout(){
        new Header().openMenu().clickLogout();
    }

    /***
     * Common scenario to create new campaign with default values
     * Precondition: Any page with header frame (class = Header()) should be opened
     * Post-condition: Site created. Site details page is opened
     * */
    public static IntegrationInstructionPage createNewSite(String siteName, String url) {
        Header header = new Header();
        AddSitePage addSitePage = header.openMenu().clickCreateNewSiteButton();
        IntegrationInstructionPage integrationInstructionPage = addSitePage.submitForm(siteName, url);
        Assert.assertEquals(getSiteNameFromHeader(), siteName);
        Log.logRecord("New site successfully created. Site name = " + siteName);
        return integrationInstructionPage;
    }

    public static void navigateToAdminUrl(){
        DriverConfig.getDriver().navigate().to(EnvFactory.getAdminUrl());
        new Header();
        Log.logRecord("Navigated to admin URL");
    }

    public static SiteDashboardPage switchToIntegratedSiteByVisibleText(String siteName) {
        new Header().selectByVisibleText(siteName);
        SiteDashboardPage siteDashboardPage = new SiteDashboardPage().verifySiteName(siteName);

        Assert.assertEquals(getSiteNameFromHeader(), siteName, "FAILED: Site is not switched");

        Log.siteSwitchedMsg(siteName);
        return siteDashboardPage;
    }

    public static SiteDashboardPage switchToSiteByVisibleText(String siteName){
        new Header().selectByVisibleText(siteName);
        try {
            return new SiteDashboardPage().verifySiteName(siteName);
        }catch (AssertionError e){
            Log.debagRecord("Site Dashboard is not opened when site is switched. Verifying  integrationPage instruction page...");
            new IntegrationInstructionPage().dontShowItAgain();
            SiteDashboardPage dashboardPage = new SiteDashboardPage().verifySiteName(siteName);
            Log.debagRecord("Integration instruction page is closed.");
            return dashboardPage;
        }
    }

    public static String getSiteNameFromHeader(){
        String name = new Header().getSiteName();
        Log.logRecord("Site name retrieved from Header : " + name);
        return name;
    }

    public static IntegrationInstructionPage switchToNonIntegratedSiteByVisibleText(String siteName) {
        new Header().selectByVisibleText(siteName);
        IntegrationInstructionPage integrationInstructionPage = new IntegrationInstructionPage();
        Assert.assertEquals(getSiteNameFromHeader(), siteName, "FAILED: Site is not switched");
        Log.siteSwitchedMsg(siteName);

        return integrationInstructionPage;
    }

    public static SiteDashboardPage openDashboardPage(){
        SiteDashboardPage page = new Header().openSiteDashboard();
        Log.logRecord("Site Dashboard page is opened.");
        return page;
    }

    public static SiteDashboardPage openDashboardPageForNonIntegratedSite(){
        new Header()
                .openSiteDashboardForNonIntegratedSite()
                .dontShowItAgain();
        return new SiteDashboardPage();
    }

    //method modified as temp workaround for old CSP:
    public static PersonLookupPage openCustomerServicePortal(){
        PersonLookupPage page = new Header().openCustomerServicePortal();
//        new Header().openOldCustomerServicePortal();
//        String oldCspUrl = DriverConfig.getDriver().getCurrentUrl();
//        String newCspUrl = oldCspUrl + "_portal";
//        DriverConfig.getDriver().navigate().to(newCspUrl);
//        PersonLookupPage page = new PersonLookupPage();
        Log.logRecord("New Customer Service Portal is opened. (Person Lookup page is displayed)");
        return page;
    }

    //correct method with method for

//    public static PersonLookupPage openCustomerServicePortal(){
//        PersonLookupPage page = new Header().openCustomerServicePortal();
//        Log.logRecord("Customer Service Portal is opened. (Person Lookup page is displayed)");
//        return page;
//    }




    public static PageCampaigns openCampaignsPage() {
        PageCampaigns page = new Header().openCampaignsPage();
        Log.logRecord("Campaigns Page is opened");
        return page;
    }

    public static String getCampaignsCountFromCampaignsPage(Table.Status status){
        try {
            return new PageCampaigns().getCampaignsTable(status).getCampaignsCount();
        }catch (AssertionError e){
            return "0";
        }
    }

    public static void openCampaignDetailsPage(){
        new CampaignNavigationMenu().openDetailsPage();
        Log.logRecord("Campaign Details page is opened.");
    }

    public static void openCampaignDetailsPageFor(String campaignName, Table.Status campaignStatus){
        PageCampaigns page = openCampaignsPage();
        page.openCampaignByName(campaignName, campaignStatus);
        Log.logRecord("Details page is opened for campaign <" + campaignName + "> with status = <" + campaignStatus + ">.");
    }

    public static void deleteCampaignFromDetailsPage(){
        new CampaignDetailsPage().delete();
        Log.logRecord("Campaign deleted");
    }


    public static PageCampaigns deactivateAllCampaigns() {
        return openCampaignsPage().deactivateAllLiveCampaigns();
    }

    public static PageCampaigns deleteAllCampaignsWithStatus(Table.Status status){
        return new Header().openCampaignsPage().deleteAllCampaignsWithStatus(status);
    }

    public static PageCampaignRules openCampaignRulesPage() {
        PageCampaignRules page = new CampaignNavigationMenu().openRulesPage();
        Log.logRecord("Campaign Rules page is opened");
        return page;
    }

    public static PageCampaignRules setCampaignNameOnRulesPage(String campaignName) {
        PageCampaignRules page = new PageCampaignRules().setCampaignName(campaignName);
        Assert.assertEquals(page.getCampaignName(), campaignName, "FAILED: Campaign name is not updated on Rules page");
        Log.logRecord("Campaign name changed to <" + campaignName + ">");
        return page;
    }

    public static void setCampaignNameAndDescriptionOnRulesPage(String cName, String cDescription){
        setCampaignNameOnRulesPage(cName);
        setCampaignDescriptionOnRulesPage(cDescription);
//        new PageCampaignRules().waitTillSaveMsgDisappeared();
    }

    public static void setCampaignDescriptionOnRulesPage(String campaignDesc){
        PageCampaignRules page = new PageCampaignRules().setCampaignDescription(campaignDesc);
        Assert.assertEquals(page.getCampaignDescription(), campaignDesc, "FAILED: Campaign description is not updated on Rules page");
        Log.logRecord("Campaign description changed to <" + campaignDesc + ">");
    }

    public static String getCampaignDescriptionOnRulesPage(){
        return new PageCampaignRules().getCampaignDescription();
    }

    public static void setDeadlinesOnRulesPage(String advocateOfferDeadlineDate, String adOfferEndTime, String friendDeadlineDate, String frOfferEndTime){
        PageCampaignRules rulesPage = new PageCampaignRules();
        rulesPage.setDeadlineDates(advocateOfferDeadlineDate, adOfferEndTime, friendDeadlineDate, frOfferEndTime);
        assertDeadlinesOnRulesPage(advocateOfferDeadlineDate, adOfferEndTime, friendDeadlineDate, frOfferEndTime);
        Log.logRecord("Friend deadline date and time have been changed to " + friendDeadlineDate + ", " + frOfferEndTime + "on Campaign Rules page");
//        new PageCampaignRules().waitTillSaveMsgDisappeared();
    }

    public static void assertDeadlinesOnRulesPage(String advocateOfferDeadlineDate, String adOfferEndTime, String friendDeadlineDate, String frOfferEndTime){
        PageCampaignRules rulesPage = new PageCampaignRules();
        Assert.assertEquals(rulesPage.getAdvocateDeadlineDate(), advocateOfferDeadlineDate, "FAILED: Incorrect Advocate Deadline Date on Campaign Rules page");
        Assert.assertEquals(rulesPage.getAdvocateDeadlineTime(), adOfferEndTime, "FAILED: Incorrect Advocate Deadline Time on Campaign Rules page");
        Log.logRecord("Advocate deadline date and time have been changed to " + advocateOfferDeadlineDate + ", " + adOfferEndTime + "on Campaign Rules page");

        Assert.assertEquals(rulesPage.getFriendDeadlineDate(), friendDeadlineDate, "FAILED: Incorrect Friend Deadline Date on Campaign Rules page");
        Assert.assertEquals(rulesPage.getFriendDeadlineTime(), frOfferEndTime,  "FAILED: Incorrect Friend Deadline Time on Campaign Rules page");
    }

    public static void switchUseFacebookAppIdCheckboxOnRulesPage(){
        new PageCampaignRules().switchUseFacebookAppIdCheckbox();
    }

    public static String getUseFacebookAppIdCheckboxValueFromRulesPage(){
        return new PageCampaignRules().getUseFacebookAppIdCheckbox();
    }

    public static void switchPlainTextVersionCheckboxOnRulesPage(){
        new PageCampaignRules().switchPlainTextVersionCheckbox();
    }

    public static String getPlainTextVersionCheckboxOnRulesPage(){
        return new PageCampaignRules().getPlainTextVersionCheckbox();
    }

    public static void switchRedirectOnExpiredClaimCheckboxOnRulesPage(){
        new PageCampaignRules().switchRedirectOnExpiredClaimCheckbox();
    }

    public static String getRedirectOnExpiredClaimCheckbox(){
        return new PageCampaignRules().getRedirectOnExpiredClaimCheckbox();
    }

    public static void addNewIncentive(PageCampaignRules.IncentiveType incentiveType,
                                          int rewardAmount,
                                          PageCampaignRules.DiscountType discountType,
                                          PageCampaignRules.CouponCodeType couponCodeType
                                          ){
        new PageCampaignRules()
                .createNewIncentive(
                        incentiveType,
                        rewardAmount,
                        discountType,
                        couponCodeType)
//                .waitTillSaveMsgDisappeared()
        ;
    }

    /***
     * Scenario to initiate campaign creation.
     * Precondition: Header should be available. Site should not have any active campaign with the selected campaignType/placementType
     * Post-condition: Campaign Details page of newly created campaign(as per input parameters)
     * */
    public static CampaignDetailsPage initiateCampaignCreation(CampaignType campaignType, CampaignPlacement placementType) {
        Header header = new Header();
        CreateNewCampaignPage createNewCampaignPage = header.openMenu().clickCreateNewCampaignButton();
        //Create New Campaign page:
        CampaignDetailsPage campaignDetailsPage = createNewCampaignPage.createCampaign(campaignType, placementType);
        //check Campaign Status
        Assert.assertEquals(campaignDetailsPage.campaignNavigationMenu.getCampaignStatus(), liveStatusTest);
        Log.logRecord("New campaign is created. Campaign type = <" + campaignType + ">, placement = <" + placementType + ">");
        return campaignDetailsPage;
    }

    /***
     * Scenario to initiate campaign creation from Campaigns Page adn return campaign name.
     * Precondition: Header should be available. Site should not have any active campaign with the selected campaignType/placementType
     * @return campaign name
     * Post-condition: Campaign Details page of newly created campaign(as per input parameters)
     * */
    public static String createNewCampaignFromCampaignsPage(CampaignType campaignType, CampaignPlacement placement){
        CampaignDetailsPage detailsPage = new PageCampaigns()
                .createNewCampaign(campaignType, placement);
        Log.logRecord("New campaign is created. Campaign type = <" + campaignType + ">, placement = <" + placement + ">");
        return detailsPage.campaignNavigationMenu.getCampaignName();
    }

    public static void openCampaignsPageAndCreateCampaign(CampaignType campaignType, CampaignPlacement placement){
        openCampaignsPage();
        createNewCampaignFromCampaignsPage(campaignType, placement);
    }

//    /***
//     * Scenario to initiate campaign creation from Campaigns Page..
//     * Precondition: Header should be available.
//     * Post-condition: Campaign Details page of newly created campaign(as per input parameters)
//     * */
//    public static CampaignDetailsPage initiateCampaignCreationFromCampaignsPage(CampaignType campaignType, CampaignPlacement placementType) {
//        return new Header().openCampaignsPage().createNewCampaign(campaignType, placementType);
//    }


    /***
     * Scenario to launch campaign
     * Precondition: initateCampaignCreation() should be already executed OR Page with Launch Button should be displayed
     * Post-condition: Campaign  status changed to Active. Campaign Details page is opened
     * */
    public static CampaignDetailsPage launchCampaign() {
        CampaignNavigationMenu campaignNavigationMenu = new CampaignNavigationMenu();
        LaunchCampaignPage launchCampaignPage = campaignNavigationMenu.clickLaunchButton();

        //Launch Campaign Page is opened
        CampaignDetailsPage campaignDetailsPage = launchCampaignPage.launchCampaign();
        //check Campaign Status
        return assertCampaignStatusFromNavigationMenu(LIVE);
    }

    public static void launchIntegratedCampaign(){
        new CampaignNavigationMenu()
                .clickLaunchButton()
                .launchIntegratedCampaign();
        assertCampaignStatusFromNavigationMenu(LIVE);
    }

    public static CampaignDetailsPage assertCampaignStatusFromNavigationMenu(Table.Status status){
        String expectedStatus = "";
        switch (status){
            case LIVE:
                expectedStatus = liveStatusActive;
                break;
            case DISABLED:
                expectedStatus = liveStatusDisabled;
                break;
            case TEST:
                expectedStatus = liveStatusTest;
                break;
        }
        Assert.assertEquals(new CampaignNavigationMenu().getCampaignStatus(), expectedStatus);
        Log.logRecord( "Campaign (name = " + new CampaignNavigationMenu().getCampaignName() + ") has status <" + expectedStatus + ">.");
        return new CampaignDetailsPage();
    }

    /***
     * Scenario to launch campaign from any page with CampaignNavigation menu
     * Post-condition: Details page of launched campaign.
     * */
    public static CampaignDetailsPage createAndLaunchCampaign(CampaignType campaignType, CampaignPlacement placementType) {
        createNewCampaignFromCampaignsPage(campaignType, placementType);
        try {
            // This part is added due to an existing defect. Error 500 is returned when campaign is launched directly after creation.
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return launchCampaign();
    }

    public static String getAdvocateOfferTotalCountFromCampaignDetailsPage(){
        return new CampaignDetailsPage().getAdvocateOffersTotalCount();
    }



    /***
     * Scenario to register new User and create new Site
     * Precondition: Register page should be opened(...talkable.com/register?object_or_array)
     * 1. Chose platform
     * 2. Populate registration form
     * 3. Verify that site is created.
     * Post-condition: IntegrationInstructionPage is opened.
     * Returns: user email.
     * */
    public static IntegrationInstructionPage registerNewAccountWithSite(String email, String password, String siteName, String siteUrl, ChosePlatformPage.PlatformType platformType) {
        CreateAccountPage createAccountPage = new ChosePlatformPage().selectPlatform(platformType);
        IntegrationInstructionPage integrationInstructionPage = createAccountPage.populateAndSubmitForm(email, password, siteName, siteUrl);
//        Verify that site is created:
//        IntegrationInstructionPage integrationInstructionPage = new IntegrationInstructionPage();
        Assert.assertEquals(getSiteNameFromHeader(), siteName);
        Log.logRecord("New Account is registered. User name = <" + email + ">. Site = <" + siteName + ">");

        return integrationInstructionPage;
    }

    /**
     * Scenarios to create Test Offer for campaign with Post Purchase placement.
     * Precondition: Campaign Details page should be opened.
     * 1. Click Create Test Offer button
     * 2. Click Create Origin button with default values on Create Test Offer page
     * 3. Switch back to Campaign Details page
     * Returns: Campaign Details Page for parent campaign.
     * */
    public static CampaignDetailsPage createTestOfferNewPurchase() {
        CampaignDetailsPage detailsPage = new CampaignDetailsPage();

        CreateNewPurchasePage createNewPurchasePage = detailsPage.clickCreateTestOfferForPostPurchase();
        createNewPurchasePage.createOfferAndSwitchToCampaign();
        Log.logRecord("Test offer created (New Purchase)");
        return new CampaignDetailsPage();
    }

    public static void copyCampaignFromDetailsPage(){
        new CampaignDetailsPage().copyCampaign();
        Log.logRecord("Campaign copied.");
    }

    public static void flushCampaignDataFromDetailsPage(){
        new CampaignDetailsPage().flushAllData();
    }

    public static String getCampaignNameFromNavigationMenu(){
        return new CampaignNavigationMenu().getCampaignName();
    }

    public static CampaignPlacement getCampaignPlacementFromNavigationMenu(){
        return new CampaignNavigationMenu().getCampaignPlacement();
    }

    public static void openSiteIntegrationPage(){
        new Header().openMenu().clickIntegration();
    }


    /**
     * Scenarios to create Test Offer for campaign with non Post Purchase placement (FW, SA, GR).
     * Precondition: Campaign Details page should be opened.
     * 1. Click Create Test Offer button
     * 2. Populate email on New Affiliated Member page
     * 3. Click Create Origin button
     * 4. Switch back to Campaign Details page
     * Returns: Campaign Details Page for parent campaign.
     * */
    public static CampaignDetailsPage createTestOfferNewAffiliateMember(String newAffiliatedMemberEmail) {
        CampaignDetailsPage detailsPage = new CampaignDetailsPage();
        PageNewAffiliateMember newAffiliateMember = detailsPage.clickCreateTestOfferNewAffiliateMember();

        newAffiliateMember.createMemberAndSwitchToCampaign(newAffiliatedMemberEmail);
        Log.logRecord("Test offer created (New Affiliate Member)");
        return new CampaignDetailsPage();
    }

    /**
     * Verification of Advocate Total Offers Count on Campaign Details page.
     * @expectedAdvocateOffersCount should be String value, e.g. "Total: 2"
    */
    public static void assertAdvocateOffersCountOnCampaignDetailsPage(String expectedAdvocateOffersCount){
        Assert.assertEquals(
                CommonScenarios.getAdvocateOfferTotalCountFromCampaignDetailsPage(),
                expectedAdvocateOffersCount,
                "FAILED: Incorrect Offers count on Campaign Details page");
    }


    /*Scenarios to deactivate campaign.
     * Precondition: Page with header should opened. Campaign with campaignName should be active(Live).
     * 1. Navigate to Campaigns page
     * 2. Search campaign by @campaignName
     * 3. Select campaign
     * 4. Deactivate campaign.
     * @Returns: Campaign Details Page deactivated campaign.
     * */
    public static CampaignDetailsPage deactivateCampaign(String campaignName) {
        PageCampaigns campaignsPage = new Header().openCampaignsPage();
        CampaignDetailsPage detailsPage = campaignsPage.openCampaignByName(campaignName, LIVE);
        CampaignNavigationMenu menu = detailsPage.campaignNavigationMenu.deactivateCampaign();
        Assert.assertEquals(menu.getCampaignStatus(), "Status: Disabled", "FAILED: Campaign is not deactivated");
        Log.logRecord("Campaign <" + campaignName + "> is deactivated");

        return new CampaignDetailsPage();
    }

    public static void reactivateCampaignFromCampaignsPage(String campaignName){
        new PageCampaigns()
                .openCampaignByName(campaignName, DISABLED)
                .campaignNavigationMenu.clickLaunchButton()
                .launchCampaign();
        Log.logRecord("Campaign <" + campaignName + "> is reactivated");
    }



    /*Scenarios to open Multi-Campaign Editor page for some campaign.
     * Precondition: Page with header should opened. Campaign with @campaignName should exist with defined @status.
     * 1. Navigate to Campaigns page
     * 2. Search campaign by @campaignName and @status
     * 3. Select campaign
     * 4. Navigate to Editor page.
     * 5. Select view by @pageViewName
     * 6. Select localization type by @contentType (COPY, IMAGE, CONFIGURATION or COLOR)
     * 7. Find localization by @localizationName and click 'Copy to Other Campaigns' button
     * @Returns: Multi-Campaign Editor page for mentioned parameters.
     * */
    public static PageMultiCampaignEditor openMultiCampaignEditorPage(String campaignName,
                                                                      Table.Status status,
                                                                      String pageViewName,
                                                                      String localizationName,
                                                                      SimpleEditorPage.LocalizationType contentType) {
        CampaignDetailsPage detailsPage = new Header().openCampaignsPage().openCampaignByName(campaignName, status);
        SimpleEditorPage editor = detailsPage.campaignNavigationMenu.openEditorPage();
        editor = editor.switchViewByNameOnSimpleEditor(pageViewName);
        editor.switchTo(contentType);
        PageMultiCampaignEditor mceEditor =  editor.clickCopyToOtherCampaigns(contentType, localizationName + "#");
        Log.logRecord("Multi-Campaign Editor page is opened for campaign = <" + campaignName + ">, localization = <" + contentType + " --> " + localizationName + ">.");
        return mceEditor;
    }

    public static String getCampaignStatusFromMenu(){
        return new CampaignNavigationMenu().getCampaignStatus();
    }




    public static void verifyPagination(Pagination pagination) {
        int page = 1;
        String current = pagination.getCurrentPage();
        Assert.assertEquals(current, String.valueOf(page), "FAILED: Pagination: First page is not opened");

        pagination.next();
        page++;
        current = pagination.getCurrentPage();
        Assert.assertEquals(current, String.valueOf(page), "FAILED: Pagination: Incorrect page value after next() method");

        pagination.first();
        page = 1;
        current = pagination.getCurrentPage();
        Assert.assertEquals(current, String.valueOf(page), "FAILED: Pagination: Incorrect page value after first() method");

        pagination.last();
        current = pagination.getCurrentPage();

        pagination.previous();
        Assert.assertNotEquals(pagination.getCurrentPage(), current, "FAILED: Pagination: page is not switched after previous() method");

        pagination.last();
        pagination.first();
        page = 1;
        current = pagination.getCurrentPage();
        Assert.assertEquals(current, String.valueOf(page), "FAILED: Pagination: Incorrect page value after first() method");

        Log.testPassed("Pagination successfully verified");
    }

    public static SiteSettingsBasicTab openSiteSettingsPage(){
        SiteSettingsBasicTab page = new Header().openMenu().clickSiteSettings();
        Log.logRecord("Site Settings page is opened (Basic Tab)");
        return page;
    }

// updateSiteSettingsBasicTab

    public static void updateSiteSettingsBasicTab(String siteName, String siteID, String siteURL, String platform, String currency ){
        new SiteSettingsBasicTab().updateAll(siteName, siteID, siteURL, platform, currency);
        Log.logRecord("Site Settings Basic Tab updated");

    }

    public static void setUrlAndPlatformOnSiteSettings(String url, String platform){
        String currentUrl = new SiteSettingsBasicTab().getSiteURL();
        String currentPlatform = new SiteSettingsBasicTab().getPlatform();
        if(!currentUrl.equals(url) || !currentPlatform.equals(platform)) {
            new SiteSettingsBasicTab()
                    .populateUrl(url)
                    .selectPlatform(platform)
                    .updateChanges();
        }else {
            Log.logRecord("Site platform and url have been already populated by values: platform = <" + platform + ">, url = <" + url + ">");
        }
    }

    public static void populateSiteBasicNegativeTest(String siteName, String siteID, String siteURL, String platform){
        new SiteSettingsBasicTab().populate(siteName, siteID, siteURL);
        new SiteSettingsBasicTab().selectPlatform(platform);
        new SiteSettingsBasicTab().clickSaveChanges();

        Log.logRecord("Site Settings Basic Tab populated");
    }
    public static void assertErrorMsgSiteSettigsBasicTab(String siteName, String siteID, String siteURL){
        Assert.assertEquals(
                new SiteSettingsBasicTab().getSiteName(),
                siteName,
                "FAILED: Site settings/basic tab/Incorect Site Name");
        Assert.assertEquals(
                new SiteSettingsBasicTab().getSiteID(),
                siteID,
                "FAILED: Site settings/basic tab/Incorect Site ID");
        Assert.assertEquals(
                new SiteSettingsBasicTab().getSiteURL(),
                siteURL,
                "FAILED: Site settings/basic tab/Incorect Site URL");
    }
    public static void assertErrorMsgSiteSettigsBasicTab(String expectedErrorMsg){
        Assert.assertEquals(
                new SiteSettingsBasicTab().getErrorMsg(),
                expectedErrorMsg,
                "FAILED: Site settings/basic tab/Validation Failed"+expectedErrorMsg);
        new SiteSettingsBasicTab().clickCancel();
    }
// updateSiteSettingsContactTab
    public static void populateSiteSettingsContactsTab(String csEmail){
    new SiteSettingsContactsTab().populate(csEmail);
    Log.logRecord("Site Settings Contacts CS Email populated");
    }
    public static void updateSiteSettingsContactsTab(String csEmail, String csName, String technicalEmail, String rewardEmail, String marketerEmail ){
        new SiteSettingsContactsTab().updateAll( csEmail,  csName,  technicalEmail,  rewardEmail,  marketerEmail );
        Log.logRecord("Site Settings Contacts updated");
    }


// SiteSettings-Unsaved Changes Popup
    public static void swithUnsavedTab(SiteSettingsPage.SiteSettingsTab tab){
        new SiteSettingsPage().switchTabWithUnsavedChanges(tab);
        Log.logRecord("Popup Unsaved Changes Displayed");
    }
    public static void saveUnsavedchanges(){
    new ElmntUnsavedChangesPopup().saveChanges();
    }
    public static void canselUnsavedchanges(){
        new ElmntUnsavedChangesPopup().cancelChanges();
    }
    public static SiteSettingsContactsTab discardUnsavedchanges(){
        new ElmntUnsavedChangesPopup().discardChanges();
        Log.logRecord("Site Settings Contacts changes discarded");
        return new SiteSettingsContactsTab();

    }
    public static String getSiteCSemail(){
        return new SiteSettingsContactsTab().getCSEmail();
    }
    public static void assertDiscardedChangesSiteSettigsContactsTab(String expectedEmail) {
        Assert.assertEquals(
                new SiteSettingsContactsTab().getCSEmail(),
                expectedEmail,
                "FAILED: Site settings/Contacts tab/Discard changes");
    }
// SiteSettings-Unsaved Changes Popup

    public static Site getSiteIntegrationValues(){
        SiteSettingsBasicTab basicTab = openSiteSettingsPage();
        String siteID = basicTab.getSiteID();
        String apiKey = basicTab
                .openIntegrationSettingsTab()
                .getApiKey();
        return new Site().setData(siteID, apiKey);
    }


    public static IntegrationPage installShopifyApp(String shopifyUser, String passwrd){
        IntegrationPage page = new IntegrationPage()
                .installShopifyApp()
                .enterCredentialToIntegrateTalkable(shopifyUser, passwrd);
        Assert.assertEquals(
                page.getWelcomeMsg(),
                "Integration Is Completed",
                "FAILED: Welcome Integration message is not displayed on Integration page when shopify app was installed.");
        Log.logRecord("Shopify App is installed.");
        return page;
    }



}
