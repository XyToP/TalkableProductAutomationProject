package talkable.talkableSite.createNewCampaignPage;

import abstractObjects.DrivenElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import talkable.talkableSite.AbstractTalkableSitePage;
import talkable.common.CampaignPlacement;
import talkable.common.CampaignType;
import talkable.talkableSite.campaign.pages.detailsPage.CampaignDetailsPage;
import talkable.talkableSite.createNewCampaignPage.elements.*;


public class CreateNewCampaignPage extends AbstractTalkableSitePage {
    private SelectAdvocateDashboardButton selectAdvocateDashboardButton = new SelectAdvocateDashboardButton();;
    private SelectInviteButton selectInviteButton = new SelectInviteButton();
    private SelectRewardGleamButton selectRewardGleamButton = new SelectRewardGleamButton();

    public CreateNewCampaignPage(){
    }

    public CampaignDetailsPage createCampaign(CampaignType campaignType, CampaignPlacement placementType){

        DrivenElement campaignButton = getSelectCampaignButton(campaignType);
        campaignButton.click();
        DrivenElement createButton = getPlacementButton(placementType);
        wait.until(ExpectedConditions.visibilityOf(createButton.getWebElement()));
        try {
            createButton.click();
        }catch (WebDriverException e){
            System.out.println("DEBAG-TEMP: WebDriverException is returned and Ignored for the first time.");
            createButton.click();
            System.out.println("DEBAG-TEMP: Clicked to Create Button successfully from the second attempt.");
        }
        //debag:::
        try {
            return new CampaignDetailsPage();
        }catch (AssertionError e){
            System.out.println("DEBAG-TEMP: Details page is not opened after the first click!!!. \r Details:\r" + e.getLocalizedMessage());
            createButton.click();
            System.out.println("DEBAG-TEMP: clicked at second time");
            return new CampaignDetailsPage();
        }
    }


    private DrivenElement getPlacementButton(CampaignPlacement placementType){
        DrivenElement createButton = null;
        switch (placementType){
            case PostPurchase:
                createButton = new CreatePostPurchaseButton();
                break;

            case FloatingWidget:
                createButton = new CreateFloatingWidgetButton();
                break;

            case Standalone:
                createButton =  new CreateStandaloneButton();
                break;

            case Gleam:
                createButton = new ElmntCreateGleamButton();
                break;
        }
        return createButton;
    }

    private DrivenElement getSelectCampaignButton(CampaignType campaignType){
        DrivenElement campaignButton = null;
        switch(campaignType){
            case Invite:
                campaignButton = selectInviteButton;
                break;

            case AdvocateDashboard:
                campaignButton = selectAdvocateDashboardButton;
                break;

            case RewardGleam:
                campaignButton = selectRewardGleamButton;
                break;
        }
        return campaignButton;
    }
}
