package talkable.talkableSite;

import abstractObjects.AbstractElementsContainer;
import abstractObjects.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.logging.Log;

public abstract class AbstractTkblSitePageWithoutHeader extends AbstractElementsContainer{
    /*Class for Talkable pages without Header(). Example SimpleEditorPage() */

    private static final By changesSavedMsgLctr = By.xpath("//*[text()='Changes have been saved']");


    public void waitSaving(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(changesSavedMsgLctr));
//        new Element(changesSavedMsgLctr, "'Changes have been saved' notification");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(changesSavedMsgLctr));
        }catch (TimeoutException e){
            Log.logRecord("LOG: Saving message is not disappeared for 15 seconds. Test execution is continued.\r\n" + e.getLocalizedMessage());
        }
    }

//    public void waitTillSaveMsgDisappeared(){
//        wait.until(ExpectedConditions
//                .invisibilityOfElementLocated(changesSavedMsgLctr));
//        Log.logRecord("'Changes Saved' notification message has been disappeared.");
//    }
}
