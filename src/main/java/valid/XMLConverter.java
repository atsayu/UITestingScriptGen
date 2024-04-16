package valid;



import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class XMLConverter {
    public static void convertJSONToXML(String jsonPath, String xmlPath) {
        try {
            String jsonString = readFileAsString(jsonPath);
            JSONObject jsonObject = new JSONObject(jsonString);
//            JSONArray testcase = (JSONArray) jsonObject.get("testcases");
//            JSONObject firstTest = (JSONObject) testcase.get(0);
//            JSONArray jsonactions = (JSONArray) firstTest.get("actions");
//            JSONObject openAction = (JSONObject) jsonactions.get(0);
//            String url = (String) openAction.get("url");
            String url = (String) jsonObject.get("url");
            jsonObject.remove("url");
            jsonObject.remove("variables");
            jsonObject.remove("data");
            jsonObject.remove("storedData");
            String xmlString = XML.toString(jsonObject);
            xmlString = "<TestSuite><url>" + url + "</url>" + xmlString + "</TestSuite>";
//            xmlString = "<TestSuite>" + xmlString + "</TestSuite>";
            String prettyXmlString = prettyPrintByTransformer(xmlString, 2,
                    true);
            try {
                FileWriter writer = new FileWriter(xmlPath);
                writer.write(prettyXmlString);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(xmlPath));
            NodeList testcases = doc.getElementsByTagName("testcases");

            for (int i = 0; i < testcases.getLength(); i++) {
                doc.renameNode(testcases.item(i),null, "TestCase");
            }
            NodeList scenarios = doc.getElementsByTagName("scenario");
            for (int i = 0; i < scenarios.getLength(); i++) {
                doc.renameNode(scenarios.item(i),null, "Scenario");
            }
            NodeList actions = doc.getElementsByTagName("actions");

            for (int j = 0; j < actions.getLength(); j++) {
                NodeList list = actions.item(j).getChildNodes();
                for (int k = 0; k < list.getLength(); k++) {
                    if (list.item(k) instanceof Element) {
                        Element root = (Element) list.item(k);
                        System.out.println(root.getNodeName());
                        break;
                    }
                }
                doc.renameNode(actions.item(j),null, "LogicExpressionOfActions");
            }
            NodeList type = doc.getElementsByTagName("type");
            for (int i = 0; i < type.getLength(); i++) {
                if (type.item(i).getTextContent().equals("input")) {
                    type.item(i).setTextContent("Input Text");
                }
                if (type.item(i).getTextContent().equals("click")) {
                    type.item(i).setTextContent("Click Element");
                }
                if (type.item(i).getTextContent().equals("verifyURL")) {
                    type.item(i).setTextContent("Verify URL");
                }
            }
            NodeList describedLocator = doc.getElementsByTagName("describedLocator");
            for (int i = 0; i < describedLocator.getLength(); i++) {
                doc.renameNode(describedLocator.item(i),null, "locator");
            }
            NodeList value = doc.getElementsByTagName("value");
            for (int i = 0; i < value.getLength(); i++) {
                doc.renameNode(value.item(i),null, "text");
            }
            NodeList haveAssert = doc.getElementsByTagName("haveAssert");
            for (int i = 0; i < haveAssert.getLength(); i++) {
                haveAssert.item(i).getParentNode().removeChild(haveAssert.item(i));
                i--;
            }
            NodeList actionList = doc.getElementsByTagName("LogicExpressionOfActions");
            for (int i = 0; i < actionList.getLength(); i++) {
                Node cur = actionList.item(i);
                if (cur.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) cur;
                    if (element.getElementsByTagName("type").item(0).getTextContent().equals("open")) {
                        cur.getParentNode().removeChild(cur);
                        break;
                    }
                }
            }
            NodeList listAction = doc.getElementsByTagName("LogicExpressionOfActions");
            for (int i = 0; i < listAction.getLength(); i++) {
                Node cur = listAction.item(i);
                if (cur.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) cur;
                    if (element.getElementsByTagName("type").item(0).getTextContent().equals("Input Text")) {
                        Node typeNode = element.getElementsByTagName("type").item(0);
                        Node locatorNode = element.getElementsByTagName("locator").item(0);
                        Node textNode = element.getElementsByTagName("text").item(0);
                        element.removeChild(typeNode);
                        element.removeChild(locatorNode);
                        element.removeChild(textNode);
                        element.appendChild(typeNode);
                        element.appendChild(locatorNode);
                        element.appendChild(textNode);
                    } else if (element.getElementsByTagName("type").item(0).getTextContent().equals("Click Element")) {
                        Node typeNode = element.getElementsByTagName("type").item(0);
                        Node locatorNode = element.getElementsByTagName("locator").item(0);
                        element.removeChild(typeNode);
                        element.removeChild(locatorNode);
                        element.appendChild(typeNode);
                        element.appendChild(locatorNode);
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
            String result_xml_String = stringWriter.toString();
            try {
                FileWriter writer = new FileWriter(xmlPath);
                writer.write(result_xml_String);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyPrintByTransformer(String xmlString, int indent, boolean ignoreDeclaration) {

        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ignoreDeclaration ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Writer out = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error occurs when pretty-printing xml:\n" + xmlString, e);
        }
    }

    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static void main(String[] args) {
            convertJSONToXML("src/main/resources/template/outline.json", "src/main/resources/template/outline.xml");
    }
}
