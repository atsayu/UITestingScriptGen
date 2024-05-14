package valid;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.bpodgursky.jbool_expressions.Expression;
import invalid.DataPreprocessing;
import invalid.PythonTruthTableServer;
import objects.assertion.LocationAssertion;
//import objects.normalAction.NormalAction;
import org.apache.tomcat.Jar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static valid.LogicParser.createTextExpression;


public class ScriptGen {

    public static Map<String, JSONArray> createValidIndex(String outlinePath, int index) throws IOException, ParseException {
        JSONObject outlineJSON = (JSONObject) new JSONParser().parse(new FileReader(outlinePath));
        JSONArray testcases = (JSONArray) outlineJSON.get("testcases");
        JSONObject testcase = (JSONObject) testcases.get(0);


        JSONArray actions = (JSONArray) testcase.get("actions");

        List<JSONObject> listForValid = new ArrayList<>();
        JSONArray listForInvalid = new JSONArray();

        int currentNumberOfAssert = 1;
        for (int i = 0; i < actions.size(); i++) {
            Object action = actions.get(i);
            JSONObject actionJSON = (JSONObject) action;

            if (currentNumberOfAssert < index) {
                listForValid.add(actionJSON);
            } else {
                listForInvalid.add(actionJSON);
            }

            String type = actionJSON.get("type").toString();
            if (type.contains("verify")) {
                currentNumberOfAssert++;
            }
        }

        List<List<List<JSONObject>>> backtrackList = createBacktrackList(listForValid);
        JSONArray validBlockSuite = new JSONArray();
        backtrackV2(backtrackList, validBlockSuite, new JSONArray(), 0);
        JSONArray storedData = (JSONArray) outlineJSON.get("storedData");

        Map<String, JSONArray> res = new HashMap<>();
        res.put("validBlocks", validBlockSuite);
        res.put("data", storedData);
        res.put("invalidBlock", listForInvalid);
        return res;

    }



    public static List<List<List<JSONObject>>> createBacktrackList(List<JSONObject> actions) {
        List<List<List<JSONObject>>> res = new ArrayList<>();
        for (Object action: actions) {
            JSONObject actionJSON = (JSONObject) action;
            List<List<JSONObject>> listForCurAction = new ArrayList<>();
            List<List<JSONObject>> DNFList = LogicParser.createDNFListV2(LogicParser.createActionV2(actionJSON));
            List<JSONObject> allActionCombination = new ArrayList<>();
            for (List<JSONObject> andOfSingleActions: DNFList) {
                List<JSONObject> combination = new ArrayList<>();
                for (JSONObject singleAction: andOfSingleActions) {
                    combination.add(singleAction);
                    allActionCombination.add(singleAction);
                }
                listForCurAction.add(combination);
            }
            String type = actionJSON.get("type").toString();
            if (type.equals("and") || type.equals("or")) listForCurAction.add(allActionCombination);
            res.add(listForCurAction);
        }
        return res;
    }
    public static void createDataSheetForInvalid (String outlinePath, String datasheetPath) throws IOException, ParseException {
        JSONObject outlineJSON = (JSONObject) new JSONParser().parse(new FileReader(outlinePath));
        JSONArray testcases = (JSONArray) outlineJSON.get("testcases");
        JSONObject testcase = (JSONObject) testcases.get(0);

        JSONArray actions = (JSONArray) testcase.get("actions");

        List<List<String>> backtrackVariableList = new ArrayList<>();
        for (Object action: actions) {
            JSONObject actionJSON = (JSONObject) action;
            String type = actionJSON.get("type").toString();
            if (type.equals("verifyURL")) {
                String url = actionJSON.get("url").toString();
                List<String> urlList = new ArrayList<>();
                urlList.add(url);
                backtrackVariableList.add(urlList);
                continue;
            }
            if (type.equals("click") || type.equals("open")) continue;
            List<String> stringList = new ArrayList<>();
            Expression<String> exprString =  LogicParser.createTextExpression(actionJSON);
            String[] andString = exprString.toLexicographicString().split(" \\| ");
            for (int i = 0; i < andString.length; i++) {
                String s = andString[i];
                if (s.charAt(0) == '(') {
                    andString[i] = s.substring(1, s.length() - 1);
                }
            }
            List<String> combinationVariable = new ArrayList<>(List.of(andString));
            backtrackVariableList.add(combinationVariable);
        }
        List<String> dataSheetVariables = new ArrayList<>();
        backTrackVariable(backtrackVariableList, new StringBuilder(), 0, dataSheetVariables);
        System.out.println(dataSheetVariables);
        JSONArray storedData = (JSONArray) outlineJSON.get("storedData");
        List<StringBuilder> csvLines = new ArrayList<>();
        for (int i = 0; i < dataSheetVariables.size(); i++) {
            StringBuilder init = new StringBuilder(dataSheetVariables.get(i));
            csvLines.add(init);
        }
        JSONArray variables = (JSONArray) outlineJSON.get("variables");
        String[] variableString = new String[variables.size()];
        for (int i = 0; i < variableString.length; i++) {
            variableString[i] = variables.get(i).toString();
        }

        for (Object userData: storedData) {
            JSONObject jsonUserData = (JSONObject) userData;
            Map<String, String> dataMap = new HashMap<>();
            for (String s: variableString) {
                String realdata = jsonUserData.get(s).toString();
                String[] singleData = realdata.split("\\s*&\\s*|\\s*\\|\\s*");
                String[] singleVariable = s.split("\\s*&\\s*|\\s*\\|\\s*");
                for (int j = 0; j < singleVariable.length; j++) {
                    dataMap.put(singleVariable[j], singleData[j]);
                }
            }

            for (int i = 0; i < dataSheetVariables.size(); i++) {
                String[] variables2 = dataSheetVariables.get(i).split(" & ");
                StringBuilder newData = new StringBuilder(dataSheetVariables.get(i));
                for (String s: variables2) {
                    replace(s, dataMap.get(s).toString(), newData);
                }
                csvLines.get(i).append(",").append(newData);
            }
        }

//        System.out.println(csvLines);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(datasheetPath, true));
        for (StringBuilder line: csvLines) {
            bufferedWriter.append(line.toString());
        }
        bufferedWriter.close();

    }
    public static void backTrackVariable(List<List<String>> linesOfCombination,StringBuilder curVariable,  int n, List<String> dataSheetVariable) {
        if (n >= linesOfCombination.size()) {
            dataSheetVariable.add(curVariable.toString());
            return;
        } else {
            for (String combination: linesOfCombination.get(n)) {
                StringBuilder cur = new StringBuilder(curVariable);
                if (!cur.isEmpty()) cur.append(" & ").append(combination);
                else cur.append(combination);
                backTrackVariable(linesOfCombination, new StringBuilder(cur), n + 1, dataSheetVariable);
            }
        }
    }

