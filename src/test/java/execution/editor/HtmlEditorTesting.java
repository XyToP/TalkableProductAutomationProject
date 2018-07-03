package execution.editor;

import common.cases.CommonScenarios;
import common.cases.functionalities.EditorScenarios;
import execution.BaseTest;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import talkable.common.elements.alert.Alert;
import talkable.talkableSite.campaign.pages.editorPage.CreateNewViewPage;
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

	/*Link to test scenario: https://docs.google.com/spreadsheets/d/148sQEgtaeSuPrwor1s5HIFFqrgG4QEFR2_Gi2K3-r5k/edit#gid=0
     * */
public class HtmlEditorTesting extends BaseTest {

    private String user = PropertyLoader.loadProperty("talkable.user.editor");
    private String pswrd = EnvFactory.getPassword();
    private String campaignDetailsPageUrl;


    @BeforeGroups("ui-actions")
    public void precondition(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.login(user, pswrd);
        CommonScenarios.openCampaignsPage();
        CommonScenarios.deleteAllCampaignsWithStatus(TEST);
        CommonScenarios.createNewCampaignFromCampaignsPage(Invite, Standalone);
        campaignDetailsPageUrl = driver.getCurrentUrl();
    }


    //Pending defect --> https://talkable.atlassian.net/browse/PR-9495
    /*Scenarios1*/
    @Test(groups = {"ui-actions"})
    public void creteNewView(){
        String viewType = "Advocate Offer Email";
        String viewNameInHtmlEditor = "Advocate offer email";

        driver.navigate().to(campaignDetailsPageUrl);
        EditorScenarios.openHtmlEditor();
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

    //todo: remove expectedException when the defect fixed.
    // Blocked by the Defect: https://talkable.atlassian.net/browse/PR-9486
    /*Scenarios2*/
    @Test(groups = {"ui-actions"}, expectedExceptions = AssertionError.class)
    public void deleteView(){
        String viewName = "Advocate share page";

        driver.navigate().to(campaignDetailsPageUrl);
        EditorScenarios.openHtmlEditor();
        EditorScenarios.deleteView(viewName);

        Assert.assertEquals(
                EditorScenarios.isViewPresent(viewName),
                false,
                "FAILED: View was not deleted."
        );
    }


}
