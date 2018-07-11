package execution.registration;

import common.cases.CommonScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import talkable.talkableSite.IntegrationInstructionPage.IntegrationInstructionPage;
import util.EnvFactory;
import util.TestDataGenerator;

import static talkable.userRegistration.chosePlatformPage.ChosePlatformPage.PlatformType.OTHER;

/*Link to test scenario: https://docs.google.com/spreadsheets/d/1VtMlmQDO77F5gGKOBBEuBN9hVyXBsnp3jD5u-v3z5Zk/
 * */
public class CreateNewUserWithSite extends BaseTest{

    private static final String email = "maxim.laba+auto.test" + TestDataGenerator.getRandomId() + "@talkable.com";
    private static final String password = "Password@1";
    private static final String siteName = "autoSite" + TestDataGenerator.getRandomId();
    private static final String siteUrl = "https://auto.test.site.com";


    @BeforeClass
    public void setup() {
        this.driver.navigate().to(EnvFactory.getRegistrationURL());
        CommonScenarios.acceptCookiesUsage();
    }

    //Select Custom platform
    //Populate user registration form
    //Submit form
    //Check that new site is created
    @Test
    public void test1_register(){
        IntegrationInstructionPage page = CommonScenarios.registerNewAccountWithSite(email, password, siteName, siteUrl, OTHER);
        Assert.assertEquals(page.header.getSiteName(), siteName);
    }

}
