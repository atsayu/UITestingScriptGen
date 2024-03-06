package detect.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Input extends Action {

    String value;
    String text_locator;
    String dom_locator;
    public Input(String value, String text_locator) {
        this.value = value;
        this.text_locator = text_locator;
        this.dom_locator = "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText_locator() {
        return text_locator;
    }

    public void setText_locator(String text_locator) {
        this.text_locator = text_locator;
    }

    public String getDom_locator() {
        return dom_locator;
    }

    public void setDom_locator(String dom_locator) {
        this.dom_locator = dom_locator;
    }

    @Override
    public void run(WebDriver driver) {
        driver.findElement(By.xpath(dom_locator)).sendKeys(value);
    }
}
