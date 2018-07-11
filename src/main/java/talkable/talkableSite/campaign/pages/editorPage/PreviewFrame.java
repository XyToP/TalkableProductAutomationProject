package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.AbstractTalkableFrame;
import abstractObjects.Element;
import org.openqa.selenium.By;

class PreviewFrame extends AbstractTalkableFrame{

    private static final By frameLocator = By.xpath("//iframe");

    PreviewFrame(){
        setWebElement(frameLocator);
        switchToThisFrame();
        switchToParentFrame();
    }


    public String getElementText(By locator){
        switchToThisFrame();
        String out = new Element(locator).getText();
        switchToParentFrame();
        return out;
    }


}
