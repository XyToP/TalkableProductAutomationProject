
import common.cases.CommonScenarios;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import talkable.talkableSite.campaign.pages.editorPage.EditorPage;
import util.DriverConfig;

import static talkable.talkableSite.campaign.pages.editorPage.EditorPage.LocalizationMode.CONFIGURATION;
import static talkable.talkableSite.campaign.pages.editorPage.EditorPage.LocalizationMode.COPY;
import static talkable.talkableSite.campaign.pages.editorPage.EditorPage.LocalizationMode.IMAGES;


public class FirstTest {

    private WebDriver driver;

    private String siteName = "automationSite";

    @BeforeClass
    public void setup(){
        driver = new DriverConfig().getDriver();
        driver.navigate().to("https://void.talkable.com");
    }

    @Test
    public void test1_login(){
        CommonScenarios.login("maxim.laba@talkable.com", "Password@1");

        driver.navigate().to("https://admin.void.talkable.com/sites/simple-test/campaigns/45595/editor#/view_setups/150283/preset_slug/default-preset");
    }

    @Test
    public void test2() {
        EditorPage editorPage = new EditorPage();
        editorPage.switchTo(CONFIGURATION);
        editorPage.switchTo(IMAGES);
        editorPage.switchTo(COPY);
        editorPage = editorPage.switchViewByName("Advocate social sharing");
        editorPage.updateLocalization(COPY, "Facebook share title#", "test");

    }


}
