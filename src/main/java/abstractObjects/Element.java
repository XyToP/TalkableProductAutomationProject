package abstractObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Element extends AbstractElement{

    public Element(By by){
        setWebElement(by);
    }

    public Element(WebElement webElement){
        setWebElement(webElement);
    }

    //to create new element which is child of @parentElement
    public Element(Element parent, By by){
        setWebElement(parent.getWebElement().findElement(by));
    }
}