    public static String getStringFromJSON(JSONObject action, JSONObject locatorMap) {
        StringBuilder s = new StringBuilder();
        switch (action.get("type").toString()) {
            case "open":
                s.append("\tOpen Browser\t").append(action.get("url").toString()).append("\tChrome").append("\n");
                break;
            case "click":
                s.append("\tCLick Element\t").append(locatorMap.get(action.get("describedLocator").toString()).toString()).append("\n");
                break;
            case "input":
                s.append("\tInput Text\t").append(locatorMap.get(action.get("describedLocator").toString()).toString()).append("\t").append(action.get("value").toString()).append("\n");
                break;
            case "verifyURL":
                s.append("\tLocation should be\t").append(action.get("url").toString()).append("\n");
                break;
        }
        return s.toString();
    }
    public static String convertJSONToTestScript(JSONArray testSuite, JSONObject locatorMap, String testName) {
        StringBuilder script = new StringBuilder("*** Setting ***\nLibrary\tSeleniumLibrary\n\n*** Test Cases ***\n");
        for (int i = 0; i < testSuite.size(); i++) {
            script.append("Valid Test ").append(testName).append(" ").append(i + 1).append("\n");
            JSONObject jsonTestcase = (JSONObject) testSuite.get(i);
            JSONArray actions = (JSONArray) jsonTestcase.get("actions");
            for (Object action: actions) {
                script.append(getStringFromJSON((JSONObject) action, locatorMap));
            }
        }
        return script.toString();
    }
    public static void createScriptV3(String outlinePath, String dataSheetPath, String robotFilePath) throws IOException, ParseException {
//        Map<String, List<String>> dataMap = createDataMap(dataSheetPath);

        JSONObject outlineObject = (JSONObject) new JSONParser().parse(new FileReader(outlinePath));
        JSONArray testcases = (JSONArray) outlineObject.get("testcases");
        JSONObject testcase = (JSONObject) testcases.get(0);
        StringBuilder script = new StringBuilder();
        JSONArray storedData = (JSONArray) outlineObject.get("storedData");
        JSONArray variablesJSON = (JSONArray) outlineObject.get("variables");
        String[] variableString = new String[variablesJSON.size()];

        for (int i = 0; i < variableString.length; i++) {
            variableString[i] = variablesJSON.get(i).toString();
        }

//        String testUrl = outlineObject.get("url").toString();
        JSONArray actions = (JSONArray) testcase.get("actions");
        String testName = testcase.get("scenario").toString();
        JSONArray testSuite = new JSONArray();
        for (int i = 0; i < storedData.size(); i++) {
            JSONObject dataSet = (JSONObject) storedData.get(i);
            Map<String, String> dataMap = new HashMap<>();

            //Store data to map
            for (String variable: variableString) {
                String userData = dataSet.get(variable).toString();
                String[] singleVariable = variable.split("\\s*&\\s*|\\s*\\|\\s*");
                String[] singleData = userData.split("\\s*&\\s*|\\s*\\|\\s*");
                for (int j = 0; j < singleVariable.length; j++) {
                    dataMap.put(singleVariable[j], singleData[j]);
                }
            }

            //Init List of List for backtracking

            List<List<List<JSONObject>>> backTrackList = new ArrayList<>();
            List<JSONObject> listForOpenAction = new ArrayList<>();
//            JSONObject openAction = new JSONObject();
//            openAction.put("type", "open");
//            openAction.put("url", testUrl);
//            listForOpenAction.add(openAction);
//            List<List<JSONObject>> listForOpen = new ArrayList<>();
//            listForOpen.add(listForOpenAction);
//            backTrackList.add(listForOpen);

            for (int j = 0; j < actions.size(); j++) {
                JSONObject action = (JSONObject) actions.get(j);
                List<List<JSONObject>> listForCurAction = new ArrayList<>();
                List<List<JSONObject>> DNFList = LogicParser.createDNFListV2(LogicParser.createActionV2(action));
                for (List<JSONObject> andOfSingleActions: DNFList) {
                    List<JSONObject> combination = new ArrayList<>();
                    for (JSONObject singleAction: andOfSingleActions) {
                        combination.add(createActionHaveData(singleAction, dataMap));
                    }
                    listForCurAction.add(combination);
                }
                backTrackList.add(listForCurAction);
            }
            backtrackV2(backTrackList, testSuite, new JSONArray(), 0);
        }
        System.out.println(testSuite);
        JSONObject locatorMap =  sendRequestToLocatorDetector(testSuite);
        StringBuilder csvLocatr = new StringBuilder();
        for (Object key: locatorMap.keySet()) {
            String keyString = (String) key;
            String realLocator = locatorMap.get(keyString).toString();
            csvLocatr.append(keyString).append(",").append(realLocator).append("\n");
        }
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter(dataSheetPath));
        csvWriter.append(csvLocatr);
        csvWriter.close();
        String robotScript = convertJSONToTestScript(testSuite, locatorMap, testName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(robotFilePath)));
        bufferedWriter.append(robotScript);
        bufferedWriter.close();


