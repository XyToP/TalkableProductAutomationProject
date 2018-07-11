package talkable.talkableSite.siteSettings;

import abstractObjects.Element;
import org.openqa.selenium.By;
import org.testng.annotations.Listeners;
import talkable.talkableSite.AbstractTalkableSitePage;
import talkable.talkableSite.siteSettings.basic.SiteSettingsBasicTab;
import talkable.talkableSite.siteSettings.contacts.SiteSettingsContactsTab;
import talkable.talkableSite.siteSettings.integrationSettings.SiteSettingIntegrationTab;

import static talkable.talkableSite.siteSettings.SiteSettingsPage.SiteSettingsTab.BASIC;

public class SiteSettingsPage extends AbstractTalkableSitePage{

    //Elements

    private static final By elmntBasicButton = By.xpath("//a[contains(@class, 'ac-site-navigation-basic')]");
    private static final By elmntIntegrationSettingsButton = By.xpath("//a[contains(@class, 'ac-site-navigation-integration')]");
    private static final By elmntContactsButton = By.xpath("//a[contains(@class, 'ac-site-navigation-contacts')]");
    private static final By elmntSaveButton = By.xpath("//button[contains(@class,'ac-site-save')]");
    private static final By elmntCancelButton = By.xpath("//div[contains(@class,'base-form-inline-link')]");

    private Element basicTab = new Element(elmntBasicButton, "Basic tab");
    private Element contactsTab = new Element(elmntContactsButton, "Contact tab");
    private Element integrationTab = new Element(elmntIntegrationSettingsButton, "Integration Settings tab");

    public enum SiteSettingsTab{ BASIC, CONTACTS, INTERGATION}


    public ElmntUnsavedChangesPopup switchTabWithUnsavedChanges(SiteSettingsTab tab){
        switch (tab){
            case BASIC:
//                new Element(elmntBasicButton, "Basic tab").click();
                basicTab.click();
                break;
            case CONTACTS:
//                new Element(elmntContactsButton, "Contact tab").click();
                contactsTab.click();
                break;
            case INTERGATION:
//                new Element(elmntIntegrationSettingsButton, "Integration Settings tab").click();
                integrationTab.click();
                break;
        }
        return new ElmntUnsavedChangesPopup();
    }

    public SiteSettingsBasicTab openBasicTab(){
//        new Element(elmntBasicButton).click();
        basicTab.click();
        return new SiteSettingsBasicTab();
    }
    public SiteSettingsContactsTab openContactsTab(){
//        new Element(elmntContactsButton).click();
        contactsTab.click();
        return new SiteSettingsContactsTab();
    }
    public SiteSettingIntegrationTab openIntegrationSettingsTab(){
//        new Element(elmntIntegrationSettingsButton).click();
        integrationTab.click();
        return new SiteSettingIntegrationTab();
    }

    public void updateChanges(){
        clickSaveChanges();
        waitSaving();
    }

    public void clickSaveChanges(){
        new Element(elmntSaveButton, "Save Changes button").click();
    }
    public void clickCancel(){
        new Element(elmntCancelButton, "Cancel button").click();
    }


}
