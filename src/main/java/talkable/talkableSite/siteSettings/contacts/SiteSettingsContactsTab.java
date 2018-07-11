package talkable.talkableSite.siteSettings.contacts;

import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.siteSettings.SiteSettingsPage;

public class SiteSettingsContactsTab extends SiteSettingsPage {
    //Page Elements
    private static final By elmntCSEmail = By.xpath("//*[@name = 'customer_service_email']");
    private static final By elmntCSName = By.xpath("//*[@name = 'customer_service_name']");
    private static final By elmntTechnicalEmail = By.xpath("//*[@name = 'technical_contact_email']");
    private static final By elmntCheckboxIntegrationError = By.xpath("//*[@for='site-ignore-failed-transactions']");
    private static final By elmntRewardEmail = By.xpath("//*[@name='reward_contact_email']");
    private static final By elmntMarketerEmail = By.xpath("//*[@name='marketer_contact_email']");
    private static final By elmntCheckboxUnpaidReward = By.xpath("//*[@for='site-send-unpaid-reward-notifications']");
    private static final By elmntCheckboxWeeklyPerformanceReport = By.xpath("//*[@for='site-weekly-performance-report-enabled']");

    public SiteSettingsContactsTab(){
        setVisibleElements();
    }
    private void setVisibleElements(){
        new Element(elmntCSEmail);
        new Element(elmntMarketerEmail);
        new Element(elmntCheckboxWeeklyPerformanceReport);
    }

    //Getters
    public String getCSEmail(){
        return new Element(elmntCSEmail).getAttribute("value");
    }
    public String getCSName(){
        return new Element(elmntCSName).getAttribute("value");
    }
    public String getTechnicalEmail(){
        return new Element(elmntTechnicalEmail).getAttribute("value");
    }
    public String getRewardEmail(){
        return new Element(elmntRewardEmail).getAttribute("value");
    }
    public String getMarketerEmail(){
        return new Element(elmntMarketerEmail).getAttribute("value");
    }

    public SiteSettingsContactsTab updateAll(String csEmail, String csName, String technicalEmail, String rewardEmail, String marketerEmail ) {
        new Element(elmntCSEmail, "CS Email field").clearAndSendKeys(csEmail);
        new Element(elmntCSName, "CS Name field").clearAndSendKeys(csName);
        new Element(elmntTechnicalEmail, "Technical contact Email field").clearAndSendKeys(technicalEmail);
        new Element(elmntRewardEmail, "Reward contact email field").clearAndSendKeys(rewardEmail);
        new Element(elmntMarketerEmail, "Marketer contact email field").clearAndSendKeys(marketerEmail);
        updateChanges();
        return new SiteSettingsContactsTab();
    }

    public void populate(String csEmail) {
        new Element(elmntCSEmail, "CS Email field").clearAndSendKeys(csEmail);
    }
}
