package detect.object;

import org.openqa.selenium.WebDriver;

public class AssertURL extends Action {
    String expectedUrl;
    public AssertURL(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    public String getExpectedUrl() {
        return expectedUrl;
    }

    public void setExpectedUrl(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    @Override
    public String getText_locator() {
        return null;
    }

    @Override
    public void run(WebDriver driver) {
        return;
    }
}
