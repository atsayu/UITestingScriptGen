package detect;

import detect.object.Action;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import detect.object.AssertURL;
import detect.object.Click;
import detect.object.Input;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Process {
    public static Pair<String, List<Action>> parseJson(String pathToJson) {
        String url = "";
        List<Action> list = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(pathToJson));

            JSONObject jsonObject =  (JSONObject) obj;

            url = (String) jsonObject.get("url");
//            System.out.println(url);

            JSONArray actions = (JSONArray) jsonObject.get("actions");
            for (Object o : actions) {
                JSONObject action = (JSONObject) o;
                String type = (String) action.get("type");
                if (type.equals("input")) {
                    String value = (String) action.get("value");
                    String locator = (String) action.get("locator");
                    Action act = new Input(value, locator);
                    list.add(act);
                }
                if (type.equals("click")) {
                    String locator = (String) action.get("locator");
                    Action act = new Click(locator);
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
        System.out.println(url);
        List<Action> actions = res.getSecond();
        for (Action act : actions) {
            System.out.println(act.getText_locator());
        }
    }
}
