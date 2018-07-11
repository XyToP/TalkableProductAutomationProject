package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.AbstractElementsContainer;

import abstractObjects.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class CreateNewViewPage extends AbstractElementsContainer{

    private static final By viewTypeSelectLctr = By.xpath("//select[@name='view_setup[view_category]']");
    private static final By createBtnLctr = By.xpath("//input[@name='commit']");

    private Element createBtn = new Element(createBtnLctr, "Create button");

    public HtmlEditorPage createNewView(String viewType){
        selectViewType(viewType);
        createBtn.click();
        return new HtmlEditorPage();
    }

    private void selectViewType(String viewType){
        new Select(new Element(viewTypeSelectLctr).getWebElement())
                .selectByVisibleText(viewType);
    }


}
