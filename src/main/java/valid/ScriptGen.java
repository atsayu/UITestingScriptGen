package valid;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mockpage.*;
import invalid.DataPreprocessing;
import invalid.PythonTruthTableServer;
import objects.Expression;
import objects.assertion.LocationAssertion;
import objects.assertion.PageElementAssertion;
import objects.normalAction.NormalAction;
import objects2.InputText;
import org.openqa.selenium.devtools.v85.page.Page;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ScriptGen {
    public static void createDataSheetV2(String outline, String datasheetPath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            File newfile = new File(datasheetPath);
            if (newfile.createNewFile()) {
                System.out.println("Created " + datasheetPath + "!");
            } else {
                System.out.println("The file" + datasheetPath + "already exists!");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(datasheetPath));
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(outline));
            StringBuilder content = new StringBuilder();
            String url = document.getElementsByTagName("url").item(0).getTextContent();
            NodeList testcases = document.getElementsByTagName("TestCase");
            List<Element> testCaseElements = new ArrayList<>();
            for (int i = 0; i < testcases.getLength(); i++) {
                Node testcase = testcases.item(i);
                if (testcase.getNodeType() == Node.ELEMENT_NODE)
                    testCaseElements.add((Element) testcase);
            }
            //This list will be passed to the finding locator API
            List<String> locators = new ArrayList<>();
//            locators.add(url);
            for (Element testcaseElement: testCaseElements) {
                boolean haveAssert = Boolean.parseBoolean(testcaseElement.getElementsByTagName("assert").item(0).getTextContent());
                NodeList testCaseChildNodes = testcaseElement.getChildNodes();
                List<Element> expressionActionElements2 = new ArrayList<>();
                for (int i = 0; i < testCaseChildNodes.getLength(); i++) {
                    Node childNode = testCaseChildNodes.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals("LogicExpressionOfActions"))
                        expressionActionElements2.add((Element) childNode);
                }
                if (haveAssert) {
                    List<List<String>> beforeAssertActions = new ArrayList<>();
                    Stack<String> assertionStack = new Stack<>();
                    for (Element expressionActionElement: expressionActionElements2) {
                        String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
                        if (type.contains("Verify")) {
                            switch (type) {
                                case "Verify url":
                                    System.out.println(expressionActionElement.getElementsByTagName("url").getLength());
                                    assertionStack.add(expressionActionElement.getElementsByTagName("url").item(0).getTextContent());
                                    String assertionExpression = String.join(" & ", assertionStack.stream().toList());
                                    System.out.println(assertionExpression);
                                    List<String> verifyList = new ArrayList<>();
                                    verifyList.add(expressionActionElement.getElementsByTagName("url").item(0).getTextContent());
                                    beforeAssertActions.add(verifyList);
                                    StringBuilder andActions = new StringBuilder();
                                    List<String> output = new ArrayList<>();
                                    addActionAndAssert(beforeAssertActions, output, 0, new StringBuilder());
                                    System.out.println(output);
                                    for (String assertString: output) content.append(assertString).append("\n");
                            }
                        }
                        if (!type.equals("and") && !type.equals("or") && !type.contains("Verify")) {
                            NormalAction action = (NormalAction) LogicParser.createAction(expressionActionElement).getAllK().stream().toList().get(0);
                            String elementLocator = action.getElementLocator();
                            if (!locators.contains(elementLocator))
                                locators.add(elementLocator);
                            String value = action.getValue();
                            if (value != null) {
                                assertionStack.add(value);
                                if (content.indexOf(value) == -1) content.append(value).append("\n");
                                List<String> curActionListString = new ArrayList<>();
                                curActionListString.add(value);
                                beforeAssertActions.add(curActionListString);
                            }
                        } else if(!type.contains("Verify")) {
                            List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(expressionActionElement));
                            List<List<String>> texts = new ArrayList<>();
                            for (List<Expression> actionList : dnfList) {
                                List<String> textList = new ArrayList<>();
                                for (Expression action : actionList) {
                                    String elementLocator = ((NormalAction)action).getElementLocator();
                                    if (!locators.contains(elementLocator))
                                        locators.add(elementLocator);
                                    String value = ((NormalAction) action).getValue();
                                    if (value != null)
                                        textList.add(value);
                                }
                                texts.add(new ArrayList<>(textList));
                            }
                            List<List<Integer>> subsets = Subset.subsets(texts.size());
                            List<String> curActionListString = new ArrayList<>();
                            for (List<Integer> subset : subsets) {
                                if (subset.isEmpty()) continue;
                                StringBuilder satisfy = new StringBuilder();
                                for (int index : subset) {
                                    List<String> textList = texts.get(index);
                                    for (String text : textList) {
                                        if (satisfy.indexOf(text) == -1) {
                                            if (satisfy.isEmpty()) satisfy.append(text);
                                            else satisfy.append(" & ").append(text);
                                        }
                                    }
                                }
                                assertionStack.add(satisfy.toString());
                                curActionListString.add(satisfy.toString());
                                if (content.indexOf(satisfy.toString()) == -1)
                                    content.append(new StringBuilder(satisfy)).append("\n");
                            }
                            beforeAssertActions.add(curActionListString);

                            //This block of code is for invalid test gen
                            String action = LogicParser.createTextExpression(expressionActionElement).toString();
                            System.out.println(action);
                            if (action.charAt(0) == '(') {
                                action = action.substring(1, action.length() - 1);
                            }
                            action = action.replaceAll("\\(", "%28");
                            action = action.replaceAll("\\)", "%29");
                            action = action.replaceAll("[\\&]", "%26");
                            action = action.replaceAll("\\s", "");
                            System.out.println(action);
                            Vector<Vector<String>> tb = DataPreprocessing.truthTableParse(PythonTruthTableServer.logicParse(action), action);
                            System.out.println(tb);
                            Vector<String> invalids = tb.get(2);
                            boolean[] variables = new boolean[tb.get(0).size()];
                            for (String truthLine: invalids) {
                                String[] values = truthLine.split(" ");
                                System.out.println(values.length);
                                for (int i = 0; i < variables.length; i++) {
                                    if (values[i].equals("1")) variables[i] = true;
                                }
                            }
                            for (int i = 0; i < variables.length; i++) {
                                if (variables[i]) content.append(tb.get(0).get(i)).append("\n");
                            }

                        }

                    }
                } else {
                    for (Element expressionActionElement: expressionActionElements2) {
                        String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
                        if (!type.equals("and") && !type.equals("or")) {
                            NormalAction action = (NormalAction) LogicParser.createAction(expressionActionElement).getAllK().stream().toList().get(0);
                            String elementLocator = action.getElementLocator();
                            if (!locators.contains(elementLocator))
                                locators.add(elementLocator);
                            String value = action.getValue();
                            if (value != null) {
                                if (content.indexOf(value) == -1) content.append(value).append("\n");
                            }
                        } else {
                            List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(expressionActionElement));
                            List<List<String>> texts = new ArrayList<>();
                            for (List<Expression> actionList : dnfList) {
                                List<String> textList = new ArrayList<>();
                                for (Expression action : actionList) {
                                    String elementLocator = ((NormalAction)action).getElementLocator();
                                    if (!locators.contains(elementLocator))
                                        locators.add(elementLocator);
                                    String value = ((NormalAction) action).getValue();
                                    if (value != null)
                                        textList.add(value);
                                }
                                texts.add(new ArrayList<>(textList));
                            }
                            List<List<Integer>> subsets = Subset.subsets(texts.size());
                            for (List<Integer> subset : subsets) {
                                if (subset.isEmpty()) continue;
                                StringBuilder satisfy = new StringBuilder();
                                for (int index : subset) {
                                    List<String> textList = texts.get(index);
                                    for (String text : textList) {
                                        if (satisfy.indexOf(text) == -1) {
                                            if (satisfy.isEmpty()) satisfy.append(text);
                                            else satisfy.append(" & ").append(text);
                                        }
                                    }
                                }
                                if (content.indexOf(satisfy.toString()) == -1)
                                    content.append(new StringBuilder(satisfy)).append("\n");
                            }

                            //This block of code is for invalid test gen
                            String action = LogicParser.createTextExpression(expressionActionElement).toString();
                            System.out.println(action);
                            if (action.charAt(0) == '(') {
                                action = action.substring(1, action.length() - 1);
                            }
                            action = action.replaceAll("\\(", "%28");
                            action = action.replaceAll("\\)", "%29");
                            action = action.replaceAll("[\\&]", "%26");
                            action = action.replaceAll("\\s", "");
                            System.out.println(action);
                            Vector<Vector<String>> tb = DataPreprocessing.truthTableParse(PythonTruthTableServer.logicParse(action), action);
                            System.out.println(tb);
                            Vector<String> invalids = tb.get(2);
                            boolean[] variables = new boolean[tb.get(0).size()];
                            for (String truthLine: invalids) {
                                String[] values = truthLine.split(" ");
                                System.out.println(values.length);
                                for (int i = 0; i < variables.length; i++) {
                                    if (values[i].equals("1")) variables[i] = true;
                                }
                            }
                            for (int i = 0; i < variables.length; i++) {
                                if (variables[i]) content.append(tb.get(0).get(i)).append("\n");
                            }

                        }

                    }
                }
            }
            List<String> dectedLocator = fakeLocatorDectector(locators);
            for (int i = 0; i < dectedLocator.size(); i++) {
                StringBuilder variableAndXpath = new StringBuilder();
                variableAndXpath.append(locators.get(i)).append(",").append(dectedLocator.get(i)).append("\n");
                content.append(variableAndXpath);
            }
            bufferedWriter.append(content);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<String> fakeLocatorDectector(List<String> elementDescription) {
        List<String> locators = new ArrayList<>();
        for (String s: elementDescription) {
            StringBuilder detectedLocator = new StringBuilder(s);
            detectedLocator.append("_locator");
            locators.add(detectedLocator.toString());
        }
        return locators;
    }



    public static void searchLogicExpressionOfActions(Node testcase, List<Element> expressionActionElements) {
        if (testcase.getNodeType() == Node.ELEMENT_NODE && ((Element) testcase).getTagName().equals("LogicExpressionOfActions")) {
            expressionActionElements.add((Element) testcase);
        }
        for (int i = 0; i < testcase.getChildNodes().getLength(); i++) {
            searchLogicExpressionOfActions(testcase.getChildNodes().item(i), expressionActionElements);
        }
    }


    public static String contentAction(Element expressionActionElement, Vector<String> allValueVariable, Vector<String> valueVariableNotAssert,  Map<String, String> mapLocatorVariableAndValueVariable, Vector<String> input) {
        String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
        System.out.println(type);
        if (!type.equals("and") && !type.equals("or")) {
            if (type.equals("Input Text")) {
                String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                input.add(locator);
                String text = "";
                if (expressionActionElement.getElementsByTagName("text").getLength() > 0) {
                    text = expressionActionElement.getElementsByTagName("text").item(0).getTextContent();
                }
                if (!mapLocatorVariableAndValueVariable.containsKey(locator)) {
                    mapLocatorVariableAndValueVariable.put(locator, text);
                }
                allValueVariable.add(text);
                valueVariableNotAssert.add(text);
                return "Input " + text + " to " + locator;
            }
            if (type.equals("Click Element")) {
                String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                return "Click " + locator;
            }
            if (type.equals("Radio Button")) {
                String choice = expressionActionElement.getElementsByTagName("choice").item(0).getTextContent();
                String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
                return "Choose " + choice + " for " + question;
            }
            if (type.equals("Select List")) {
                String value = expressionActionElement.getElementsByTagName("value").item(0).getTextContent();
                String list = expressionActionElement.getElementsByTagName("list").item(0).getTextContent();
                return "Select " + value + " for " + list;
            }
            if (type.equals("Select Checkbox")) {
                String answer = expressionActionElement.getElementsByTagName("answer").item(0).getTextContent();
                String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
                return "Select " + answer + " for " + question;
            }
            if (type.equals("Verify URL")) {
                String url = expressionActionElement.getElementsByTagName("url").item(0).getTextContent();
                allValueVariable.add(url);
                return "Assert URL" +  "<input class='assert_url' type='text' value=''>";
            }
        }
        return null;
    }



    public static void addActionAndAssert(List<List<String>> actionTextList, List<String> output, int n, StringBuilder andActions) {
        if (n >= actionTextList.size()) {
            output.add(andActions.toString());
            return;
        }
        List<String> curActions = actionTextList.get(n);
        for (String curAction: curActions) {
            int startIndex = andActions.length();
            if (andActions.isEmpty()) andActions.append(curAction);
            else andActions.append(" & ").append(curAction);
            addActionAndAssert(actionTextList, output, n + 1, new StringBuilder(andActions));
            int endIndex = -1;
            if (startIndex == 0) endIndex = startIndex + curAction.length();
            else endIndex = startIndex + curAction.length() + 3;
            andActions.delete(startIndex, endIndex);
        }
    }

    public static void addActionAndAssertAction(List<List<String>> actionTextList, List<String> output, int n, StringBuilder andActions) {
        if (n >= actionTextList.size()) {
            output.add(andActions.toString());
            return;
        }
        List<String> curActions = actionTextList.get(n);
        for (String curAction: curActions) {
            int startIndex = andActions.length();
            if (andActions.isEmpty()) andActions.append(curAction);
            else andActions.append(curAction);
            addActionAndAssertAction(actionTextList, output, n + 1, new StringBuilder(andActions));
            int endIndex = -1;
            if (startIndex == 0) endIndex = startIndex + curAction.length();
            else endIndex = startIndex + curAction.length() + 3;
            andActions.delete(startIndex, endIndex);
        }
    }
    public static Map<String, List<String>> createDataMap(String path) {
        Map<String, List<String>> variables = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] words = null;
            while ((words = csvReader.readNext()) != null) {
                variables.put(words[0], new ArrayList<>());
                for (int i = 1; i < words.length; i++) {
                    variables.get(words[0]).add(words[i]);
                }
            }
            return variables;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }

    public static void replace(String oldStr, String newStr, StringBuilder testscript) {
        int start = testscript.indexOf(oldStr);
        int end = start + oldStr.length();
        testscript.replace(start, end, newStr);
    }

    public static void createTestCase(int n, Map<Integer, List<StringBuilder>> lines, StringBuilder testScript, StringBuilder content, String testName, AtomicInteger testCount) {
        if (n >= lines.size()) {
            content.append("Valid-Test-").append(testName).append("-").append(testCount).append("\n").append(testScript);
            testCount.incrementAndGet();
            return;
        }
        List<StringBuilder> lineOptions = lines.get(n);
        for (StringBuilder lineOption : lineOptions) {
            int startIndex = testScript.length();
            testScript.append(lineOption);
            createTestCase(n + 1, lines, new StringBuilder(testScript), content, testName, testCount);
            testScript.delete(startIndex, testScript.length());
        }
    }

    public static void createScriptV2(String outlinePath, String dataPath, String outputScriptPath) {
        Map<String, List<String>> dataMap = createDataMap(dataPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputScriptPath));
            DocumentBuilder bulider = factory.newDocumentBuilder();
            Document document = bulider.parse(new File(outlinePath));
            String url = document.getElementsByTagName("url").item(0).getTextContent();
            NodeList testcases = document.getElementsByTagName("TestCase");
            StringBuilder content = new StringBuilder();
            content.append("*** Setting ***\nLibrary\tSeleniumLibrary\n\n*** Test Cases ***\n");
            StringBuilder header = new StringBuilder("*** Variables ***\n");
            for (int i = 0; i < testcases.getLength(); i++) {
                Node testcase = testcases.item(i);
                StringBuilder testScript = new StringBuilder();
                testScript.append("\tOpen Browser\t").append(url).append("\tChrome\n");
                testScript.append("\tMaximize Browser Window\n");
                if (testcase.getNodeType() != Node.ELEMENT_NODE) continue;
                Element testcaseElement = (Element) testcase;
                String testName = testcaseElement.getElementsByTagName("Scenario").item(0).getTextContent();
                StringBuilder validations = new StringBuilder();
                boolean haveAssert = Boolean.parseBoolean(testcaseElement.getElementsByTagName("assert").item(0).getTextContent());
//                for (int j = 0; j < )
                //Giả sử luôn có assert URL
                NodeList nodes = testcaseElement.getChildNodes();
                List<Element> parentLogicOfActions = new ArrayList<>();
                for (int j = 0; j < nodes.getLength(); j++) {
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE && ((Element) nodes.item(j)).getTagName().equals("LogicExpressionOfActions"))
                        parentLogicOfActions.add((Element) nodes.item(j));
                }
                Map<Integer, List<StringBuilder>> lines = new HashMap<>();
                if (haveAssert) {
                    createScriptListHaveAssert(parentLogicOfActions, dataMap, header, lines);
                } else {
                    createScriptListNoAssert(parentLogicOfActions, dataMap, header, lines);
                }

                createTestCase(0, lines, testScript, content, testName, new AtomicInteger(1));
            }
            content.insert(0, header.append("\n"));
            bufferedWriter.append(content);
            bufferedWriter.close();
