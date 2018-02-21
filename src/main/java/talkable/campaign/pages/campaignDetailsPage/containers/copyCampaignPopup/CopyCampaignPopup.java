package talkable.campaign.pages.campaignDetailsPage.containers.copyCampaignPopup;

import abstractObjects.AbstractElementsContainer;
import org.openqa.selenium.support.ui.ExpectedConditions;
import talkable.campaign.pages.campaignDetailsPage.CampaignDetailsPage;

public class CopyCampaignPopup extends AbstractElementsContainer{

    private ElmntCampaignNameInput campaignName;
    private ElmntCopyButton copyButton;

    public CopyCampaignPopup(){
        campaignName = new ElmntCampaignNameInput();
        copyButton = new ElmntCopyButton();
    }

    public CampaignDetailsPage copyCampaign(String newCampaignName){
        campaignName.clear();
        campaignName.sendKeys(newCampaignName);
        copyButton.click();
        return new CampaignDetailsPage();
    }

    public CampaignDetailsPage copyCampaign(){
        copyButton.click();
        return new CampaignDetailsPage();
    }



}