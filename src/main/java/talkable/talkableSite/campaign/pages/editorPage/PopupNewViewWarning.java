package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;

class PopupNewViewWarning extends AbstractElementsContainer{

    private static final By proceedBtnLctr = By.xpath("//div[text()='Proceed']");

    private Element proceedBtn = new Element(proceedBtnLctr, "'Proceed' button");

    CreateNewViewPage proceedInWarningPopup(){
        proceedBtn.click();
        return new CreateNewViewPage();
    }
}
