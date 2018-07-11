package talkable.talkableSite.headerFrame;

import abstractObjects.AbstractElementsContainer;
import org.openqa.selenium.By;
import talkable.homePage.HomePage;
import talkable.talkableSite.IntegrationInstructionPage.IntegrationInstructionPage;
import talkable.talkableSite.campaignsPage.PageCampaigns;
import talkable.talkableSite.customerServicePortal.OldCspPage;
import talkable.talkableSite.customerServicePortal.personLookup.PersonLookupPage;
import talkable.talkableSite.headerFrame.elements.*;
import talkable.talkableSite.headerFrame.elements.menuFrame.MenuFrame;
import talkable.talkableSite.reports.reportsPage.ReportsPage;
import talkable.talkableSite.siteDashboardPage.SiteDashboardPage;

public class Header extends AbstractElementsContainer{

    //Elements:
    private CustomerServicesButton customerServicesButton = new CustomerServicesButton();
    private DashboardButton dashboardButton = new DashboardButton();
    private CampaignsButton campaignsButton = new CampaignsButton();
    private ReportsButton reportsButton = new ReportsButton();
    private MenuButton menuButton = new MenuButton();
    private SiteSelect siteSelect = new SiteSelect();


//    public Header(){
//        siteSelectButton = new SelectedSiteElement();
//        customerServicesButton = new CustomerServicesButton();
//        dashboardButton = new DashboardButton();
//        campaignsButton = new CampaignsButton();
//        reportsButton = new ReportsButton();
//        menuButton = new MenuButton();
//        siteSelect = new SiteSelect();

//        siteSelect = new SiteSelect();

//    }


    public Header selectByVisibleText(String siteName){
        new SiteSelect().selectByVisibleText(siteName);
        return new Header();
    }

    public String getSiteName(){
        return new SiteSelect().getSelectedItemText();
    }

    public MenuFrame openMenu(){
        new MenuButton().click();
        return new MenuFrame();
    }

    public ReportsPage clickReportsButton(){
        new ReportsButton().click();
        return new ReportsPage();
    }

    public PageCampaigns openCampaignsPage(){
        new CampaignsButton().click();
        return new PageCampaigns();
    }

    public SiteDashboardPage openSiteDashboard() {
        dashboardButton.click();
        return new SiteDashboardPage();
    }

    public IntegrationInstructionPage openSiteDashboardForNonIntegratedSite() {
        dashboardButton.click();
        return new IntegrationInstructionPage();
    }


    public PersonLookupPage openCustomerServicePortal(){
        customerServicesButton.click();
        return new PersonLookupPage();
    }

    // method to support old CSP portal(should be removed when new one deployed)
    public OldCspPage openOldCustomerServicePortal(){
        customerServicesButton.click();
        return new OldCspPage();
    }

    public HomePage logout(){
        return openMenu().clickLogout();

    }


}
