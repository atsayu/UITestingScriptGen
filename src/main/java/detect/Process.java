package detect;

import detect.object.Action;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import detect.object.AssertURL;
import detect.object.ClickAction;
import detect.object.InputAction;
import detect.ver2.Click;
import detect.ver2.InputElement;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.print.Doc;

public class Process {
    public static Pair<String, List<Action>> parseJson(String pathToJson) {
        String url = "";
        List<Action> list = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(pathToJson));

            JSONObject jsonObject =  (JSONObject) obj;

            url = (String) jsonObject.get("url");

            JSONArray actions = (JSONArray) jsonObject.get("actions");
            for (Object o : actions) {
                JSONObject action = (JSONObject) o;
                String type = (String) action.get("type");
                if (type.equals("input")) {
                    String value = (String) action.get("value");
                    String locator = (String) action.get("locator");
                    Action act = new InputAction(value, locator);
                    list.add(act);
                }
                if (type.equals("click")) {
                    String locator = (String) action.get("locator");
                    Action act = new ClickAction(locator);
                    list.add(act);
                }
                if (type.equals("assertUrl")) {
                    String expectedUrl = (String) action.get("expectedUrl");
                    Action act = new AssertURL(expectedUrl);
                    list.add(act);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Pair<>(url, list);
    }

    public static List<Action> detectLocators(List<Action> list, String url) {
        String htmlContent = getHtmlContent(url);
        Document document = getDomTree(htmlContent);
        List<Action> visited = new ArrayList<>();
        Map<String, List<Action>> map = new HashMap<>(); //saves actions that needed to  be currently detected
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof AssertURL) {
                if (map.containsKey("Input")) {
                    List<Action> inputActions = map.get("Input");
                    List<String> text_locators = new ArrayList<>();
                    for (Action action : inputActions) {
                        text_locators.add(action.getText_locator());
                    }
                    Map<String, String> res = InputElement.detectInputElement(text_locators, document);
                    for (Action action : inputActions) {
                        action.setDom_locator(res.get(action.getText_locator()));
                    }
                }
                if (map.containsKey("Click")) {
                    List<Action> clickActions = map.get("Click");
                    List<String> text_locators = new ArrayList<>();
                    for (Action action : clickActions) {
                        text_locators.add(action.getText_locator());
                    }
                    Map<String, String> res = Click.detectClickElement(text_locators, document);
                    for (Action action : clickActions) {
                        action.setDom_locator(res.get(action.getText_locator()));
                    }
                }
                if (i != list.size() - 1) {
                    WebDriver driver = new ChromeDriver();
                    driver.get(url);
                    Action.runActions(visited, driver);
                    String pageSource = driver.getPageSource();
                    document = getDomTree(pageSource);
                    driver.quit();
                }
                visited.add(list.get(i));
                map.clear();

            } else {
                visited.add(list.get(i));
                if (list.get(i) instanceof InputAction) {
                    if (!map.containsKey("Input")) {
                        List<Action> inputActions = new ArrayList<>();
                        inputActions.add(list.get(i));
                        map.put("Input", inputActions);
                    } else {
                        map.get("Input").add(list.get(i));
                    }
                }
                if (list.get(i) instanceof ClickAction) {
                    if (!map.containsKey("Click")) {
                        List<Action> clickActions = new ArrayList<>();
                        clickActions.add(list.get(i));
                        map.put("Click", clickActions);
                    } else {
                        map.get("Click").add(list.get(i));
                    }
                }
                if (i == list.size() - 1) {
                    if (map.containsKey("Input")) {
                        List<Action> inputActions = map.get("Input");
                        List<String> text_locators = new ArrayList<>();
                        for (Action action : inputActions) {
                            text_locators.add(action.getText_locator());
                        }
                        Map<String, String> res = InputElement.detectInputElement(text_locators, document);
                        for (Action action : inputActions) {
                            action.setDom_locator(res.get(action.getText_locator()));
                        }
                    }
                    if (map.containsKey("Click")) {
                        List<Action> clickActions = map.get("Click");
                        List<String> text_locators = new ArrayList<>();
                        for (Action action : clickActions) {
                            text_locators.add(action.getText_locator());
                        }
                        Map<String, String> res = Click.detectClickElement(text_locators, document);
                        for (Action action : clickActions) {
                            action.setDom_locator(res.get(action.getText_locator()));
                        }
                    }
                    map.clear();
                }
            }
        }
        return list;
    }
    public static String getHtmlContent(String linkHtml) {
        WebDriver driver = new ChromeDriver();
        driver.get(linkHtml);
        String htmlContent = driver.getPageSource();
        driver.quit();
        return htmlContent;
    }

    public static Document getDomTree(String htmlContent) {
        Document domTree = Jsoup.parse(htmlContent);
        return domTree;
    }

    public static String getXpath(Element e) {
        int attributes_size = e.attributesSize();
        String xpath = "";
        xpath += "//" + e.tagName() + "[";
        boolean havingPreviousAttribute = false;
        if (attributes_size > 0) {
            Attributes attr = e.attributes();
            if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
                xpath += "@id=" + "'" + e.attr("id") + "']";
                return xpath;
            }
            if (attributes_size == 1 && e.hasAttr("class") && !e.attr("class").isEmpty()) {
                xpath += "@class=" + "'" + e.attr("class");
                havingPreviousAttribute = true;
            } else {
                for (Attribute temp : attr) {
                    if (temp.getKey().equals("pattern") || temp.getKey().equals("class")) {
                        continue;
                    } else {
                        if (havingPreviousAttribute) {
                            xpath += " and " + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
                        } else {
                            xpath += "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
                            havingPreviousAttribute = true;
                        }

                    }
                }
            }
        }

        String textOfElement = e.ownText();
        if (havingPreviousAttribute && !textOfElement.matches("\\s*")) {
            xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
        } else {
            if (!textOfElement.matches("\\s*")) {
                xpath += "normalize-space()=" + "'" + textOfElement + "'";
            }
        }
        xpath += "]";
        return xpath;
    }

    public static void main(String[] args) {
        Pair<String, List<Action>> res = parseJson("C:\\Users\\admin\\Desktop\\sample_saucedemo.json");
        String url = res.getFirst();
        List<Action> actions = res.getSecond();
        List<Action> result = detectLocators(actions, url);
        for (Action action : result) {
            System.out.println(action.getDom_locator());
        }
    }
}
