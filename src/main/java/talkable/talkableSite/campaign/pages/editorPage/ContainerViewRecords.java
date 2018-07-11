package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import util.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ContainerViewRecords extends AbstractElementsContainer{

    private static final By containerElmntLctr = By.xpath("//ul[contains(@class, 'ac-editor-widget-navigation')]");
    private static final By recordsLctr = By.xpath("./div/li");
    private static final By createNewViewLctr = By.xpath("//a[contains(@class, 'create-new-view')]");

    private Element containerElement = new Element(containerElmntLctr);
    private ArrayList<ElmntViewRecord> records;


    ContainerViewRecords(){
        records = getRecords();
    }

    private ArrayList<ElmntViewRecord> getRecords(){
        List<WebElement> webElements = containerElement.getWebElement().findElements(recordsLctr);
        ArrayList<ElmntViewRecord> arr = new ArrayList<>();
        for (WebElement webElement :
                webElements) {
            ElmntViewRecord viewRecord = new ElmntViewRecord(new Element(webElement));
            arr.add(viewRecord);
        }
        return arr;
    }

    public void selectViewByText(String viewName){
        Objects.requireNonNull(findViewRecord(viewName)).viewName.click();
    }

    ElmntViewRecord findViewRecord(String name){
        for (ElmntViewRecord record : getRecords()) {
            if(record.getViewName().equals(name)){
                return  record;
            }
        }
        Assert.fail("FAILED: There is no view records with name : <" + name + ">");
        return null;
    }

    boolean isViewPresent(String name){
        for (ElmntViewRecord record : records) {
            if(record.getViewName().equals(name)){
                return  true;
            }
        }
        return false;
    }

    PopupNewViewWarning clickCreateNewView(){
        new Element(createNewViewLctr, "Create New View Btn").click();
        return new PopupNewViewWarning();
    }


    class ElmntViewRecord extends AbstractElementsContainer{

        private By viewNameLctr = By.xpath(".//span[contains(@class, 'editor-menu-text')]");
        private By isVisibleLctr = By.xpath(".//input[contains(@type, 'checkbox')]");
        private By deleteIconLctr = By.xpath(".//a[@data-method='delete']");

        private Element rowElement;
        private Element viewName;

        private ElmntViewRecord(Element rowElement){
            this.rowElement = rowElement;
            viewName = new Element(rowElement, viewNameLctr);
        }

        void clickIsVisible(){
            new Element(
                    rowElement.getWebElement().findElement(isVisibleLctr))
                    .click();
        }

        private String getViewName(){
            return new Element(rowElement, viewNameLctr).getText();
        }

        void delete(){
            new Element(rowElement, deleteIconLctr).click();
        }


    }



}
