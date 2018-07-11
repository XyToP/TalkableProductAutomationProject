package execution.editor;

import common.cases.CommonScenarios;
import common.cases.functionalities.EditorScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import talkable.talkableSite.campaign.pages.editorPage.SimpleEditorPage;
import talkable.talkableSite.campaignsPage.Table;
import util.EnvFactory;
import util.PropertyLoader;
import util.TestDataGenerator;
import util.logging.Log;

import static talkable.common.CampaignPlacement.Standalone;
import static talkable.common.CampaignType.Invite;
import static talkable.talkableSite.campaign.pages.editorPage.SimpleEditorPage.LocalizationType.*;

/*Link to test scenario: https://docs.google.com/spreadsheets/d/1jjxHr_cLNaSq3HVBgU_y6k_llNDpNyTZUoWPAJ9Aw20/edit
 * */

public class SimpleEditorTesting_v2 extends BaseTest {

    private static final String siteName = PropertyLoader.loadProperty("sites.name.editorTesting");
    private String campaignDetailsPageUrl;
    private String user = EnvFactory.getCommonUser();
    private String pswrd = EnvFactory.getPassword();

    @Test
    public void loginAndCreateNewCampaign(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.loginAndCreateNewSite(user, pswrd);
//        CommonScenarios.login(EnvFactory.getCommonUser(), EnvFactory.getPassword());
//        CommonScenarios.switchToSiteByVisibleText(siteName);
//        CommonScenarios.openCampaignsPage();
//        CommonScenarios.deleteAllCampaignsWithStatus(Table.Status.TEST);
        CommonScenarios.openCampaignsPage();
        CommonScenarios.createNewCampaignFromCampaignsPage(Invite, Standalone);
        campaignDetailsPageUrl = driver.getCurrentUrl();
    }

    @Test(dataProvider = "editContentTestData", dependsOnMethods = "loginAndCreateNewCampaign")
    public void updateContent(SimpleEditorPage.LocalizationType type, String view, String localizationName, String newValue){
        Log.logRecord("\r\nSimple Editor test started for <" +type + "." + localizationName + ">.");
        //open CampaignDetails page
        driver.navigate().to(campaignDetailsPageUrl);

        EditorScenarios.openSimpleEditor();
        EditorScenarios.updateLocalization(type, view, localizationName, newValue);
        Assert.assertEquals(
                EditorScenarios.getLocalizationValue(type, localizationName),
                newValue,
                "FAILED: Localization <" +type + "." + localizationName + "> is not updated to value: " + newValue + ".");
        Log.testPassed("Value is updated in Simple Editor. <" +type + "." + localizationName + ">.\r\n");
    }

    @Test(dependsOnMethods = "loginAndCreateNewCampaign"
            , expectedExceptions = AssertionError.class) //scenarios is failed due to the defect https://talkable.atlassian.net/browse/PR-9523
    public void createNewPreset(){
        String presetName = "testPreset" + TestDataGenerator.getRandomId();
        driver.navigate().to(campaignDetailsPageUrl);
        EditorScenarios.openSimpleEditor();
        EditorScenarios.createNewPreset(presetName, " ");
        EditorScenarios.deletePresetOnHtmlEditor(presetName);
        Assert.assertEquals(
                EditorScenarios.isPresetPresent(presetName),
                false,
                "FAILED: View Preset is not deleted on Editor (Preset name = <" + presetName + ">)."
        );
        Log.testPassed("Create new Preset Test. Preset was created and removed.");
    }



//    @Test(dependsOnMethods = {
//            "loginAndCreateNewCampaign",
//            "updateContent",
//            "createNewPreset"
//    },
//            alwaysRun = true)
//    public void cleanUpTestData(){
//        driver.navigate().to(campaignDetailsPageUrl);
//        CommonScenarios.deleteCampaignFromDetailsPage();
//    }


    @DataProvider
    public Object[][] editContentTestData() {
        return new Object[][]{
                {COPY, "Advocate signup page",  "Advocate signup page preheader#", "Updated value"},
                {COLOR, "Advocate signup page", "Advocate signup page cta background#", "#0fef4e"},
                {CONFIGURATION, "Advocate signup page", "Advocate signup button corners#", "Circle"},
                {IMAGES, "Advocate signup page", "Advocate signup page background#", "tkbl_default_example.jpg"},
                {COPY, "Friend share email", "Friend share email copy#", "Updated value for auto-test"}
        };
    }

}
