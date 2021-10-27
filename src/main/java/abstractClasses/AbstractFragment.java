package abstractClasses;

import driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AbstractFragment {

    private WebElement rootElement;

    protected WebElement findElement(By by){
        return DriverManager.getDriver().findElement(by);
    }

    protected List<WebElement> findElements(By by){
        return DriverManager.getDriver().findElements(by);
    }

}
