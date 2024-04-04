package invalid;

import com.opencsv.CSVReader;
import invalid.strategies.Context;
import objects.assertion.LocationAssertion;
import objects.assertion.PageElementAssertion;
import objects.normalAction.ClickElement;
import objects.normalAction.InputText;
import objects.normalAction.SelectRadioButton;
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

import static invalid.AssertTestGen.assertTestGenInit;
import static invalid.FileWriteModule.writeStringsToFile;
import static invalid.PythonTruthTableServer.dnfParse;
import static invalid.PythonTruthTableServer.logicParse;


public class DataPreprocessing {
    public static Dictionary<String, Vector<Vector<Vector<String>>>> lineDict = new Hashtable<>();
    static Vector<Vector<String>> invalidDict = new Vector<>();
    public static Vector<String> temp = new Vector<>();

    public static HashMap<String, InputText> inputTextMap = new HashMap<>();
    public static HashMap<String, ClickElement> clickElementMap = new HashMap<>();
    public static HashMap<String, SelectRadioButton> selectRadioButtonMap = new HashMap<>();
    public static HashMap<String, LocationAssertion> locationShouldBeMap = new HashMap<>();
    public static HashMap<String, PageElementAssertion> pageShouldContainElementMap = new HashMap<>();
    static Map<String, List<String>> dataMap = new HashMap<>();

    public static void main(String[] args) {
        initInvalidDataParse("src/main/resources/data_test_invalid/all_click3.csv", "src/main/resources/data_test_invalid/all_click3.xml", "src/main/resources/robot_test_file/final_test.robot");
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
                writeStringsToFile(assertTestGenInit(), robotPath);
//                Vector<String> finalTest = invalidTestCaseGen();
//                writeStringsToFile(finalTest, robotPath);
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
                    temp.add("\tOpen Browser\t" + tempNode.getTextContent() + "\tChrome");
                } else if (tempNode.getNodeName().equals("TestCase")) {
                    parseTest(tempNode.getChildNodes());
                    initInvalidDict();
                }
            }
        }
    }

    public static void parseTest(NodeList nodeList) {
        int line = 1;
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (tempNode.getNodeName()) {
                    case "Scenario" -> temp.add(0, "Invalid-Test-" + tempNode.getTextContent());
                    case "LogicExpressionOfActions" -> {
                        exprToMap(logicExpr(tempNode.getChildNodes(), false));
                        String encodedExpr = exprEncode(logicExpr(tempNode.getChildNodes(), false));
                        lineDict.put("LINE" + line, dnfParse(encodedExpr));
                        line++;
                    }
                }
            }
        }
        templateGen();
    }

    public static void templateGen() {
        int i = 1;
        while (lineDict.get("LINE" + i) != null) {
            Vector<String> headerVec = new Vector<>();
            for (int j = 0; j < lineDict.get("LINE" + i).size(); j++) {
                for (int k = 0; k < lineDict.get("LINE" + i).get(j).get(0).size(); k++) {
                    String header = lineDict.get("LINE" + i).get(j).get(0).get(k);
                    if (!headerVec.contains(header)) {
                        headerVec.add(header);
                    }
                }
            }
            for (String s : headerVec) {
                temp.add("LINE" + i + "\t" + s);
            }
            i++;
        }
        temp.add("\tClose Browser");
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
                            case "Input Text" -> temp.append("Input Text\t").append(tempNode.getTextContent());
                            case "Click Element" -> {
                                return "Click Element\t" + tempNode.getTextContent();
                            }
                            case "Verify Element Text" ->
                                    temp.append("Element Should Contain\t").append(tempNode.getTextContent());
                        }
                    }
                    case "text" -> {
                        return temp + "\t" + tempNode.getTextContent();
                    }
                    case "url" -> {
                        assert type != null;
                        if (type.equals("Verify URL")) {
                            return "Location Should Be\t" + tempNode.getTextContent();
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
//        tb.add(arrToVec(encodeExpr.split("\\||%26|%28|%29")));
        String[] splitted = encodeExpr.split("\\||%26|%28|%29");
        String[] cleanSplitted = Arrays.stream(splitted).filter(split -> !split.isEmpty()).toArray(String[]::new);
        tb.add(arrToVec(cleanSplitted));
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
            expr.append(enumeration.nextElement()).append("%26");
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