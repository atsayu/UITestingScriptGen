package mockpage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static valid.TempScriptGen.contentAction;
import static valid.TempScriptGen.searchLogicExpressionOfActions;

public class Input extends ProcessDetectElement {
  Map<String, Element> mapStoreLocatorAndElement = new HashMap<>();
  Vector<Element> visitedInputElements = new Vector<>();
  Vector<String> locator_input = new Vector<>();
  int countDetectedElements = 0;

  public Vector<String> processDetectInputElement(Vector<String> input, String linkHtml) {
    Vector<String> result = new Vector<>();
    for (String s : input) {
      String normalize_s = normalize(s);
      locator_input.add(normalize_s);
    }
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    if (countDetectedElements < locator_input.size()) {
      for (Element e : visitedInputElements) {
        if (!mapStoreLocatorAndElement.containsValue(e) && locator_input.contains(normalize(e.attr("placeholder")))) {
          mapStoreLocatorAndElement.put(normalize(e.attr("placeholder")), e);
        }
      }
    }
    for (String locator : locator_input) {
      Element e = mapStoreLocatorAndElement.get(locator);
      String locator_value = getXpath(e);
      result.add(locator_value);
    }
    return result;
  }

  public void changeDomAndCreateMockPage(Vector<String> input, String linkHtml, Map<String, String> mapLocatorVariableAndValueVariable)
      throws IOException {
    for (String s : input) {
      String normalize_s = normalize(s);
      locator_input.add(normalize_s);
    }
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    if (countDetectedElements < locator_input.size()) {
      for (Element e : visitedInputElements) {
        if (!mapStoreLocatorAndElement.containsValue(e) && locator_input.contains(normalize(e.attr("placeholder")))) {
          mapStoreLocatorAndElement.put(normalize(e.attr("placeholder")), e);
        }
      }
    }
    for (String locator : input) {
      Element e = mapStoreLocatorAndElement.get(normalize(locator));
      String valueVariable = mapLocatorVariableAndValueVariable.get(locator);
      e.attr("cong", valueVariable);
    }
    File mockWebContent = new File("src/main/resources/html/mockweb.html");
    mockWebContent.createNewFile();
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mockWebContent));
    bufferedWriter.append(domTree.body().html());
    bufferedWriter.close();
  }

  public void changeDomAndCreateMockPageVersion2(String outline)
          throws IOException, ParserConfigurationException, SAXException {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      org.w3c.dom.Document document = builder.parse(new File(outline));
      String linkHtml = document.getElementsByTagName("url").item(0).getTextContent();
      NodeList testcases = document.getElementsByTagName("TestCase");
      List<org.w3c.dom.Element> expressionActionElements = new ArrayList<>();
      for (int i = 0; i < testcases.getLength(); i++) {
        searchLogicExpressionOfActions(testcases.item(i), expressionActionElements);
      }
    Vector<String> input = new Vector<>();
    Map<String, String> mapLocatorVariableAndValueVariable = new HashMap<>();
    Vector<String> allValueVariable = new Vector<>();
    Vector<String> valueVariableNotAssert = new Vector<>();
    String contentActions = "";
    for (int i = 0; i < expressionActionElements.size(); i++) {
      contentActions += contentAction(expressionActionElements.get(i), allValueVariable, valueVariableNotAssert,mapLocatorVariableAndValueVariable, input);
      if (i < expressionActionElements.size() - 1) {
        contentActions += "<br>";
      }
    }
    for (String s : input) {
      String normalize_s = normalize(s);
      locator_input.add(normalize_s);
    }
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      traversalDomTree(child);
    }
    if (countDetectedElements < locator_input.size()) {
      for (Element e : visitedInputElements) {
        if (!mapStoreLocatorAndElement.containsValue(e) && locator_input.contains(normalize(e.attr("placeholder")))) {
          mapStoreLocatorAndElement.put(normalize(e.attr("placeholder")), e);
        }
      }
    }

    for (String locator : input) {
      Element e = mapStoreLocatorAndElement.get(normalize(locator));
      String valueVariable = mapLocatorVariableAndValueVariable.get(locator);
      e.attr("cong", valueVariable);
    }
    String contentAllValueVariable = "";
    for (int i = 0; i < allValueVariable.size(); i++) {
      contentAllValueVariable += allValueVariable.get(i);
      if (i < allValueVariable.size() - 1) {
        contentAllValueVariable += ",";
      }
    }
    String contentValueVariableNotAssert = "";
    for (int i = 0; i < valueVariableNotAssert.size(); i++) {
      contentValueVariableNotAssert += valueVariableNotAssert.get(i);
      if (i < valueVariableNotAssert.size() - 1) {
        contentValueVariableNotAssert += ",";
      }
    }
    Element script_action = domTree.body().appendElement("input");
    script_action.attr("id", "script_action");
    script_action.attr("hidden", "true");
    script_action.attr("value", contentActions);
    Element all_value_variable = domTree.body().appendElement("input");
    all_value_variable.attr("id", "all_value_variable");
    all_value_variable.attr("hidden", "true");
    all_value_variable.attr("value", contentAllValueVariable);
    Element value_variable_not_assert = domTree.body().appendElement("input");
    value_variable_not_assert.attr("id", "value_variable_not_assert");
    value_variable_not_assert.attr("hidden", "true");
    value_variable_not_assert.attr("value", contentValueVariableNotAssert);

    File mockWebContent = new File("src/main/resources/html/mockweb.html");
    mockWebContent.createNewFile();
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mockWebContent));
    bufferedWriter.append(domTree.body().html());
    bufferedWriter.close();
  }

  @Override
  public void traversalDomTree(Element e) {
    if (isInputElement(e) && !visitedInputElements.contains(e) && !mapStoreLocatorAndElement.containsValue(e)) {
      visitedInputElements.add(e);
    }
    String text = e.ownText();
    String normalize_text = normalize(text);
    if (normalize_text != null && !normalize_text.matches("\\s*")) {
      if (locator_input.size() == 0) {
        return;
      }
      if (locator_input.contains(normalize_text)) {
        String valueOfForAttribute = "";
        if (e.hasAttr("for") && !e.attr("for").matches("\\s*")) {
          valueOfForAttribute = e.attr("for");
        }
        findElementCorrespondToText(e, normalize_text, valueOfForAttribute);
      }
    }

    for (Element child : e.children()) {
      traversalDomTree(child);
    }
  }

  /** Tìm phần tử input từ cây con với gốc là phần tử hiện tại hoặc tổ tiên của nó. */
  public void findElementCorrespondToText(Element e, String text, String valueOfForAttribute) {
    int temp = countDetectedElements;
    findElementInSubtree(e, text, valueOfForAttribute);
    if (temp == countDetectedElements) {
      findElementCorrespondToText(e.parent(), text, valueOfForAttribute);
    }
  }

  /** Tìm phần tử input trong cây con. */
  public void findElementInSubtree(Element root, String text, String valueOfForAttribute) {
    if (isInputElement(root) && !mapStoreLocatorAndElement.containsValue(root)) {
      if (!visitedInputElements.contains(root)) {
        visitedInputElements.add(root);
      }
      if (valueOfForAttribute.isEmpty()) {
        mapStoreLocatorAndElement.put(text, root);
        countDetectedElements++;
        visitedInputElements.remove(root);
        return;
      } else {
        String id = root.attr("id");
        if (id.equals(valueOfForAttribute)) {
          mapStoreLocatorAndElement.put(text, root);
          countDetectedElements++;
          visitedInputElements.remove(root);
          return;
        }
      }
    }
    for (Element child : root.children()) {
      findElementInSubtree(child, text, valueOfForAttribute);
    }

  }
  public String getXpath(Element e) {
    int attributes_size = e.attributesSize();
    String xpath = "";
    if (attributes_size > 0) {
      Attributes attr = e.attributes();
      xpath += "//" + e.tagName() + "[";
      boolean havingPreviousAttribute = false;
      for (Attribute temp : attr) {
        if (temp.getKey().equals("pattern")) {
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
      String textOfElement = e.ownText();
      if (havingPreviousAttribute && !textOfElement.matches("\\s*")) {
        xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
      } else {
        if (!textOfElement.matches("\\s*")) {
          xpath += "normalize-space()=" + "'" + textOfElement + "'";
        }
      }
      xpath += "]";
    }
    return xpath;
  }

  public boolean isInputElement(Element e) {
    if (e.tagName().equals("input")) {
      if (e.hasAttr("type") && (e.attr("type").equals("submit") || e.attr("type").equals("hidden"))) {
        return false;
      }
      return true;
    }
    if (e.tagName().equals("textarea")) {
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    Input ip = new Input();
    String outline = "E:\\LAB UI\\TestWebDemo\\SpringbootUITestingForm\\src\\main\\resources\\template\\outline.xml";
      try {
          ip.changeDomAndCreateMockPageVersion2(outline);
      } catch (IOException e) {
          throw new RuntimeException(e);
      } catch (ParserConfigurationException e) {
          throw new RuntimeException(e);
      } catch (SAXException e) {
          throw new RuntimeException(e);
      }
  }
}