//        JSONArray flatActionList = new JSONArray();
//        script.append("\tOpen Browser\t").append(testUrl).append("\tChrome\n");
//        JSONObject open = new JSONObject();
//        open.put("type", "open");
//        open.put("url", testUrl);
//        flatActionList.add(open);
////        map.for
//        for (Object action: actions) {
//            JSONObject jsonAction = (JSONObject) action;
//            if (!jsonAction.get("type").equals("and")) {
//                JSONObject actionClone = new JSONObject(jsonAction);
//                switch (jsonAction.get("type").toString()) {
//                    case "input":
//                        if (jsonAction.get("type").equals("input")) actionClone.put("value", dataMap.get(jsonAction.get("value").toString()).get(0));
//                        script.append("");
//                }
//
//                if (jsonAction.get("type").equals("verifyURL")) actionClone.put("url", dataMap.get(jsonAction.get("url").toString()).get(0));
//                flatActionList.add(actionClone);
//
//            } else {
//                JSONArray children = (JSONArray) jsonAction.get("actions");
//                for (Object child: children) {
//                    JSONObject jsonChild = (JSONObject) child;
//                    JSONObject actionClone = new JSONObject(jsonChild);
//                    actionClone.put("value", dataMap.get(jsonChild.get("value").toString()).get(0));
//                    flatActionList.add(actionClone);
//                }
//            }
//        }
    }

    public static void backtrackV2 (List<List<List<JSONObject>>> actions, JSONArray testSuite, JSONArray actionInTestCase, int i) {
        if (i >= actions.size()) {
            JSONObject testcase = new JSONObject();
            testcase.put("actions", actionInTestCase);
            testSuite.add(testcase);
            return;
        } else {
            List<List<JSONObject>> combinations = actions.get(i);
            for (List<JSONObject> combination: combinations) {
                JSONArray newActions = new JSONArray();
                newActions.addAll(actionInTestCase);
                newActions.addAll(combination);
                backtrackV2(actions, testSuite, newActions, i + 1);
            }
        }
    }

    public static JSONObject createActionHaveData(JSONObject action, Map<String, String> dataMap) {
        JSONObject actionHaveData = new JSONObject(action);
        String variable = "";
        switch (action.get("type").toString()) {
            case "open":

                return actionHaveData;
            case "click":
                return actionHaveData;
            case "input":
                variable = actionHaveData.get("value").toString();
                actionHaveData.put("value", dataMap.get(variable));
                return actionHaveData;
            case "verifyURL":
                variable = actionHaveData.get("url").toString();
                actionHaveData.put("url", dataMap.get(variable));
                return actionHaveData;
        }
        actionHaveData.put("error", "wrong type");
        return actionHaveData;
    }

    public static void createDataSheetV3(String outlinePath, String datasheetPath) throws IOException, ParseException {
        JSONObject outlineJSON = (JSONObject) new JSONParser().parse(new FileReader(outlinePath));
        JSONArray variableAndData = (JSONArray) outlineJSON.get("data");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(datasheetPath));
        for (Object data: variableAndData) {
            String dataLine = (String) data;
            bufferedWriter.append(dataLine).append("\n");
        }
        bufferedWriter.close();
    }

    public static JSONObject sendRequestToLocatorDetector(JSONArray testsuite) {
        RestTemplate restTemplate = new RestTemplate();
        JSONArray testcases = new JSONArray();
        JSONObject testcase = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        JSONObject open = new JSONObject();
        open.put("type", "open");
        open.put("url", "https://www.saucedemo.com/");
        jsonArray.add(open);
        JSONObject user = new JSONObject();
        user.put("type", "input");
        user.put("describedLocator", "username");
        user.put("value", "standard_user");
        jsonArray.add(user);
        JSONObject pass = new JSONObject();
        pass.put("type", "input");
        pass.put("describedLocator", "password");
        pass.put("value", "secret_sauce");
        jsonArray.add(pass);
        JSONObject click = new JSONObject();
        click.put("type", "click");
        click.put("describedLocator", "login");
        jsonArray.add(click);
        JSONObject verify = new JSONObject();
        verify.put("type", "verifyUrl");
        verify.put("url", "https://www.saucedemo.com/inventory.html");
        jsonArray.add(verify);

        testcase.put("actions", jsonArray);
        testcases.add(testcase);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONArray> entity = new HttpEntity<>(testsuite,headers);
        JSONObject res = restTemplate.postForObject("http://localhost:8080/locator", entity,JSONObject.class);
        System.out.println(res.get("username"));
        return res;
    }
