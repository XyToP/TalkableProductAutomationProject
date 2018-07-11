package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.common.elements.alert.Alert;
import util.logging.Log;

public class HtmlEditorPage extends AbstractEditorPage{

    private static final By extraBtnLctr = By.xpath("//*[@data-editor-toggle = 'extra-fields']");
    private static final By viewDestroyedMcsgLctr = By.xpath("//div[contains(text(), 'destroyed')]");
    private static final By cssPanelTitleLctr = By.xpath("//div[@class='editor-panel-tip-title']");
    private static final By emailSubjectInExtraPopup = By.xpath("//textarea[@name='view_setup[caption]']");
    private static final By saveChangesBtnInExtaLctr = By.xpath("//*[contains(@class,'editor-popup')]/div[text() = 'Save changes']");

    private Element extraBtn = new Element(extraBtnLctr, "Extra button");

    public HtmlEditorPage(){
        //elements initiation to make sure that Html Editor is opened.
        new Element(cssPanelTitleLctr);
    }

    public HtmlEditorPage switchViewByNameOnHtmlEditor(String name){
        switchViewByName(name);
        return new HtmlEditorPage();
    }

    public CreateNewViewPage clickCreateNewView(){
        return openViewList()
                .clickCreateNewView()
                .proceedInWarningPopup();
    }

    public HtmlEditorPage deleteView(String viewName){
        openViewList()
                .findViewRecord(viewName)
                .delete();
        new Alert().confirm();
        waitViewDestroyedMsg();
        return new HtmlEditorPage();
    }

    public void waitViewDestroyedMsg(){
        new Element(viewDestroyedMcsgLctr);
    }

    public HtmlEditorPage updateExtraEmailSubject(String newSubject){
        extraBtn.click();
        new Element(emailSubjectInExtraPopup, "Email Subject in Extra")
                .clearAndSendKeys(newSubject);
        HtmlEditorPage page = saveChangesInExtraPopup();
        Log.logRecord("Email Subject updated in HTML Editor --> Extra popup. ");
        return page;
    }

    private HtmlEditorPage saveChangesInExtraPopup(){
        new Element(saveChangesBtnInExtaLctr, "Save changes button in Extra popup")
                .click();
        waitSaving();
        extraBtn.click();
        return new HtmlEditorPage();
    }




}
