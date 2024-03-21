package detect;

import detect.object.*;

import java.io.*;
import java.util.*;

import detect.ver2.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static valid.TempScriptGen.searchLogicExpressionOfActions;

public class Process {
    public static List<detect.Pair<List<detect.object.Action>, String>> parseXml(String outline) {
        List<detect.Pair<List<detect.object.Action>, String>> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new File(outline));
            String url = document.getElementsByTagName("url").item(0).getTextContent();
            NodeList testcases = document.getElementsByTagName("TestCase");
            List<org.w3c.dom.Element> expressionActionElements = new ArrayList<>();
            for (int i = 0; i < testcases.getLength(); i++) {
                searchLogicExpressionOfActions(testcases.item(i), expressionActionElements);
                List<detect.object.Action> listActions = Process.convertLogicExpressionOfActionToAction(expressionActionElements);
                detect.Pair<List<detect.object.Action>, String> pair = new detect.Pair<>(listActions, url);
                list.add(pair);
                expressionActionElements.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Action> convertLogicExpressionOfActionToAction(List<org.w3c.dom.Element> expressionActionElements) {
        List<Action> listActions = new ArrayList<>();
        for (org.w3c.dom.Element expressionActionElement: expressionActionElements) {
            String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
            if (!type.equals("and") && !type.equals("or")) {
                if (type.equals("Input Text")) {
                    String text_locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                    String text = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                    Action action = new InputAction(text, text_locator);
                    listActions.add(action);
                }
                if (type.equals("Click Element")) {
                    String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                    Action action = new ClickAction(locator);
                    listActions.add(action);
                }
                if (type.equals("Select List")) {
                    String choice = expressionActionElement.getElementsByTagName("value").item(0).getTextContent();
                    String question = expressionActionElement.getElementsByTagName("list").item(0).getTextContent();
                    Action action = new SelectAction(question, choice);
                    listActions.add(action);
                }
                if (type.equals("Select Checkbox")) {
                    String choice = expressionActionElement.getElementsByTagName("answer").item(0).getTextContent();
                    String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
                    Action action = new ClickCheckboxAction(choice, question);
                    listActions.add(action);
                }
                if (type.equals("Verify URL")) {
                    String url = expressionActionElement.getElementsByTagName("url").item(0).getTextContent();
                    Action action = new AssertURL(url);
                    listActions.add(action);
                }
            }
        }
        return listActions;
    }

//    public static Pair<String, List<Action>> parseJson(String pathToJson) {
//        String url = "";
//        List<Action> list = new ArrayList<>();
//        JSONParser parser = new JSONParser();
//
//        try {
//            Object obj = parser.parse(new FileReader(pathToJson));
//
//            JSONObject jsonObject =  (JSONObject) obj;
//
//            url = (String) jsonObject.get("url");
//
//            JSONArray actions = (JSONArray) jsonObject.get("actions");
//            for (Object o : actions) {
//                JSONObject action = (JSONObject) o;
//                String type = (String) action.get("type");
//                if (type.equals("input")) {
//                    String value = (String) action.get("value");
//                    String locator = (String) action.get("locator");
//                    Action act = new InputAction(value, locator);
//                    list.add(act);
//                }
//                if (type.equals("click")) {
//                    String locator = (String) action.get("locator");
//                    Action act = new ClickAction(locator);
//                    list.add(act);
//                }
//                if (type.equals("select")) {
//                    String question = (String) action.get("question");
//                    String choice = (String) action.get("choice");
//                    Action act = new SelectAction(question, choice);
//                    list.add(act);
//                }
//                if (type.equals("checkbox")) {
//                    String choice = (String) action.get("choice");
//                    Action act = new ClickCheckboxAction(choice);
//                    list.add(act);
//                }
//                if (type.equals("hover")) {
//                    String locator = (String) action.get("locator");
//                    Action act = new HoverAction(locator);
//                    list.add(act);
//                }
//                if (type.equals("assertUrl")) {
//                    String expectedUrl = (String) action.get("expectedUrl");
//                    Action act = new AssertURL(expectedUrl);
//                    list.add(act);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return new Pair<>(url, list);
//    }

    public static List<Action> detectLocators(List<Action> list, String url) {
        String htmlContent = getHtmlContent(url);

        Element previousElement = null;
        Document document = getDomTree(htmlContent);
        Elements inputElements = HandleInput.getInputElements(document);
        Elements selectElements = HandleSelect.getSelectElements(document);
        Elements clickableElements = HandleClick.getClickableElements(document);
        boolean isAfterHoverAction = false;
        List<Action> visited = new ArrayList<>();
        Map<String, List<Action>> map = new HashMap<>(); //saves actions that needed to  be currently detected
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof AssertURL) {
                if (i != list.size() - 1) {
                    WebDriver driver = new ChromeDriver();
                    driver.get(url);
                    Action.runActions(visited, driver);
                    String pageSource = driver.getPageSource();
                    try {
                        FileWriter file = new FileWriter("src/main/resources/testcase/pagesource.html");
                        file.write(pageSource);
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    document = getDomTree(pageSource);
                    inputElements = HandleInput.getInputElements(document);
                    selectElements = HandleSelect.getSelectElements(document);
                    clickableElements = HandleClick.getClickableElements(document);
                    driver.quit();
                }
                visited.add(list.get(i));
                isAfterHoverAction = false;
                previousElement = null;
            } else {
                visited.add(list.get(i));
                if (list.get(i) instanceof InputAction) {
                    String text_locator = list.get(i).getText_locator();
                    List<String> input = Arrays.asList(text_locator);
                    Map<String, List<Element>> res = InputElement.detectInputElement(input, inputElements, isAfterHoverAction);
                    isAfterHoverAction = false;
                    List<Element> elementList = res.get(text_locator);
                    if (elementList.size() == 1) {
                        Element e = elementList.get(0);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    } else {
                        Element e = HandleElement.findNearestElementWithSpecifiedElement(previousElement, elementList);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    }

                }
                if (list.get(i) instanceof ClickAction) {
                    String text_locator = list.get(i).getText_locator();
                    List<String> input = Arrays.asList(text_locator);
                    Map<String, List<Element>> res = Click.detectClickElement(input, clickableElements, isAfterHoverAction);
                    isAfterHoverAction = false;
                    List<Element> elementList = res.get(text_locator);
                    if (elementList.size() == 1) {
                        Element e = elementList.get(0);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    } else {
                        Element e = HandleElement.findNearestElementWithSpecifiedElement(previousElement, elementList);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    }
                }
                if (list.get(i) instanceof HoverAction) {
                    String text_locator = list.get(i).getText_locator();
                    List<String> input = Arrays.asList(text_locator);
                    Map<String, List<Element>> res = Hover.detectHoverElement(input, clickableElements, isAfterHoverAction);
                    isAfterHoverAction = true;
                    List<Element> elementList = res.get(text_locator);
                    if (elementList.size() == 1) {
                        Element e = elementList.get(0);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    } else {
                        Element e = HandleElement.findNearestElementWithSpecifiedElement(previousElement, elementList);
                        String locator = Process.getXpath(e);
                        list.get(i).setDom_locator(locator);
                        previousElement = e;
                    }
                }
                if (list.get(i) instanceof ClickCheckboxAction) {
                    ClickCheckboxAction checkboxAction = (ClickCheckboxAction) list.get(i);
                    String choice = checkboxAction.getChoice();
                    String question = checkboxAction.getQuestion();
                    List<String> listChoices = Arrays.asList(choice);
                    Map<String, List<String>> mapQuestionAndChoice = new HashMap<>();
                    Pair<String, String> pair = new Pair<>(question, choice);
                    mapQuestionAndChoice.put(question, listChoices);
                    Map<Pair<String, String>, Element> res = Checkbox.detectCheckboxElement(mapQuestionAndChoice, document);
                    Element checkbox = res.get(pair);
                    isAfterHoverAction = false;
                    list.get(i).setDom_locator(Process.getXpath(checkbox));
                    previousElement = checkbox;
                }
                if (list.get(i) instanceof SelectAction) {
                    SelectAction selectAction = (SelectAction) list.get(i);
                    String question = selectAction.getQuestion();
                    String choice = selectAction.getChoice();
                    Pair<String, String> pair = new Pair<>(question, choice);
                    List<Pair<String, String>> list_pair = new ArrayList<>();
                    list_pair.add(pair);
                    Map<Pair<String, String>, Element> res = SelectElement.detectSelectElement(list_pair, selectElements);
                    Element select = res.get(pair);
                    previousElement = select;
                    isAfterHoverAction = false;
                    list.get(i).setDom_locator(Process.getXpath(select));
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
        List<detect.Pair<List<Action>, String>> res = parseXml("src/main/resources/template/outline.xml");
        for (detect.Pair<List<Action>, String> pair : res) {
            String url = pair.getSecond();
            List<Action> actions = pair.getFirst();
            List<Action> result = detectLocators(actions, url);
            for (Action action : result) {
                System.out.println(action.getDom_locator());
            }
            System.out.println("next testcase");
        }
    }
}
