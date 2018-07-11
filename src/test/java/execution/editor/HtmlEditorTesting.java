package execution.editor;

import common.cases.CommonScenarios;
import common.cases.functionalities.EditorScenarios;
import execution.BaseTest;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import talkable.common.elements.alert.Alert;
import talkable.talkableSite.campaign.pages.detailsPage.CampaignDetailsPage;
import talkable.talkableSite.campaign.pages.editorPage.CreateNewViewPage;
import talkable.talkableSite.campaign.pages.editorPage.HtmlEditorPage;
import util.EnvFactory;
import util.PropertyLoader;
import util.logging.Log;

import static talkable.common.CampaignPlacement.Standalone;
import static talkable.common.CampaignType.Invite;
import static talkable.talkableSite.campaignsPage.Table.Status.TEST;


/** Common Precondition:
 *      1. Accept Cookies Usage.
 *      2. Login
 *      3. Open Campaigns page.
 *      4. Deleted campaigns from the previous execution.
 *      5. Create New Campaign.
 *      6. Get link to campaign Details
 *
 * Scenario#1. Create new view (Advocate Offer Email).
     * 1. Open campaign details.
     * 3. Open Html Editor
     * 4. Create new View
     * 5. Verify new preset in the views list
     *
 *
 *    Scenario2. Delete view.
 *    1. Open campaign details.
 *    2. Open HTML Editor.
 *    3. Delete view.
 *    4. Verify that view is not present in the dropdown list
 *    */

/*    Scenario3. Update email subject in Extra
        1. Open campaign details
        2.Open HTML Editor.
        3. Switch view to 'Friend share email'
        4. Open Extra popup.
        5. Update Email Subject
        6. Verify Email Subject in the preview frame.
        */


	/*Link to test scenario: https://docs.google.com/spreadsheets/d/148sQEgtaeSuPrwor1s5HIFFqrgG4QEFR2_Gi2K3-r5k/edit#gid=0
     * */
public class HtmlEditorTesting extends BaseTest {

    private String user = PropertyLoader.loadProperty("talkable.user.editor");
    private String pswrd = EnvFactory.getPassword();
    private String campaignDetailsPageUrl;


    @BeforeGroups("ui-actions")
    public void precondition(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.loginAndCreateNewSite(user, pswrd);
        CommonScenarios.openCampaignsPage();
        CommonScenarios.createNewCampaignFromCampaignsPage(Invite, Standalone);
        campaignDetailsPageUrl = driver.getCurrentUrl();
    }

    @BeforeMethod
    public void openCampaignDetailsPage(){
        try {
            driver.navigate().to(campaignDetailsPageUrl);
            new CampaignDetailsPage();
        }catch (UnhandledAlertException e){
            new Alert().confirm();
            openCampaignDetailsPage();
        }

        //debag part:
        try{
            new Alert().confirm();
            Log.debagRecord("Alert found and closed on Campaign details page during @BeforeMethod execution.");
        }catch (TimeoutException e){
            Log.debagRecord("Alert was not shown on Campaign details page during @BeforeMethod execution.");
        }
        //end

        Log.logRecord("Navigated to Campaign Details page URL: " + campaignDetailsPageUrl);
    }



    //Pending defect --> https://talkable.atlassian.net/browse/PR-9495
    /*Scenarios1*/
    @Test(groups = {"ui-actions"})
    public void creteNewView(){
        String viewType = "Advocate Offer Email";
        String viewNameInHtmlEditor = "Advocate offer email";

        EditorScenarios.openHtmlEditor();
        //todo: try{} should be removed when the defect PR-9495 is fixed.
        try {
            EditorScenarios.createNewView(viewType);
        }catch (UnhandledAlertException e){
            Log.debagRecord(" UnhandledAlertException is returned on EditorScenarios.createNewView().");
            new Alert().confirm();
            new CreateNewViewPage().createNewView(viewType);
        }
        Assert.assertEquals(
                EditorScenarios.getSelectedView(),
                viewNameInHtmlEditor,
                "FAILED: Newly created view <" + viewNameInHtmlEditor + "> is not selected."
        );
    }


    // Blocked by the Defect: https://talkable.atlassian.net/browse/PR-9495
    /*Scenarios2*/
    @Test(groups = {"ui-actions"})
    public void deleteView(){
        String viewName = "Advocate share page";

        EditorScenarios.openHtmlEditor();
        //todo: try{} should be removed when the defect PR-9495 is fixed.
        try {
            EditorScenarios.deleteView(viewName);

        }catch (UnhandledAlertException e){
            Log.debagRecord(" UnhandledAlertException is returned on EditorScenarios.deleteView().");
            new Alert().confirm();
            new HtmlEditorPage().waitViewDestroyedMsg();
        }

        Assert.assertEquals(
                EditorScenarios.isViewPresent(viewName),
                false,
                "FAILED: View was not deleted."
        );
    }


    //Pending defect --> https://talkable.atlassian.net/browse/PR-9495
    /*Scenario3*/
    @Test(groups = {"ui-actions"})
    public void updateEmailSubjectInExtraPopup(){
        String viewName = "Friend share email";
        String newSubject = "Automation test email subject";

        EditorScenarios.openHtmlEditor();
        //todo: try{} should be removed when the defect PR-9495 is fixed.
        try {
            EditorScenarios.switchViewByName(viewName);
        }catch (UnhandledAlertException e){
            Log.debagRecord(" UnhandledAlertException is returned on EditorScenarios.updateEmailSubjectInExtraPopup().");
            new Alert().confirm();
        }
        EditorScenarios.updateExtraEmailSubject(newSubject);
        Assert.assertEquals(
                EditorScenarios.getEmailSubjectFromPreview(),
                newSubject,
                "FAILED: Email subject is not updated in the HTML Editor Preview frame."
        );

    }


}
