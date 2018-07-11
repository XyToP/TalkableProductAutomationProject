package talkable.talkableSite.campaignsPage;

import abstractObjects.AbstractElementsContainer;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import talkable.common.elements.alert.Alert;
import abstractObjects.Element;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import util.WaitFactory;

public class Table extends AbstractElementsContainer {
    private static final By disabledLctr = By.xpath("//h2[contains(text(), 'Disabled')]/following::table[1]");
    private static final By liveLctr = By.xpath("//h2[contains(text(), 'Live')]/following::table[1]");
    private static final By testLctr = By.xpath("//h2[contains(text(), 'Test')]/following::table[1]");
    private static final By rowsXpath = By.xpath("./tbody/tr");

    private static final By campaignsCountXpath = By.xpath("./preceding::h2[1]/span[@class='Campaign-management-list-header-count']");

    private Status status;
    private ArrayList<Row> table = new ArrayList<>();

    public enum Status{LIVE, TEST, DISABLED}

    Table(Status status) {

        WebElement tableElement = setTableWebElement(status);
        assert tableElement != null;

//        List<WebElement> allRows = tableElement.findElements(rowsXpath);
        List<WebElement> allRows = WaitFactory
                .getCustomWait(2, 500)
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(tableElement, rowsXpath));

        this.status = status;
        for (WebElement webElement :
                allRows) {
            this.table.add(new Row(webElement));
        }
    }


    private WebElement setTableWebElement(Status status){
        long waitSecondsForElmntToBePresent = 2;
        long sleepMillis = 500;

        try {
            switch (status) {
                case LIVE:
                    return WaitFactory
                            .getCustomWait(waitSecondsForElmntToBePresent, sleepMillis)
                            .until(ExpectedConditions.visibilityOfElementLocated(liveLctr));
//                return driver.findElement(liveLctr) ;
                case TEST:
                    return WaitFactory
                            .getCustomWait(waitSecondsForElmntToBePresent, sleepMillis)
                            .until(ExpectedConditions.visibilityOfElementLocated(testLctr));
//                return driver.findElement(testLctr);
                case DISABLED:
                    return WaitFactory
                            .getCustomWait(waitSecondsForElmntToBePresent, sleepMillis)
                            .until(ExpectedConditions.visibilityOfElementLocated(disabledLctr));
//                return driver.findElement(disabledLctr);
                default:
                    Assert.fail("FAILED: Unknown campaign status: " + status.toString());
                    return null;
            }
        }catch (TimeoutException e){
            Assert.fail("FAILED: Campaigns with status <" + status + "> are not available on Campaigns Page");
            return null;
        }
    }

    public String getCampaignsCount(){
        return setTableWebElement(status)
                .findElement(campaignsCountXpath)
                .getText();
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

        String getName(){
            return new Element(rowElement.findElement(By.xpath(".//*[contains(@class, 'campaign-teaser-name')]")))
                    .getText();
        }
    }
}

