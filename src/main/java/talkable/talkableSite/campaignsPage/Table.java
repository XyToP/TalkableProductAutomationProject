package talkable.talkableSite.campaignsPage;

import abstractObjects.AbstractElementsContainer;
import talkable.common.elements.alert.Alert;
import abstractObjects.Element;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class Table extends AbstractElementsContainer {
    private static final By disabled = By.xpath("//h2[contains(text(), 'Disabled')]/following::table[1]");
    private static final By live = By.xpath("//h2[contains(text(), 'Live')]/following::table[1]");
    private static final By test = By.xpath("//h2[contains(text(), 'Test')]/following::table[1]");

    private Status status;
    private ArrayList<Row> table = new ArrayList<>();

    public enum Status{LIVE, TEST, DISABLED}

    Table(Status status) {
        WebElement tableElement = setTableWebElement(status);
        assert tableElement != null;
        List<WebElement> allRows = tableElement.findElements(By.xpath("./tbody/tr"));

        this.status = status;
        for (WebElement webElement :
                allRows) {
            this.table.add(new Row(webElement));
        }
    }


    private WebElement setTableWebElement(Status status){
        switch (status){
            case LIVE:
                return driver.findElement(live);
            case TEST:
                return driver.findElement(test);
            case DISABLED:
                return driver.findElement(disabled);
            default:
                Assert.fail("FAILED: Unknown campaign status: " + status.toString());
                return null;
        }
    }

    public Status getStatus() {
        return status;
    }

    public String getCampaignType(int rowNumber) {
        return this.table.get(rowNumber).type.getText();
    }

    public String getCampaignName(int rowNumber) {
        return this.table.get(rowNumber).name.getText();
    }

    public String getCampaignId(int rowNumber) {
        return this.table.get(rowNumber).id.getText();
    }

    public String getCampaignOffers(int rowNumber) {
        return this.table.get(rowNumber).offers.getText();
    }

    public Row getRowByCampaignName(String campaignName){
        Row out = null;
        for (Row row :
                this.table) {
            if (row.name.getText().equals(campaignName)) {
                out = row;
            }
        }
        out = isNull(out, campaignName);
        return out;
    }

    private Row isNull(Row row, String campaignName){
        if(row == null){
            Assert.fail("Campaign with name <" + campaignName + "> is not found");
            return row;
        }
        return row;
    }

    ArrayList<Row> getAllRows(){
        return table;
    }



    class Row {
        WebElement rowElement;
        Element type;
        Element name;
        Element id;
        Element offers;
        Element actionsButton;

        Row(WebElement rowElement){
            this.rowElement = rowElement;

            type = new Element(rowElement.findElement(By.xpath(".//div[contains(@class, 'campaign-type')]/span")));
            name = new Element(rowElement.findElement(By.xpath(".//*[contains(@class, 'campaign-teaser-name')]")));
            id = new Element(rowElement.findElement(By.xpath(".//*[contains(@class, 'campaign-teaser-id')]")));
            offers = new Element(rowElement.findElement(By.xpath(".//*[contains(@class, 'campaign-teaser-count-text')]")));
            actionsButton = new Element(rowElement.findElement(By.xpath(".//*[@data-toggle = 'dropdown']")));
        }

        void deactivate(){
            actionsButton.click();
            Element deactivateButton = new Element(rowElement.findElement(By.xpath(".//*[contains(text(),'Deactivate')]")));
            deactivateButton.click();
        }

        void delete(){
            verifyOffers();
            actionsButton.click();
            Element deleteButton = new Element(rowElement.findElement(By.xpath(".//*[contains(text(),'Delete')]")));
            deleteButton.click();
            new Alert().confirm();
        }

        private void verifyOffers(){
            if(!offers.getText().equals("0")){
                Assert.fail("FAILED: You can not delete campaign with offers. Offers count: <" + offers.getText() + ">, Campaign name: <" + name.getText() + ">");
            }
        }
    }
}

