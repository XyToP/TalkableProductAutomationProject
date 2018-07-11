package talkable.talkableSite.customerServicePortal.pendingReferrals;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;

class ConfirmationPopup extends AbstractElementsContainer{

    private static final By okLctr = By.xpath("//div[contains(@class, 'is-confirmation')]//div[contains(@class, 'is-success')]");

    private Element okButton = new Element(okLctr, "OK");

    ReferralsPage confirm(){
        okButton.click();
        return new ReferralsPage();
    }
}
