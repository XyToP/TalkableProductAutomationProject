package talkable.talkableSite.customerServicePortal;

import abstractObjects.Element;
import org.openqa.selenium.By;
import talkable.talkableSite.AbstractTalkableSitePage;

public class OldCspPage extends AbstractTalkableSitePage{

    private static final By createReferralTabLctr = By.xpath("//a[text()='Create Referral']");

    private Element createReferralTab = new Element(createReferralTabLctr);


}