//    public static void createDataSheetV2(String outlinePath, String datasheetPath) {
//        try{
//            File newfile = new File(datasheetPath);
//            if (newfile.createNewFile()) {
//                System.out.println("Created " + datasheetPath + "!");
//            } else {
//                System.out.println("The file" + datasheetPath + "already exists!");
//            }
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(datasheetPath));
//            Object outlineObject = new JSONParser().parse(new FileReader(outlinePath));
//
//
//
//            JSONObject outlineJSON = (JSONObject) outlineObject;
//            JSONArray vars = (JSONArray) outlineJSON.get("variables");
//            String[] variableStrings = new String[vars.size()];
//            for (int i = 0; i < variableStrings.length; i++) {
//                variableStrings[i] = vars.get(i).toString();
//            }
//
//            JSONArray data = (JSONArray) outlineJSON.get("data");
//            List<List<String>> dataString = new ArrayList<>();
//            for (int i = 0; i < data.size(); i++) {
//
//                List<String> dataList = new ArrayList<>(List.of(data.get(i).toString().split(",")));
//                dataString.add(dataList);
//            }
//            Map<String, List<String>> variableData = new HashMap<>();
//            for (int i = 0; i < dataString.size(); i++) {
//                variableData.put(variableStrings[i], dataString.get(i));
//            }
//            StringBuilder content = new StringBuilder();
//            String url = outlineJSON.get("url").toString();
////            NodeList testcases = document.getElementsByTagName("TestCase");
//            JSONArray testcases = (JSONArray) outlineJSON.get("testcases");
////            List<Element> testCaseElements = new ArrayList<>();
////            for (int i = 0; i < testcases.getLength(); i++) {
////                Node testcase = testcases.item(i);
////                if (testcase.getNodeType() == Node.ELEMENT_NODE)
////                    testCaseElements.add((Element) testcase);
////            }
//            //This list will be passed to the finding locator API
//            List<String> locators = new ArrayList<>();
////            locators.add(url);
//            for (Object testcase: testcases) {
//                JSONObject testcaseJSON = (JSONObject) testcase;
////                boolean haveAssert = Boolean.parseBoolean(testcaseElement.getElementsByTagName("assert").item(0).getTextContent());
//                boolean haveAssert = (boolean) testcaseJSON.get("haveAssert");
//                JSONArray actions = (JSONArray) testcaseJSON.get("actions");
////                NodeList testCaseChildNodes = testcaseElement.getChildNodes();
////                List<Element> expressionActionElements2 = new ArrayList<>();
////                for (int i = 0; i < testCaseChildNodes.getLength(); i++) {
////                    Node childNode = testCaseChildNodes.item(i);
////                    if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals("LogicExpressionOfActions"))
////                        expressionActionElements2.add((Element) childNode);
////                }
//                if (haveAssert) {
//                    List<List<String>> beforeAssertActions = new ArrayList<>();
//                    Stack<String> assertionStack = new Stack<>();
//                    for (Object actionObj: actions) {
//                        JSONObject actionJSON = (JSONObject) actionObj;
//                        String type = actionJSON.get("type").toString();
//                        if (type.contains("verify")) {
//                            switch (type) {
//                                case "verifyURL":
////                                    System.out.println(expressionActionElement.getElementsByTagName("url").getLength());
//                                    assertionStack.add(actionJSON.get("url").toString());
//                                    String assertionExpression = String.join(" & ", assertionStack.stream().toList());
//                                    System.out.println(assertionExpression);
//                                    List<String> verifyList = new ArrayList<>();
//                                    verifyList.add(actionJSON.get("url").toString());
//                                    beforeAssertActions.add(verifyList);
//                                    StringBuilder andActions = new StringBuilder();
//                                    List<String> output = new ArrayList<>();
//                                    addActionAndAssert(beforeAssertActions, output, 0, new StringBuilder());
//                                    System.out.println(output);
//                                    for (String assertString: output) content.append(assertString).append(getDataFromMap(assertString, variableData)).append("\n");
//                            }
//                        }
//                        if (!type.equals("and") && !type.equals("or") && !type.contains("verify")) {
//                            NormalAction action = (NormalAction) LogicParser.createAction(actionJSON).getAllK().stream().toList().get(0);
//                            String elementLocator = action.getElementLocator();
//                            if (!locators.contains(elementLocator))
//                                locators.add(elementLocator);
//                            String value = action.getValue();
//                            if (value != null) {
//                                assertionStack.add(value);
//                                if (content.indexOf(value) == -1) content.append(value).append(getDataFromMap(value, variableData)).append("\n");
//                                List<String> curActionListString = new ArrayList<>();
//                                curActionListString.add(value);
//                                beforeAssertActions.add(curActionListString);
//                            }
//                        } else if(!type.contains("verify")) {
//                            List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(actionJSON));
//                            List<List<String>> texts = new ArrayList<>();
//                            for (List<Expression> actionList : dnfList) {
//                                List<String> textList = new ArrayList<>();
//                                for (Expression action : actionList) {
//                                    String elementLocator = ((NormalAction)action).getElementLocator();
//                                    if (!locators.contains(elementLocator))
//                                        locators.add(elementLocator);
//                                    String value = ((NormalAction) action).getValue();
//                                    if (value != null)
//                                        textList.add(value);
//                                }
//                                texts.add(new ArrayList<>(textList));
//                            }
//                            List<List<Integer>> subsets = Subset.subsets(texts.size());
//                            List<String> curActionListString = new ArrayList<>();
//                            for (List<Integer> subset : subsets) {
//                                if (subset.isEmpty()) continue;
//                                StringBuilder satisfy = new StringBuilder();
//                                for (int index : subset) {
//                                    List<String> textList = texts.get(index);
//                                    for (String text : textList) {
//                                        if (satisfy.indexOf(text) == -1) {
//                                            if (satisfy.isEmpty()) satisfy.append(text);
//                                            else satisfy.append(" & ").append(text);
//                                        }
//                                    }
//                                }
//                                assertionStack.add(satisfy.toString());
//                                curActionListString.add(satisfy.toString());
//                                if (content.indexOf(satisfy.toString()) == -1)
//                                    content.append(new StringBuilder(satisfy)).append(getDataFromMap(satisfy.toString(), variableData)).append("\n");
//                            }
//                            beforeAssertActions.add(curActionListString);
//
//                            //This block of code is for invalid test gen
//                            String action = LogicParser.createTextExpression(actionJSON).toString();
//                            System.out.println(action);
//                            if (action.charAt(0) == '(') {
//                                action = action.substring(1, action.length() - 1);
//                            }
//                            action = action.replaceAll("\\(", "%28");
//                            action = action.replaceAll("\\)", "%29");
//                            action = action.replaceAll("[\\&]", "%26");
//                            action = action.replaceAll("\\s", "");
//                            System.out.println(action);
//                            Vector<Vector<String>> tb = DataPreprocessing.truthTableParse(PythonTruthTableServer.logicParse(action), action);
//                            System.out.println(tb);
//                            Vector<String> invalids = tb.get(2);
//                            boolean[] variables = new boolean[tb.get(0).size()];
//                            for (String truthLine: invalids) {
//                                String[] values = truthLine.split(" ");
//                                System.out.println(values.length);
//                                for (int i = 0; i < variables.length; i++) {
//                                    if (values[i].equals("1")) variables[i] = true;
//                                }
//                            }
//                            for (int i = 0; i < variables.length; i++) {
//                                if (variables[i]) content.append(tb.get(0).get(i)).append(getDataFromMap(tb.get(0).get(i), variableData)).append("\n");
//                            }
//
//                        }
//
//                    }
//                } else {
//                    for (Object actionObj: actions) {
//                        JSONObject actionJSON = (JSONObject) actionObj;
//                        String type = actionJSON.get("type").toString();
//                        if (!type.equals("and") && !type.equals("or")) {
//                            NormalAction action = (NormalAction) LogicParser.createAction(actionJSON).getAllK().stream().toList().get(0);
//                            String elementLocator = action.getElementLocator();
//                            if (!locators.contains(elementLocator))
//                                locators.add(elementLocator);
//                            String value = action.getValue();
//                            if (value != null) {
//                                if (content.indexOf(value) == -1) {
//                                    content.append(value).append(getDataFromMap(value, variableData)).append("\n");
//                                }
//                            }
//                        } else {
//                            List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(actionJSON));
//                            List<List<String>> texts = new ArrayList<>();
//                            for (List<Expression> actionList : dnfList) {
//                                List<String> textList = new ArrayList<>();
//                                for (Expression action : actionList) {
//                                    String elementLocator = ((NormalAction)action).getElementLocator();
//                                    if (!locators.contains(elementLocator))
//                                        locators.add(elementLocator);
//                                    String value = ((NormalAction) action).getValue();
//                                    if (value != null)
//                                        textList.add(value);
//                                }
//                                texts.add(new ArrayList<>(textList));
//                            }
//                            List<List<Integer>> subsets = Subset.subsets(texts.size());
//                            for (List<Integer> subset : subsets) {
//                                if (subset.isEmpty()) continue;
//                                StringBuilder satisfy = new StringBuilder();
//                                for (int index : subset) {
//                                    List<String> textList = texts.get(index);
//                                    for (String text : textList) {
//                                        if (satisfy.indexOf(text) == -1) {
//                                            if (satisfy.isEmpty()) satisfy.append(text);
//                                            else satisfy.append(" & ").append(text);
//                                        }
//                                    }
//                                }
//                                if (content.indexOf(satisfy.toString()) == -1)
//                                    content.append(new StringBuilder(satisfy)).append(getDataFromMap(satisfy.toString(), variableData)).append("\n");
//                            }
//
//                            //This block of code is for invalid test gen
//                            String action = LogicParser.createTextExpression(actionJSON).toString();
//                            System.out.println(action);
//                            if (action.charAt(0) == '(') {
//                                action = action.substring(1, action.length() - 1);
//                            }
//                            action = action.replaceAll("\\(", "%28");
//                            action = action.replaceAll("\\)", "%29");
//                            action = action.replaceAll("[\\&]", "%26");
//                            action = action.replaceAll("\\s", "");
//                            System.out.println(action);
//                            Vector<Vector<String>> tb = DataPreprocessing.truthTableParse(PythonTruthTableServer.logicParse(action), action);
//                            System.out.println(tb);
//                            Vector<String> invalids = tb.get(2);
//                            boolean[] variables = new boolean[tb.get(0).size()];
//                            for (String truthLine: invalids) {
//                                String[] values = truthLine.split(" ");
//                                System.out.println(values.length);
//                                for (int i = 0; i < variables.length; i++) {
//                                    if (values[i].equals("1")) variables[i] = true;
//                                }
//                            }
//                            for (int i = 0; i < variables.length; i++) {
//                                if (variables[i]) content.append(tb.get(0).get(i)).append(getDataFromMap(tb.get(0).get(i), variableData)).append("\n");
//                            }
//
//                        }
//
//                    }
//                }
//            }
//            List<String> dectedLocator = fakeLocatorDectector(locators);
//            for (int i = 0; i < dectedLocator.size(); i++) {
//                StringBuilder variableAndXpath = new StringBuilder();
//                variableAndXpath.append(locators.get(i)).append(",").append(dectedLocator.get(i)).append("\n");
//                content.append(variableAndXpath);
//            }
//            bufferedWriter.append(content);
//            bufferedWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String getDataFromMap(String exprVariable, Map<String, List<String>> variableDataMap) {
        String[] singleVariable = exprVariable.split(" & ");
        String[] singleData = new String[singleVariable.length];
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < variableDataMap.get(singleVariable[0]).size(); i++) {
            for (int j = 0; j < singleData.length; j++) {
                singleData[j] = variableDataMap.get(singleVariable[j]).get(i);
            }
            res.append(",").append(String.join(" & ", singleData));
        }
        return res.toString();
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

    public static String getTestCaseScript(Map<Integer, List<StringBuilder>> lines, String testName) {
        StringBuilder script = new StringBuilder();
        createTestCase(0, lines, new StringBuilder(), script, testName, new AtomicInteger(1));
        return script.toString();
    }

