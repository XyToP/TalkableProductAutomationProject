package execution.site.settings;

import common.cases.CommonScenarios;
import execution.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import talkable.talkableSite.siteSettings.SiteSettingsPage;
import util.EnvFactory;
import util.PropertyLoader;
import util.TestDataGenerator;
import util.logging.Log;

public class SiteSettingsContactsTab extends BaseTest {

    @BeforeClass
    public void login(){
        String email = PropertyLoader.loadProperty("talkable.user.siteSettings");
        String password = EnvFactory.getPassword();

        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.login(email, password );
    }
    //todo: test contacts
    @Test
    public void updateSiteSettingsContactEmail(){
        String csEmail = "examp"+ TestDataGenerator.getRandomId()+"le@cc.cc";
        String csName = "NAME";
        String technicalEmail = "techikal"+csEmail;
        String rewardEmail = "reward"+csEmail;
        String marketerEmail = "marketer"+csEmail;
        CommonScenarios.openSiteSettingsPage().openContactsTab();
        CommonScenarios.updateSiteSettingsContactsTab(csEmail,  csName,  technicalEmail,  rewardEmail,  marketerEmail );
        Log.testPassed("Site setting updated");
    }
    @Test
    public void discardSiteSettingsContactChanges(){

        String csEmail = "cs_email@dot.com";

        CommonScenarios.openSiteSettingsPage().openContactsTab();
        String expectedEmail = CommonScenarios.getSiteCSemail();
        CommonScenarios.populateSiteSettingsContactsTab(csEmail);
        CommonScenarios.swithUnsavedTab(SiteSettingsPage.SiteSettingsTab.BASIC);
        CommonScenarios.discardUnsavedchanges();

        CommonScenarios.assertDiscardedChangesSiteSettigsContactsTab(expectedEmail);
        Log.testPassed("Test PAASED");
    }
}
