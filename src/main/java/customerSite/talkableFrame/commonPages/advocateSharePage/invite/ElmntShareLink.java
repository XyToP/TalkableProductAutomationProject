package customerSite.talkableFrame.commonPages.advocateSharePage.invite;

import abstractObjects.AbstractElement;
import org.openqa.selenium.By;

class ElmntShareLink extends AbstractElement{

    private static  final By locator = By.cssSelector(".share-link");

    ElmntShareLink(){
        setWebElement(locator);
    }
}
