package invalid;

import com.opencsv.CSVReader;
import invalid.strategies.Context;
import objects.action.*;
import objects.assertion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static invalid.AssertTestGen.assertMap;
import static invalid.AssertTestGen.initAssertTestGen;
import static invalid.FileWriteModule.writeStringsToFile;
import static invalid.InvalidTestGen.invalidTestCaseGen;
import static invalid.PythonTruthTableServer.logicParse;
import static invalid.strategies.Context.isAssertion;


public class DataPreprocessing {
    public static Dictionary<String, Vector<Vector<String>>> lineDict = new Hashtable<>();
    static Vector<Vector<String>> invalidDict = new Vector<>();
    public static Vector<String> temp = new Vector<>();

    public static HashMap<String, InputText> inputTextMap = new HashMap<>();
    public static HashMap<String, ClickElement> clickElementMap = new HashMap<>();
    public static HashMap<String, SelectCheckbox> selectCheckboxMap = new HashMap<>();
    public static HashMap<String, SelectRadioButton> selectRadioButtonMap = new HashMap<>();
    public static HashMap<String, SelectFromListByValue> selectFromListByValueMap = new HashMap<>();
    public static HashMap<String, ElementShouldBeVisible> elementShouldBeVisibleMap = new HashMap<>();
    public static HashMap<String, ElementShouldContain> elementShouldContainMap = new HashMap<>();
    public static HashMap<String, LocationShouldBe> locationShouldBeMap = new HashMap<>();
    public static HashMap<String, PageShouldContainElement> pageShouldContainElementMap = new HashMap<>();
    public static HashMap<String, PageShouldNotContainElement> pageShouldNotContainElementMap = new HashMap<>();
    static Map<String, List<String>> dataMap = new HashMap<>();
    public static Vector<String> assertHeap = new Vector<>();
    public static Vector<Vector<String>> assertVec = new Vector<>();

    public static void main(String[] args) {
        initInvalidDataParse("src/main/resources/data/data.csv", "src/main/resources/template/outline.xml", "src/main/resources/robot_test_file/final_test.robot");
    }

