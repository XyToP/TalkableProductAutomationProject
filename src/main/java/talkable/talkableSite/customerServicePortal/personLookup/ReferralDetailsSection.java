package talkable.talkableSite.customerServicePortal.personLookup;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;

public class ReferralDetailsSection extends AbstractElementsContainer{
    private static final By advocateRewardStatusLctr = By.xpath("//div[@class='CSP-card-records-reward'][1]//*[contains(@class, 'status')]");
    private static final By blockedAdvocateStatusLctr = By.xpath("//div[@class='CSP-card-records-reward'][1]//*[contains(@class, 'item-value')]");

    private static final By friendRewardStatusLctr = By.xpath("//div[@class='CSP-card-records-reward'][2]//*[contains(@class, 'status')]");
    private static final By blockedFriendStatus = By.xpath("//div[@class='CSP-card-records-reward'][2]//*[contains(@class, 'status')]");
    private static final By friendEmailLctr = By.xpath("//div[contains(@class, 'is-friend')]//div[contains(@class, 'information')]/span");

    private Element friendEmail = new Element(friendEmailLctr);

    public String getAdvocateRewardStatus(){
        Element advocateRewardStatus;
        try {
            advocateRewardStatus = new Element(advocateRewardStatusLctr);
        }catch (AssertionError e ){
            advocateRewardStatus = new Element(blockedAdvocateStatusLctr);
        }
        advocateRewardStatus.moveMouseOver();
        return advocateRewardStatus.getText();
    }


    public String getFriendRewardStatus(){
        Element friendRewardStatus;
        try{
            friendRewardStatus = new Element(friendRewardStatusLctr);
        }catch (AssertionError e){
            friendRewardStatus = new Element(blockedFriendStatus);
        }
        return friendRewardStatus.getText();
    }

    public String getFriendEmail(){
        return friendEmail.getText();
    }

}
