package talkable.talkableSite.customerServicePortal.personLookup;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;

class ReferralsGrid extends AbstractElementsContainer{
    private static final By rowsLctr = By.xpath("//tr[@class='CSP-row']");

    private ArrayList<ReferralRow> getRows(){
        ArrayList<ReferralRow> rows = new ArrayList<>();
        for (WebElement wElement:
                driver.findElements(rowsLctr)
             ) {
            rows.add(new ReferralRow(wElement));
        }
        return rows;


    }

    ReferralRow findReferralByFriendEmail(String friendEmail){
        for (ReferralRow referral
                :getRows()
             ) {
            if(referral.getFriendEmail().equals(friendEmail)){
                return referral;
            }
        }
        Assert.fail("FAILED: Referral row with friend email <" + friendEmail + "> was not found in the Person Lookup Info --> Referrals tab");
        return null;
    }


    class ReferralRow{
        private final By advocateEmailRowLctr = By.xpath("./td[2]/span");
        private final By friendEmailRowLctr = By.xpath("./td[3]/span");
        private final By referralStatusLctr = By.xpath(".//div[@class='CSP-row-statuses-item-text']");
        private final By detailsLctr = By.xpath(".//span[contains(@class, 'CSP-row-details-button')]");

        private WebElement parentRowElement;

        ReferralRow(WebElement parentElement){
            parentRowElement = parentElement;
            getAdvocateEmail();
            getFriendEmail();
        }


        String getAdvocateEmail(){
            return new Element(
                    parentRowElement.findElement(advocateEmailRowLctr))
                    .getText();
        }

        String getFriendEmail(){
            return new Element(
                    parentRowElement.findElement(friendEmailRowLctr))
                    .getText();
        }

        ReferralDetailsSection clickDetails(){
            new Element(parentRowElement.findElement(detailsLctr)).click();
            return new ReferralDetailsSection();
        }

        String getReferralStatus(){
            return new Element(referralStatusLctr).getText();
        }

    }

}