//    public static void createScriptV2(String outlinePath, String dataPath, String outputScriptPath) {
//        Map<String, List<String>> dataMap = createDataMap(dataPath);
//        try {
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputScriptPath));
//            JSONObject outlineJSON = (JSONObject) new JSONParser().parse(new FileReader(outlinePath));
//
//            String url = outlineJSON.get("url").toString();
//            JSONArray testcases = (JSONArray) outlineJSON.get("testcases");
//            StringBuilder content = new StringBuilder();
//            content.append("*** Setting ***\nLibrary\tSeleniumLibrary\n\n*** Test Cases ***\n");
//            StringBuilder header = new StringBuilder("*** Variables ***\n");
//            for (int i = 0; i < testcases.size(); i++) {
//                JSONObject testcaseJSON = (JSONObject) testcases.get(i);
//                StringBuilder testScript = new StringBuilder();
//                testScript.append("\tOpen Browser\t").append(url).append("\tChrome\n");
//                testScript.append("\tMaximize Browser Window\n");
////                if (testcase.getNodeType() != Node.ELEMENT_NODE) continue;
//
//                String testName = testcaseJSON.get("scenario").toString();
//                StringBuilder validations = new StringBuilder();
//                boolean haveAssert = (boolean) testcaseJSON.get("haveAssert");
////
//                //Giả sử luôn có assert URL
////                NodeList nodes = testcaseElement.getChildNodes();
////                List<Element> parentLogicOfActions = new ArrayList<>();
////                for (int j = 0; j < nodes.getLength(); j++) {
////                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE && ((Element) nodes.item(j)).getTagName().equals("LogicExpressionOfActions"))
////                        parentLogicOfActions.add((Element) nodes.item(j));
////                }
//                JSONArray actions = (JSONArray) testcaseJSON.get("actions");
//                Map<Integer, List<StringBuilder>> lines = new HashMap<>();
//                if (haveAssert) {
//                    createScriptListHaveAssert(actions, dataMap, header, lines);
//                } else {
//                    createScriptListNoAssert(actions, dataMap, header, lines);
//                }
////                createTestCase(0, lines, testScript, content, testName, new AtomicInteger(1));
//                content.append(getTestCaseScript(lines, testName));
//            }
//
//            content.insert(0, header.append("\n"));
//            bufferedWriter.append(content);
//            bufferedWriter.close();
////            DataPreprocessing.initInvalidDataParse(dataPath,outlinePath, outputScriptPath );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static void createScriptListNoAssert(JSONArray actions, Map<String, List<String>> dataMap, StringBuilder header, Map<Integer, List<StringBuilder>> lines) {
//        for (Object actionObj: actions) {
//            JSONObject actionJSON = (JSONObject) actionObj;
//            String type = actionJSON.get("type").toString();
//            if (!type.equals("or") && !type.equals("and")) {
//                List<StringBuilder> realTestScriptsList = new ArrayList<>();
//                NormalAction actionObject = (NormalAction) LogicParser.createAction(actionJSON).getAllK().stream().toList().get(0);
//                String elementLocator = actionObject.getElementLocator();
//                StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
//                if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
//                String testScriptWithVariable = actionObject.exprToString();
//                String valueOfAction = actionObject.getValue();
//                if (valueOfAction != null) {
//                    for (String realValue : dataMap.get(valueOfAction)) {
//                        StringBuilder realTestScript = new StringBuilder("\t").append(testScriptWithVariable).append("\n");
//                        replace(valueOfAction, realValue, realTestScript);
//                        realTestScriptsList.add(realTestScript);
//                    }
//                } else {
//                    StringBuilder realTestScript = new StringBuilder("\t").append(testScriptWithVariable).append("\n");
//                    realTestScriptsList.add(realTestScript);
//                }
//                lines.put(lines.size(), realTestScriptsList);
//            } else {
//                List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(actionJSON));
//                List<StringBuilder> realTestScriptList = new ArrayList<>();
//                for (List<Expression> andOfActions: dnfList) {
//                    List<String> valueOfActionList = new ArrayList<>();
//                    StringBuilder testScriptWithVariable = new StringBuilder();
//                    for (Expression action: andOfActions) {
//                        NormalAction actionObject = (NormalAction) action;
//                        testScriptWithVariable.append("\t").append(actionObject.exprToString()).append("\n");
//                        String elementLocator = actionObject.getElementLocator();
//                        StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
//                        if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
//                        String valueOfAction = actionObject.getValue();
//                        if (valueOfAction != null) valueOfActionList.add(valueOfAction);
//                    }
//                    String andOfValues = String.join(" & ", valueOfActionList);
//                    for (String realAndOfValues: dataMap.get(andOfValues)) {
//                        String[] realValue = realAndOfValues.split(" & ");
//                        StringBuilder realTestScript = new StringBuilder(testScriptWithVariable);
//                        for (int j = 0; j < realValue.length; j++) {
//                            replace(valueOfActionList.get(j), realValue[j], realTestScript);
//                        }
//                        realTestScriptList.add(realTestScript);
//                    }
//                }
//                lines.put(lines.size(), realTestScriptList);
//            }
//        }
//    }