    public static void initInvalidDataParse(String csvPath, String xmlPath, String robotPath) {
        dataMap = createDataMap(csvPath);
        System.out.println(dataMap);

        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream(xmlPath)) {

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            // read from a project's resources folder
            Document doc = db.parse(is);

            if (doc.hasChildNodes()) {
                parseTestSuite(doc.getChildNodes());
                System.out.println(temp);
                System.out.println(invalidDict);
                System.out.println(lineDict);
                Vector<String> finalTest;
                if (assertVec.isEmpty()) {
                    finalTest = invalidTestCaseGen();
                } else {
                    finalTest = initAssertTestGen();
                }
                writeStringsToFile(finalTest, robotPath);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void parseTestSuite(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.getNodeName().equals("TestSuite")) {
                    parseUrl(tempNode.getChildNodes());
                }
            }
        }
    }

    public static void parseUrl(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.getNodeName().equals("url")) {
                    temp.add("   Open Browser   " + tempNode.getTextContent() + "   Edge");
                } else if (tempNode.getNodeName().equals("TestCase")) {
                    parseTest(tempNode.getChildNodes());
                    initInvalidDict();
                }
            }
        }
    }

    public static void parseTest(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (tempNode.getNodeName()) {
                    case "Scenario" -> temp.add(0, "Test-" + tempNode.getTextContent());
                    case "LogicExpressionOfActions" -> {
                        exprToMap(logicExpr(tempNode.getChildNodes(), false));
                        String encodedExpr = assertPreprocessing(count, (exprEncode(logicExpr(tempNode.getChildNodes(), false))));
                        Vector<Vector<String>> tb = truthTableParse(logicParse(encodedExpr), encodedExpr);
                        lineDict.put("LINE" + count, tb);
                        templateGen(tb, count);
                    }
                    case "Validation" -> parseValidation(tempNode.getChildNodes());
                }
            }
        }
    }


    //TODO: Assertion inside expr
    public static String assertPreprocessing(int count, String encodedExpr) {
//        if (!encodedExpr.contains("%26")) {
//            String dnfExpr = toDNF(encodedExpr);
//            dnfExpr = dnfExpr.substring(3, dnfExpr.length() - 1);
//            Vector<String> dnfVector = arrToVec(dnfExpr.split("And"));
//            String[] removed = {" ", ""};
//            dnfVector.replaceAll(String::trim);
//            dnfVector.removeAll(List.of(removed));
//            System.out.println(dnfVector);
//            String regex = "(\\w+)";
//            Pattern pattern = Pattern.compile(regex);
//
//            for (String s : dnfVector) {
//                Vector<String> exprVec = new Vector<>();
//                boolean isAss = false;
//                Matcher matcher = pattern.matcher(s);
//                while (matcher.find()) {
//                    String match = matcher.group();
//                    exprVec.add(match);
//                    if (isAssertion(match)) isAss = true;
//                }
//                if (isAss) {
//                    System.out.println(exprVec);
//                }
//            }
        if (isAssertion(encodedExpr)) {
            Vector<String> tempVec = new Vector<>(assertHeap);
            tempVec.add(0, encodedExpr);
            tempVec.add(0, "LINE" + count);
            assertVec.add(tempVec);
        } else {
            if (!assertHeap.contains("LINE" + count)) {
                assertHeap.add("LINE" + count);
            }
        }
        return encodedExpr;
    }

    public static void templateGen(Vector<Vector<String>> truthTable, int count) {
        for (String expr : truthTable.get(0)) {
            temp.add("#LINE" + count + "   " + expr);
        }
    }


    //TODO refactor: Done
    public static String exprEncode(String expr) {
        Context encodeContext = new Context();
        expr = encodeContext.exprEncode(expr);
        return expr;
    }


    //TODO refactor: Done
    public static void exprToMap(String expr) {
        Vector<String> value = arrToVec(expr.split("\\||%26|%28|%29"));
        String[] removed = {" ", ""};
        value.replaceAll(String::trim);
        value.removeAll(List.of(removed));
        Context exprToMapContext = new Context();
        for (String s : value) {
            exprToMapContext.exprToMap(s);
        }
    }

    public static void parseValidation(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.getNodeName().equals("surl")) {
                    temp.add("   Should Go To   " + tempNode.getTextContent());
                }
            }
        }
    }

    //TODO refactor
    public static String logicExpr(NodeList nodeList, boolean isChild) {
        String type = null;
        StringBuilder temp = new StringBuilder();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (tempNode.getNodeName()) {
                    case "type" -> type = switch (tempNode.getTextContent()) {
                        case "and" -> "and";
                        case "or" -> "or";
                        case "Input Text" -> "Input Text";
                        case "Click Element" -> "Click Element";
                        case "Verify URL" -> "Verify URL";
                        case "Verify Element Text" -> "Verify Element Text";
                        case "Select Checkbox" -> "Select Checkbox";
                        case "Select Radio Button" -> "Select Radio Button";
                        case "Select From List By Value" -> "Select From List By Value";
                        default -> type;
                    };
                    case "LogicExpressionOfActions" -> {
                        String expr = logicExpr(tempNode.getChildNodes(), true);
                        if (count == nodeList.getLength() - 2) {
                            temp.append(expr);
                        } else {
                            assert type != null;
                            if (type.equals("and")) {
                                temp.append(expr).append("%26");
                            } else if (type.equals("or")) {
                                temp.append(expr).append("|");
                            }
                        }
                    }
                    case "locator" -> {
                        assert type != null;
                        switch (type) {
                            case "Input Text" -> temp.append("Input Text   ").append(tempNode.getTextContent());
                            case "Verify Element Text" ->
                                    temp.append("Element Should Contain   ").append(tempNode.getTextContent());
                            case "Select From List By Value" ->
                                    temp.append("Select From List By Value   ").append(tempNode.getTextContent()).append("   ");
                            case "Click Element" -> {
                                return "Click Element   " + tempNode.getTextContent();
                            }
                            case "Select Checkbox" -> {
                                return "Select Checkbox   " + tempNode.getTextContent();
                            }
                        }
                    }
                    case "text" -> {
                        return temp + "   " + tempNode.getTextContent();
                    }
                    case "url" -> {
                        assert type != null;
                        if (type.equals("Verify URL")) {
                            return "Location Should Be   " + tempNode.getTextContent();
                        }
                    }
                    case "groupName" -> {
                        assert type != null;
                        if (type.equals("Select Radio Button")) {
                            temp.append("Select Radio Button   ").append(tempNode.getTextContent()).append("   ");
                        }
                    }
                    case "value" -> {
                        assert type != null;
                        if (type.equals("Select Radio Button")) {
                            return temp + tempNode.getTextContent();
                        } else if (type.equals("Select From List By Value")) {
                            return temp + tempNode.getTextContent();
                        }
                    }
                }
            }
        }
        if (isChild && (Objects.equals(type, "and") || Objects.equals(type, "or"))) {
            return "%28" + temp + "%29";
        } else {
            return temp.toString();
        }
    }

    public static Vector<Vector<String>> truthTableParse(String tbString, String encodeExpr) {
        if (!tbString.contains(" : ")) {
            tbString += " 1 : 1 0 : 0";
        }
        Vector<Vector<String>> tb = new Vector<>();
        tb.add(arrToVec(encodeExpr.split("\\||%26|%28|%29")));
        for (String header : tb.get(0)) {
            tbString = tbString.replaceAll(header, "");
        }
        tbString = "0 " + tbString.replaceAll("\\d", " $0 ").replaceAll("\\s+", " ").trim();
        Vector<String> vector = arrToVec(tbString.split(" : "));
        Vector<String> validVector = new Vector<>();
        Vector<String> invalidVector = new Vector<>();
        for (int i = 0; i < vector.size() - 1; i++) {
            vector.set(i, vector.get(i).substring(2) + " " + vector.get(i + 1).charAt(0));
        }
        vector.remove(vector.size() - 1);
        for (String tbLine : vector) {
            if (tbLine.charAt(tbLine.length() - 1) == '0') {
                invalidVector.add(tbLine);
            } else {
                validVector.add(tbLine);
            }
        }
        tb.add(validVector);
        tb.add(invalidVector);
        return tb;
    }

    public static Vector<String> arrToVec(String[] arr) {
        return new Vector<>(Arrays.asList(arr));
    }

    public static void initInvalidDict() {
        StringBuilder expr = new StringBuilder();
        Enumeration<String> enumeration = lineDict.keys();
        while (enumeration.hasMoreElements()) {
            String line = enumeration.nextElement();
            if (!isAssertion(lineDict.get(line).get(0).get(0))) {
                expr.append(line).append("%26");
            }
        }
        expr = new StringBuilder(expr.substring(0, expr.length() - 3));
        invalidDict = truthTableParse(logicParse(expr.toString()), expr.toString());
    }

    public static Map<String, List<String>> createDataMap(String path) {
        Map<String, List<String>> variables = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
            String[] words;
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
}