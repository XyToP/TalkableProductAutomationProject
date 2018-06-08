package talkable.talkableSite.campaign.pages.editorPage;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;

public class ViewPresetFrame extends AbstractElementsContainer{
    private final By presetNamesLctr = By.xpath("//*[contains(@class, 'presets-link')]/..");

    private Element createNewPresetBtn = new Element(By.xpath("//*[contains(text(), 'Create new preset')]"), "<Create New Preset>");


    PresetRow findPresetByName(String name){
        for (PresetRow row :
                getRows()) {
            if (row.getPresetName().equals(name)){
                return row;
            }
        }
        Assert.fail("FAILED: View Preset with name <" + name + "> is not found in Editor page --> Presets list");
        return null;
    }

    public CreateNewPresetPage clickCreateNewPreset(){
        createNewPresetBtn.click();
        return new CreateNewPresetPage();
    }

    private ArrayList<PresetRow> getRows(){
        ArrayList<PresetRow> rows = new ArrayList<>();
        for (Element element :
                getElementsList(presetNamesLctr)) {
            rows.add(new PresetRow(element));
        }
        return rows;
    }



    class PresetRow {

        private final By nameLctr = By.xpath(".//*[contains(@class, 'presets-link')]");
        private final By editBtn = By.xpath(".//a[contains(@class, 'trash')]");
        private Element parentElement;

        private PresetRow(Element parent){
            parentElement = parent;
            getNameElement();
        }

        private Element getNameElement(){
            return new Element(parentElement.getWebElement().findElement(nameLctr));
        }

        private Element getDeleteBtnElement() {
            if(getNameElement().getText().equals("Default Preset")){
                Assert.fail("FAILED: 'Default Preset' can not be deleted.");
            }
            return new Element(parentElement.getWebElement().findElement(editBtn));
        }

        String getPresetName(){
            return getNameElement().getText();
        }

        void select(){
            getNameElement().click();
        }

        void deletePreset(){
            getDeleteBtnElement().click();
        }

    }



}