//    private static void createScriptListHaveAssert(JSONArray actions, Map<String, List<String>> dataMap, StringBuilder header, Map<Integer, List<StringBuilder>> lines) {
//        Stack<String> assertionStack = new Stack<>();
//        List<List<String>> beforeAssertionStack = new ArrayList<>();
//        List<List<String>> listOfBlockActions = new ArrayList<>();
//
//        for (int j = 0; j < actions.size(); j++) {
//
//            List<StringBuilder> possibleAction = new ArrayList<>();
//            while (!((JSONObject) actions.get(j)).get("type").toString().contains("verify")) {
//                JSONObject actionJSON = (JSONObject) actions.get(j);
//                String type = actionJSON.get("type").toString();
//                if (!type.equals("or") && !type.equals("and")) {
//                    NormalAction action = (NormalAction) LogicParser.createAction(actionJSON).getAllK().stream().toList().get(0);
//                    List<StringBuilder> list = new ArrayList<>();
//                    StringBuilder actionString = new StringBuilder();
//                    String elementLocator = action.getElementLocator();
//                    String locator = "${" + elementLocator + "}";
//                    String value = action.getValue();
//                    if (value != null) {
//                        List<String> valueList = new ArrayList<>();
//                        valueList.add(value);
//                        beforeAssertionStack.add(valueList);
//                    }
//                    actionString.append("\t").append(action.exprToString()).append("\n");
//                    List<String> singleActionString = new ArrayList<>();
//                    singleActionString.add(actionString.toString());
//                    listOfBlockActions.add(singleActionString);
//                    System.out.println(elementLocator);
//
//                    StringBuilder locatorAndXpath = new StringBuilder().append(locator).append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
//                    if (header.indexOf(locatorAndXpath.toString()) == -1)
//                        header.append(locatorAndXpath);
//                } else {
//                    List<List<Expression>> dnfList = LogicParser.createDNFList(LogicParser.createAction(actionJSON));
//                    Map<String, String> valueToAction = new HashMap<>();
//                    List<List<String>> texts = new ArrayList<>();
//                    for (List<Expression> actionList : dnfList) {
//                        List<String> textList = new ArrayList<>();
//                        for (Expression action : actionList) {
//                            String value = ((NormalAction) action).getValue();
//                            String elementLocator = ((NormalAction) action).getElementLocator();
//                            //Waiting for locator detection API
//                            StringBuilder elementLocatorXpath = new StringBuilder("${").append(elementLocator).append("}").append("\t").append(dataMap.get(elementLocator).get(0)).append("\n");
//                            if (header.indexOf(elementLocatorXpath.toString()) == -1) header.append(elementLocatorXpath);
//                            if (value != null) {
//                                textList.add(value);
//                                valueToAction.put(value, "\t" +action.exprToString() + "\n");
//                            }
//
//                        }
//                        texts.add(new ArrayList<>(textList));
//                    }
//                    List<List<Integer>> subsets = Subset.subsets(texts.size());
//                    List<String> curActionListString = new ArrayList<>();
//                    List<String> multipleActionList = new ArrayList<>();
//                    for (List<Integer> subset : subsets) {
//                        if (subset.isEmpty() || subset.size() > 1) continue;
//                        StringBuilder satisfy = new StringBuilder();
//                        StringBuilder satisfyAction = new StringBuilder();
//                        for (int index : subset) {
//                            List<String> textList = texts.get(index);
//                            for (String text : textList) {
//                                satisfyAction.append(valueToAction.get(text));
//                                if (satisfy.indexOf(text) == -1) {
//                                    if (satisfy.isEmpty()) satisfy.append(text);
//                                    else satisfy.append(" & ").append(text);
//                                }
//                            }
//                        }
//
//                        assertionStack.add(satisfy.toString());
//                        multipleActionList.add(satisfyAction.toString());
//                        curActionListString.add(satisfy.toString());
////                                if (content.indexOf(satisfy.toString()) == -1)
////                                    content.append(new StringBuilder(satisfy)).append("\n");
//                    }
//                    beforeAssertionStack.add(curActionListString);
//                    listOfBlockActions.add(multipleActionList);
//
//                }
//                j++;
//            }
//            List<StringBuilder> realBlockOfCode = new ArrayList<>();
//            JSONObject actionJSON = (JSONObject) actions.get(j);
//            String type = actionJSON.get("type").toString();
//            switch (type) {
//                case "verifyURL":
//                    String expectedUrl = actionJSON.get("url").toString();
//                    assertionStack.push(expectedUrl);
//                    LocationAssertion verifyActionObject = (LocationAssertion) LogicParser.createAction(actionJSON).getAllK().stream().toList().get(0);
//                    List<String> verifyList = new ArrayList<>();
//                    StringBuilder verifyAction = new StringBuilder();
//                    verifyAction.append("\tLocation should be\t").append(expectedUrl).append("\n");
//                    verifyList.add(verifyAction.toString());
//                    listOfBlockActions.add(verifyList);
//                    List<String> verifyText = new ArrayList<>();
//                    verifyText.add(expectedUrl);
//                    beforeAssertionStack.add(verifyText);
//                    String assertionString = String.join(" & ", assertionStack.stream().toList());
//                    System.out.println(assertionString);
//                    List<String> outputText = new ArrayList<>();
//                    addActionAndAssert(beforeAssertionStack, outputText, 0, new StringBuilder());
//                    List<String> outputAction = new ArrayList<>();
//                    addActionAndAssertAction(listOfBlockActions, outputAction, 0, new StringBuilder());
//                    for (int outputIndex = 0; outputIndex < outputText.size(); outputIndex++) {
//                        List<String> realDataList = dataMap.get(outputText.get(outputIndex));
//                        for (String datas : realDataList) {
//                            StringBuilder newBlock = new StringBuilder(outputAction.get(outputIndex));
//                            String[] variable = outputText.get(outputIndex).split(" & ");
//                            String[] data = datas.split(" & ");
//                            Map<String, String> variableData = new HashMap<>();
//                            StringBuilder realBlock = new StringBuilder();
//                            for (int k = 0; k < variable.length; k++) {
//                                int startIndex = newBlock.indexOf(variable[k]);
//                                System.out.println(variable[k]);
//                                newBlock.replace(startIndex, startIndex + variable[k].length(), data[k]);
//                            }
//                            realBlockOfCode.add(newBlock);
//                        }
//                    }
//                    break;
//                case "Page Element Assertion":
//                    String assertElementLocator = actionJSON.get("url").toString();
////                                PageElementAssertion verifyElementObject = (PageElementAssertion) LogicParser.createAction(cur).getAllK().stream().toList().get(0);
//                    StringBuilder verifyScript = new StringBuilder();
//                    break;
//            }
//            lines.put(lines.size(), realBlockOfCode);
//        }
//    }

    public static void main(String[] args) throws IOException, ParseException {
        Map res = createValidIndex("src/main/resources/template/outline.json", 2);
        System.out.println(res);
//        sendRequestToLocatorDetector();
//        createDataSheetForInvalid("src/main/resources/template/outline.json", "src/main/resources/data/data_sheet.csv");
//        createScriptV3("src/main/resources/template/outline.json", "src/main/resources/data/data_sheet.csv", "test_saucedemo.robot");
//        createDataSheetV3("src/main/resources/template/outline.json", "src/main/resources/data/data_sheet.csv");
//        createDataSheetV2("src/main/resources/template/outline.json", "src/main/resources/data/data_sheet.csv");
//        createScriptV2("src/main/resources/template/outline.json", "src/main/resources/data/data_sheet.csv", "test_saucedemo.robot");
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