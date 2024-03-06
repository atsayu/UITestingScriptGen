package detect.object;

import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class Action {
    public abstract String getText_locator();
    public abstract void run(WebDriver driver);
    public static void runActions(List<Action> list, WebDriver driver) {
        return;
    }
}
