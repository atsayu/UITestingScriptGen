package valid;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mockpage.newSolve;
import invalid.DataPreprocessing;
import invalid.PythonTruthTableServer;
import objects.ClickElement;
import objects.Expression;
import objects.InputText;
import org.springframework.web.socket.server.HandshakeHandler;
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
            locators.add(url);
            for (Element testcaseElement: testCaseElements) {
                NodeList testCaseChildNodes = testcaseElement.getChildNodes();
                List<Element> expressionActionElements = new ArrayList<>();
                for (int i = 0; i < testCaseChildNodes.getLength(); i++) {
                    Node childNode = testCaseChildNodes.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals("LogicExpressionOfActions"))
                        expressionActionElements.add((Element) childNode);
                }
                List<List<String>> beforeAssertActions = new ArrayList<>();
                Stack<String> assertionStack = new Stack<>();
                for (Element expressionActionElement: expressionActionElements) {

                    String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
                    if (type.equals("Verify URL")) {
                        System.out.println(expressionActionElement.getElementsByTagName("url").getLength());
                        assertionStack.add(expressionActionElement.getElementsByTagName("url").item(0).getTextContent());
                        String assertionExpression = String.join(" & ", assertionStack.stream().toList());
                        System.out.println(assertionExpression);
//                        while (!assertionStack.isEmpty()) {
//                            String cur = assertionStack.pop();
//                            if (content.indexOf(cur) == -1) continue;
//                            content.delete(content.indexOf(cur), cur.length());
//                        }
                        List<String> verifyList = new ArrayList<>();
                        verifyList.add(expressionActionElement.getElementsByTagName("url").item(0).getTextContent());
                        beforeAssertActions.add(verifyList);
//                        content.append(assertionExpression).append("\n");
                        StringBuilder andActions = new StringBuilder();
                        List<String> output = new ArrayList<>();
                        addActionAndAssert(beforeAssertActions, output, 0, new StringBuilder());
                        System.out.println(output);
                        for (String assertString: output) content.append(assertString).append("\n");
                    }
                    if (!type.equals("and") && !type.equals("or") && !type.equals("Verify URL")) {
                        String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
                        if (!locators.contains(locator))
                            locators.add(locator);
                        if (expressionActionElement.getElementsByTagName("text").getLength() > 0) {
                            String text = expressionActionElement.getElementsByTagName("text").item(0).getTextContent();
                            assertionStack.add(text);
                            System.out.println(assertionStack);
                            if (content.indexOf(text) == -1)
                                content.append(text).append("\n");
                            List<String> curActionListString = new ArrayList<>();
                            curActionListString.add(text);
                            beforeAssertActions.add(curActionListString);
                        }
                    } else if(!type.equals("Verify URL")) {
                        List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(expressionActionElement));
                        List<List<String>> texts = new ArrayList<>();
                        for (List<Expression> actionList : dnfList) {
                            List<String> textList = new ArrayList<>();
                            for (Expression action : actionList) {
                                String locator = action.getLocator();
                                if (!locators.contains(locator))
                                    locators.add(locator);
                                if ( action instanceof InputText && ((InputText)action).getValue() != null)
                                    textList.add(((InputText)action).getValue());
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

            }
            String[] test = new String[locators.size()];
            Vector<String> dataOfLocator = newSolve.getLocator(locators.toArray(test));
            for (String s: dataOfLocator) {
                int separator = s.indexOf(":");
                String locator = s.substring(0, separator);
                String xpath = s.substring(separator + 2);
                content.append(locator).append(",").append(xpath);
            }
            bufferedWriter.append(content);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void createTestCase(int n, Map<Integer, List<StringBuilder>> lines, StringBuilder testScript, StringBuilder content, StringBuilder validations, String testName, AtomicInteger testCount) {
        if (n >= lines.size()) {
            content.append("Test ").append(testName).append(" ").append(testCount).append("\n").append(testScript).append(validations);
            testCount.incrementAndGet();
            return;
        }
        List<StringBuilder> lineOptions = lines.get(n);
        for (StringBuilder lineOption : lineOptions) {
            int startIndex = testScript.length();
            testScript.append(lineOption);
            createTestCase(n + 1, lines, new StringBuilder(testScript), content, validations, testName, testCount);
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
                NodeList validationNodes = testcaseElement.getElementsByTagName("Validation");
                boolean haveAssert = false;
                NodeList types = testcaseElement.getElementsByTagName("type");
//                for (int j = 0; j < )
                //Giả sử luôn có assert URL
                for (int j = 0; j < validationNodes.getLength(); j++) {
                    Node validation = validationNodes.item(j);
                    if (validation.getNodeType() != Node.ELEMENT_NODE) continue;
                    Element validationElement = (Element) validation;
                    String type = validationElement.getElementsByTagName("type").item(0).getTextContent();
                    switch (type) {
                        case "URLValidation":
                            String correctUrl = validationElement.getElementsByTagName("url").item(0).getTextContent();
                            validations.append("\tLocation Should Be\t").append(correctUrl).append("\n");
                            break;
                        case "PageContainValidation":
                            String text = validationElement.getElementsByTagName("text").item(0).getTextContent();
                            validations.append("\tPage Should Contain\t").append(text).append("\n");
                            break;
                        default:
                            break;
                    }
                }
                NodeList nodes = testcaseElement.getChildNodes();
                List<Element> parentLogicOfActions = new ArrayList<>();
                for (int j = 0; j < nodes.getLength(); j++) {
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE && ((Element) nodes.item(j)).getTagName().equals("LogicExpressionOfActions"))
                        parentLogicOfActions.add((Element) nodes.item(j));
                }
                Map<Integer, List<StringBuilder>> lines = new HashMap<>();
                Stack<String> assertionStack = new Stack<>();
                StringBuilder blockOfActions = new StringBuilder();
                for (int j = 0; j < parentLogicOfActions.size(); j++) {
                    List<StringBuilder> possibleAction = new ArrayList<>();
                    while (!parentLogicOfActions.get(j).getElementsByTagName("type").item(0).getTextContent().equals("Verify URL")) {
                        Element cur = parentLogicOfActions.get(j);
                        String type = cur.getElementsByTagName("type").item(0).getTextContent();
                        if (!type.equals("or") && !type.equals("and")) {
                            List<StringBuilder> list = new ArrayList<>();
                            StringBuilder actionString = new StringBuilder();
                            String realLocator = cur.getElementsByTagName("locator").item(0).getTextContent();
                            String locator = "${" + realLocator + "}";
                            String text = null;
                            if (cur.getElementsByTagName("text").getLength() > 0) {
                                text = cur.getElementsByTagName("text").item(0).getTextContent();
                                assertionStack.push(text);
                            }

                            actionString.append("\t").append(type).append("\t").append(locator);
                            if (text != null) {
                                actionString.append("\t").append(text).append("\n");
//                                blockOfActions.append("\t").append(text).append("\n");
                            }
                            else {
                                actionString.append("\n");
                            }
                            blockOfActions.append(actionString);
//                            if (text != null) {
//                                for (String s: dataMap.get(text)) {
//                                    StringBuilder prev = new StringBuilder(actionString);
////                                    replace(text, s, actionString);
//                                    list.add(new StringBuilder(actionString));
//                                    actionString = new StringBuilder(prev);
//                                }
//                            } else {
//                                list.add(actionString);
//                            }
//                            lines.put(j, list);
                            System.out.println(realLocator);
                            StringBuilder locatorAndXpath = new StringBuilder().append(locator).append("\t").append(dataMap.get(realLocator).get(0)).append("\n");
                            if (header.indexOf(locatorAndXpath.toString()) == -1)
                                header.append(locatorAndXpath);
                        } else {
                            NodeList texts = cur.getElementsByTagName("text");
                            List<String> allText = new ArrayList<>();
//                            for (int k = 0; k < texts.getLength(); k++) {
//                                allText.add(texts.item(k).getTextContent());
//                            }

                            NodeList actions = cur.getElementsByTagName("LogicExpressionOfActions");

                            for (int k = 0; k < actions.getLength(); k++) {
                                if (actions.item(k).getNodeType() != Node.ELEMENT_NODE) continue;
                                Element actionElement = (Element) actions.item(k);
                                StringBuilder actionString = new StringBuilder();
                                String realLocator = actionElement.getElementsByTagName("locator").item(0).getTextContent();

                                String locator = "${" + realLocator + "}";
                                StringBuilder locatorAndXpath = new StringBuilder().append(locator).append("\t").append(dataMap.get(realLocator).get(0)).append("\n");
                                if (header.indexOf(locatorAndXpath.toString()) == -1)
                                    header.append(locatorAndXpath);
                                String text = null;
                                if (actionElement.getElementsByTagName("text").getLength() > 0) {
                                    text = actionElement.getElementsByTagName("text").item(0).getTextContent();
                                }
                                actionString.append("\t").append(actionElement.getElementsByTagName("type").item(0).getTextContent()).append("\t").append(locator);
                                if (text != null) {
                                    allText.add(text);
                                    actionString.append("\t").append(text).append("\n");
//                                blockOfActions.append("\t").append(text).append("\n");
                                }
                                else {
                                    actionString.append("\n");

                                }
                                blockOfActions.append(actionString);
                            }
                            Collections.sort(allText);
                            String andOfText = String.join(" & ", allText);
                            assertionStack.push(andOfText);

                        }
                        j++;
                    }
                    Element cur = parentLogicOfActions.get(j);
                    String expectedUrl = cur.getElementsByTagName("url").item(0).getTextContent();
                    assertionStack.push(expectedUrl);
                    blockOfActions.append("\tLocation should be\t").append(expectedUrl).append("\n");
                    String assertionString = String.join(" & ", assertionStack.stream().toList());
                    System.out.println(assertionString);
                    List<String> realDataList = dataMap.get(assertionString);
                    List<StringBuilder> realBlockOfCode = new ArrayList<>();
                    for (String datas: realDataList) {
                        StringBuilder newBlock = new StringBuilder(blockOfActions);
                        String[] variable = assertionString.split(" & ");
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
                    lines.put(lines.size(), realBlockOfCode);
//                    if (!type.equals("Verify URL")) {
//                        if (!type.equals("or") && !type.equals("and")) {
//                            List<StringBuilder> list = new ArrayList<>();
//                            StringBuilder actionString = new StringBuilder();
//                            String realLocator = cur.getElementsByTagName("locator").item(0).getTextContent();
//                            String locator = "${" + realLocator + "}";
//                            String text = null;
//                            if (cur.getElementsByTagName("text").getLength() > 0) {
//                                text = cur.getElementsByTagName("text").item(0).getTextContent();
//                            }
//                            actionString.append("\t").append(type).append("\t").append(locator);
//                            if (text != null) {
//                                actionString.append("\t").append(text).append("\n");
//                            }
//                            else {
//                                actionString.append("\n");
//                            }
//                            if (text != null) {
//                                for (String s: dataMap.get(text)) {
//                                    StringBuilder prev = new StringBuilder(actionString);
//                                    replace(text, s, actionString);
//                                    list.add(new StringBuilder(actionString));
//                                    actionString = new StringBuilder(prev);
//                                }
//                            } else {
//                                list.add(actionString);
//                            }
//                            lines.put(j, list);
//                            StringBuilder locatorAndXpath = new StringBuilder().append(locator).append("\t").append(dataMap.get(realLocator).get(0)).append("\n");
//                            if (header.indexOf(locatorAndXpath.toString()) == -1)
//                                header.append(locatorAndXpath);
//                        }
//                        else {
//                            List<List<Expression>> elementList = LogicParser.createDNFList(LogicParser.createAction(cur));
//                            List<StringBuilder> listOfChoices = new ArrayList<>();
//                            List<List<String>> placeHoldersEachAction = new ArrayList<>();
//                            List<List<StringBuilder>> actionStringList = new ArrayList<>();
//                            for (List<Expression> actions : elementList) {
//                                List<String> locators = new ArrayList<>();
//                                List<String> placeholders = new ArrayList<>();
//                                StringBuilder actionString = new StringBuilder();
//                                List<StringBuilder> temp = new ArrayList<>();
//                                for (Expression action: actions) {
//                                    actionString.append("\t").append(action.getType());
//                                    String realLocator = action.getLocator();
//                                    actionString.append("\t").append("${").append(realLocator).append("}");
//                                    StringBuilder locatorAndXpath = new StringBuilder().append("${").append(realLocator).append("}").append("\t").append(dataMap.get(realLocator).get(0)).append("\n");
//                                    if (header.indexOf(locatorAndXpath.toString()) == -1)
//                                        header.append(locatorAndXpath);
//                                    locators.add(action.getLocator());
//                                    if (action instanceof InputText &&  ((InputText)action).getValue() != null) {
//                                        placeholders.add(((InputText)action).getValue());
//                                        actionString.append("\t").append(((InputText)action).getValue());
//                                    }
//                                    actionString.append("\n");
//                                    temp.add(actionString);
//                                    actionString = new StringBuilder();
//                                }
//                                placeHoldersEachAction.add(placeholders);
//                                actionStringList.add(temp);
//                            }
//                            List<List<Integer>> subsets = Subset.subsets(actionStringList.size());
//                            DisjointSet disjointSet = new DisjointSet(placeHoldersEachAction.size());
//                            for (List<Integer> subset : subsets) {
//                                if (subset.isEmpty()) continue;
//                                StringBuilder subsetOfActions = new StringBuilder();
//                                Map<String, String> mapChoseData = new HashMap<>();
//                                for (int index1 = 0; index1 < subset.size() - 1; index1++) {
//                                    for (int index2 = index1 + 1; index2 < subset.size(); index2++) {
//                                        List<String> placeHolders1 = placeHoldersEachAction.get(subset.get(index1));
//                                        List<String> placeHolders2 = placeHoldersEachAction.get(subset.get(index2));
//                                        Set<String> check = new HashSet<>(placeHolders1);
//                                        check.retainAll(placeHolders2);
//                                        if (!check.isEmpty()) disjointSet.union(subset.get(index1), subset.get(index2));
//                                    }
//                                }
//                                for (int index1 = 0; index1 < subset.size(); index1++) {
//                                    int index = subset.get(index1);
//                                    if (placeHoldersEachAction.get(index).isEmpty()) {
//                                        subsetOfActions.append(actionStringList.get(index));
//                                        continue;
//                                    }
//                                    if (!mapChoseData.containsKey(placeHoldersEachAction.get(index).get(0))) {
//                                        List<String> list = new ArrayList<>(placeHoldersEachAction.get(index));
//                                        for (int k = index1 + 1; k < subset.size(); k++) {
//                                            if (disjointSet.sameSet(index, subset.get(k))) {
//                                                for (String placeholder : placeHoldersEachAction.get(subset.get(k))) {
//                                                    if (!list.contains(placeholder)) list.add(placeholder);
//                                                }
//                                            }
//                                        }
//                                        StringBuilder combinePlaceHolder = new StringBuilder(String.join(" & ", list));
//                                        int randomIndex = new Random().nextInt(dataMap.get(combinePlaceHolder.toString()).size());
//                                        String realDatas = dataMap.get(combinePlaceHolder.toString()).get(randomIndex);
//                                        String[] datas = realDatas.split(" & ");
//                                        for (int k = 0; k < list.size(); k++) {
//                                            mapChoseData.put(list.get(k), datas[k]);
//                                        }
//                                        List<StringBuilder> actionString = actionStringList.get(index);
//                                        for (int k = 0; k < placeHoldersEachAction.get(index).size(); k++) {
//                                            StringBuilder temp = new StringBuilder(actionString.get(k));
//                                            String placeholder = placeHoldersEachAction.get(index).get(k);
//                                            replace(placeholder, mapChoseData.get(placeholder), temp);
//                                            if (subsetOfActions.indexOf(temp.toString()) == -1)
//                                                subsetOfActions.append(temp);
//                                        }
//                                    } else {
//                                        List<StringBuilder> actionString = actionStringList.get(index);
//                                        for (int k = 0; k < placeHoldersEachAction.get(index).size(); k++) {
//                                            StringBuilder temp = new StringBuilder(actionString.get(k));
//                                            String placeholder = placeHoldersEachAction.get(index).get(k);
//                                            replace(placeholder, mapChoseData.get(placeholder), temp);
//                                            if (subsetOfActions.indexOf(temp.toString()) == -1)
//                                                subsetOfActions.append(temp);
//                                        }
//                                    }
//                                }
//                                listOfChoices.add(new StringBuilder(subsetOfActions));
//                                disjointSet.makeSet();
//                            }
//                            lines.put(j, listOfChoices);
//                        }
//                    }
                }
                createTestCase(0, lines, testScript, content,validations, testName, new AtomicInteger(1));
            }
            content.insert(0, header.append("\n"));
            bufferedWriter.append(content);
            bufferedWriter.close();
            DataPreprocessing.initInvalidDataParse(dataPath,outlinePath, outputScriptPath );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        createDataSheetV2("outline].xml", "src/main/resources/data/data_sheet.csv");
//        createScriptV2("outline].xml", "src/main/resources/data/data_sheet.csv", "test_saucedemo.robot");
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