//            DataPreprocessing.initInvalidDataParse(dataPath,outlinePath, outputScriptPath );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createScriptListNoAssert(List<Element> parentLogicOfActions, Map<String, List<String>> dataMap, StringBuilder header, Map<Integer, List<StringBuilder>> lines) {
        for (Element cur: parentLogicOfActions) {
            String type = cur.getElementsByTagName("type").item(0).getTextContent();
            if (!type.equals("or") && !type.equals("and")) {
                List<StringBuilder> realTestScriptsList = new ArrayList<>();
                NormalAction actionObject = (NormalAction) LogicParser.createAction(cur).getAllK().stream().toList().get(0);
                String elementLocator = actionObject.getElementLocator();
                StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
                if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
                String testScriptWithVariable = actionObject.exprToString();
                String valueOfAction = actionObject.getValue();
                if (valueOfAction != null) {
                    for (String realValue : dataMap.get(valueOfAction)) {
                        StringBuilder realTestScript = new StringBuilder(testScriptWithVariable);
                        replace(valueOfAction, realValue, realTestScript);
                        realTestScriptsList.add(realTestScript);
                    }
                } else {
                    StringBuilder realTestScript = new StringBuilder(testScriptWithVariable);
                    realTestScriptsList.add(realTestScript);
                }
                lines.put(lines.size(), realTestScriptsList);
            } else {
                List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(cur));
                List<StringBuilder> realTestScriptList = new ArrayList<>();
                for (List<Expression> andOfActions: dnfList) {
                    List<String> valueOfActionList = new ArrayList<>();
                    StringBuilder testScriptWithVariable = new StringBuilder();
                    for (Expression action: andOfActions) {
                        NormalAction actionObject = (NormalAction) action;
                        testScriptWithVariable.append("\t").append(actionObject.exprToString()).append("\n");
                        String elementLocator = actionObject.getElementLocator();
                        StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
                        if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
                        String valueOfAction = actionObject.getValue();
                        if (valueOfAction != null) valueOfActionList.add(valueOfAction);
                    }
                    String andOfValues = String.join(" & ", valueOfActionList);
                    for (String realAndOfValues: dataMap.get(andOfValues)) {
                        String[] realValue = realAndOfValues.split(" & ");
                        StringBuilder realTestScript = new StringBuilder(testScriptWithVariable);
                        for (int j = 0; j < realValue.length; j++) {
                            replace(valueOfActionList.get(j), realValue[j], realTestScript);
                        }
                        realTestScriptList.add(realTestScript);
                    }
                }
                lines.put(lines.size(), realTestScriptList);
            }
        }
    }

    private static void createScriptListHaveAssert(List<Element> parentLogicOfActions, Map<String, List<String>> dataMap, StringBuilder header, Map<Integer, List<StringBuilder>> lines) {
        Stack<String> assertionStack = new Stack<>();
        List<List<String>> beforeAssertionStack = new ArrayList<>();
        List<List<String>> listOfBlockActions = new ArrayList<>();
        for (int j = 0; j < parentLogicOfActions.size(); j++) {
            List<StringBuilder> possibleAction = new ArrayList<>();
            while (!parentLogicOfActions.get(j).getElementsByTagName("type").item(0).getTextContent().equals("Verify URL")) {
                Element cur = parentLogicOfActions.get(j);
                String type = cur.getElementsByTagName("type").item(0).getTextContent();
                if (!type.equals("or") && !type.equals("and")) {
                    NormalAction action = (NormalAction) LogicParser.createAction(cur).getAllK().stream().toList().get(0);
                    List<StringBuilder> list = new ArrayList<>();
                    StringBuilder actionString = new StringBuilder();
                    String elementLocator = action.getElementLocator();
                    String locator = "${" + elementLocator + "}";
                    String value = action.getValue();
                    if (value != null) {
                        List<String> valueList = new ArrayList<>();
                        valueList.add(value);
                        beforeAssertionStack.add(valueList);
                    }
                    actionString.append("\t").append(action.exprToString()).append("\n");
                    List<String> singleActionString = new ArrayList<>();
                    singleActionString.add(actionString.toString());
                    listOfBlockActions.add(singleActionString);
                    System.out.println(elementLocator);

                    StringBuilder locatorAndXpath = new StringBuilder().append(locator).append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
                    if (header.indexOf(locatorAndXpath.toString()) == -1)
                        header.append(locatorAndXpath);
                } else {
                    List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(cur));
                    Map<String, String> valueToAction = new HashMap<>();
                    List<List<String>> texts = new ArrayList<>();
                    for (List<Expression> actionList : dnfList) {
                        List<String> textList = new ArrayList<>();
                        for (Expression action : actionList) {
                            String value = ((NormalAction) action).getValue();
                            String elementLocator = ((NormalAction) action).getElementLocator();
                            //Waiting for locator detection API
                            StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
                            if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
                            if (value != null) {
                                textList.add(value);
                                valueToAction.put(value, "\t" +action.exprToString() + "\n");
                            }

                        }
                        texts.add(new ArrayList<>(textList));
                    }
                    List<List<Integer>> subsets = Subset.subsets(texts.size());
                    List<String> curActionListString = new ArrayList<>();
                    List<String> multipleActionList = new ArrayList<>();
                    for (List<Integer> subset : subsets) {
                        if (subset.isEmpty() || subset.size() > 1) continue;
                        StringBuilder satisfy = new StringBuilder();
                        StringBuilder satisfyAction = new StringBuilder();
                        for (int index : subset) {
                            List<String> textList = texts.get(index);
                            for (String text : textList) {
                                satisfyAction.append(valueToAction.get(text));
                                if (satisfy.indexOf(text) == -1) {
                                    if (satisfy.isEmpty()) satisfy.append(text);
                                    else satisfy.append(" & ").append(text);
                                }
                            }
                        }

                        assertionStack.add(satisfy.toString());
                        multipleActionList.add(satisfyAction.toString());
                        curActionListString.add(satisfy.toString());
//                                if (content.indexOf(satisfy.toString()) == -1)
//                                    content.append(new StringBuilder(satisfy)).append("\n");
                    }
                    beforeAssertionStack.add(curActionListString);
                    listOfBlockActions.add(multipleActionList);

                }
                j++;
            }
            List<StringBuilder> realBlockOfCode = new ArrayList<>();
            Element cur = parentLogicOfActions.get(j);
            String type = cur.getElementsByTagName("type").item(0).getTextContent();
            switch (type) {
                case "Verify URL":
                    String expectedUrl = cur.getElementsByTagName("url").item(0).getTextContent();
                    assertionStack.push(expectedUrl);
                    LocationAssertion verifyActionObject = (LocationAssertion) LogicParser.createAction(cur).getAllK().stream().toList().get(0);
                    List<String> verifyList = new ArrayList<>();
                    StringBuilder verifyAction = new StringBuilder();
                    verifyAction.append("\tLocation should be\t").append(expectedUrl).append("\n");
                    verifyList.add(verifyAction.toString());
                    listOfBlockActions.add(verifyList);
                    List<String> verifyText = new ArrayList<>();
                    verifyText.add(expectedUrl);
                    beforeAssertionStack.add(verifyText);
                    String assertionString = String.join(" & ", assertionStack.stream().toList());
                    System.out.println(assertionString);
                    List<String> outputText = new ArrayList<>();
                    addActionAndAssert(beforeAssertionStack, outputText, 0, new StringBuilder());
                    List<String> outputAction = new ArrayList<>();
                    addActionAndAssertAction(listOfBlockActions, outputAction, 0, new StringBuilder());
                    for (int outputIndex = 0; outputIndex < outputText.size(); outputIndex++) {
                        List<String> realDataList = dataMap.get(outputText.get(outputIndex));
                        for (String datas : realDataList) {
                            StringBuilder newBlock = new StringBuilder(outputAction.get(outputIndex));
                            String[] variable = outputText.get(outputIndex).split(" & ");
                            String[] data = datas.split(" & ");
                            Map<String, String> variableData = new HashMap<>();
                            StringBuilder realBlock = new StringBuilder();
                            for (int k = 0; k < variable.length; k++) {
                                int startIndex = newBlock.indexOf(variable[k]);
                                System.out.println(variable[k]);
                                newBlock.replace(startIndex, startIndex + variable[k].length(), data[k]);
                            }
                            realBlockOfCode.add(newBlock);
                        }
                    }
                    break;
                case "Page Element Assertion":
                    String assertElementLocator = cur.getElementsByTagName("url").item(0).getTextContent();
//                                PageElementAssertion verifyElementObject = (PageElementAssertion) LogicParser.createAction(cur).getAllK().stream().toList().get(0);
                    StringBuilder verifyScript = new StringBuilder();
                    break;
            }
            lines.put(lines.size(), realBlockOfCode);
        }
    }

    public static void main(String[] args) {
//        createDataSheetV2("src/main/resources/template/outline.xml", "src/main/resources/data/data_sheet.csv");
        createScriptV2("src/main/resources/template/outline.xml", "src/main/resources/data/data_sheet.csv", "test_saucedemo.robot");
//
//        createDataSheetV2("outline_demoqa.xml", "data_demoqa.csv");
//        createScriptV2("outline_demoqa.xml", "data_demoqa.csv", "test_demoqa.robot");
//
//        createDataSheetV2("outline_thinktester.xml", "data_thinktester.csv");
//        createScriptV2("outline_thinktester.xml", "data_thinktester.csv","thinktester.robot");


//        createDataSheetV2("heroky.xml", "heroky.csv");


//        String expr = "A%26B";
//        Vector tb = DataPreprocessing.truthTableParse(PythonTruthTableServer.logicParse(expr), expr);
//        System.out.println(tb);
//        System.out.println(tb);
//        System.out.println("hello");
//        DataPreprocessing.initInvalidDataParse("data_saucedemo.csv", "outline_saucedemo.xml", "test_saucedemo.robot");
    }
}