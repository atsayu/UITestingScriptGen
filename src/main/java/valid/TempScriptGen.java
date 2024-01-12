package valid;

import invalid.DataPreprocessing;
import invalid.PythonTruthTableServer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mockpage.Checkbox;
import mockpage.ClickableElement;
import mockpage.DropDownList;
import mockpage.Input;
import mockpage.RadioButton;
import mockpage.RadioButton.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TempScriptGen {

  public static void main(String[] args) {
    tempCreateDataSheetV2("E:\\LAB UI\\TestWebDemo\\SpringbootUITestingForm\\src\\main\\resources\\template\\outline.xml", "E:\\LAB UI\\TestWebDemo\\SpringbootUITestingForm\\src\\main\\resources\\data\\data.csv");
  }
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
      List<String> locatorsInput = new ArrayList<>();
      List<String> locatorsClickElement = new ArrayList<>();
      Map<String, List<String>> radioButton = new HashMap<>();
      Map<String, List<String>> checkbox = new HashMap<>();
      Map<String, List<String>> dropdown = new HashMap<>();

//      locators.add(url);
      for (Element testcaseElement: testCaseElements) {
        NodeList testCaseChildNodes = testcaseElement.getChildNodes();
        List<Element> expressionActionElements = new ArrayList<>();
        for (int i = 0; i < testCaseChildNodes.getLength(); i++) {
          Node childNode = testCaseChildNodes.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals("LogicExpressionOfActions"))
            expressionActionElements.add((Element) childNode);
        }
        for (Element expressionActionElement: expressionActionElements) {
          String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
          if (!type.equals("and") && !type.equals("or")) {
            if (type.equals("Input Text")) {
              String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
              if (!locatorsInput.contains(locator))
                locatorsInput.add(locator);
              if (expressionActionElement.getElementsByTagName("text").getLength() > 0) {
                String text = expressionActionElement.getElementsByTagName("text").item(0).getTextContent();
                if (content.indexOf(text) == -1)
                  content.append(text).append("\n");
              }
            }
            if (type.equals("Click Element")) {
              String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
              if (!locatorsClickElement.contains(locator)) {
                locatorsClickElement.add(locator);
              }
            }
            if (type.equals("Radio Button")) {
              String choice = expressionActionElement.getElementsByTagName("choice").item(0).getTextContent();
              String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
              if (radioButton.containsKey(question)) {
                if (!radioButton.get(question).contains(choice)) {
                  radioButton.get(question).add(choice);
                }
              } else {
                List<String> choices = new ArrayList<>();
                choices.add(choice);
                radioButton.put(question, choices);
              }
            }
            if (type.equals("Select List")) {
              String value = expressionActionElement.getElementsByTagName("value").item(0).getTextContent();
              String list = expressionActionElement.getElementsByTagName("list").item(0).getTextContent();
              if (dropdown.containsKey(list)) {
                if (!dropdown.get(list).contains(value)) {
                  dropdown.get(list).add(value);
                }
              } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                dropdown.put(list, values);
              }
            }
            if (type.equals("Select Checkbox")) {
              String answer = expressionActionElement.getElementsByTagName("answer").item(0).getTextContent();
              String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
              if (checkbox.containsKey(question)) {
                if (!checkbox.get(question).contains(answer)) {
                  checkbox.get(question).add(answer);
                }
              } else {
                List<String> answers = new ArrayList<>();
                answers.add(answer);
                checkbox.put(question, answers);
              }
            }


          } else {
            List<List<Action>> dnfList = LogicParser.createDNFList(LogicParser.createAction(expressionActionElement));
            List<List<String>> texts = new ArrayList<>();
            for (List<Action> actionList : dnfList) {
              List<String> textList = new ArrayList<>();
              for (Action action : actionList) {
                String locator = action.getLocator();
                if (!locatorsInput.contains(locator))
                  locatorsInput.add(locator);
                if (action.getText() != null)
                  textList.add(action.getText());
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
            if (action.charAt(0) == '(') {
              action = action.substring(1, action.length() - 1);
            }
            action = action.replaceAll("\\(", "%28");
            action = action.replaceAll("\\)", "%29");
            action = action.replaceAll("[\\&]", "%26");
            action = action.replaceAll("\\s", "");
            System.out.println(action);
            Vector<Vector<String>> tb = DataPreprocessing.truthTableParse(
                PythonTruthTableServer.logicParse(action), action);
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
      Vector<String> vec_locatorsInput = new Vector<>(locatorsInput);
      Vector<String> vec_locatorsClickElement = new Vector<>(locatorsClickElement);
      Input ip = new Input();
      Vector<String> res_locatorsInput = ip.processDetectInputElement(vec_locatorsInput, url);
      Map<String, String> map_locatorsInput = new HashMap<>();  //map lưu locator_variable và locator_value(xpath) của các phần tử input
      for (int i = 0; i < vec_locatorsInput.size(); i++) {
        map_locatorsInput.put(vec_locatorsInput.get(i), res_locatorsInput.get(i));
      }
      System.out.println("Detect input " + res_locatorsInput);
      ClickableElement cl = new ClickableElement();
      Vector<String> res_locatorsClickElement = cl.processDetectClickableElement(vec_locatorsClickElement, url);
      Map<String, String> map_locatorsClickElement = new HashMap<>();  //map lưu locator_variable và locator_value(xpath) của các phần tử click element
      for (int i = 0; i < vec_locatorsClickElement.size(); i++) {
        map_locatorsClickElement.put(vec_locatorsClickElement.get(i), res_locatorsClickElement.get(i));
      }
      System.out.println("Detect click " + res_locatorsClickElement);
      RadioButton rb = new RadioButton();
      Map<Pair<String, String>, Pair<String, String>> res_radioButton = rb.processDetectRadioButtonElement(radioButton, url);  // map lưu pair[question, choice] và pair[group name, value] của radio button
      System.out.print("Detect radio button ");
      for (Entry<Pair<String, String>, Pair<String, String>> entry : res_radioButton.entrySet()) {
        Pair<String, String> pair1 = entry.getKey();
        Pair<String, String> pair2 = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + pair2.getFirst() + " " + pair2.getSecond());
      }

      DropDownList ddl = new DropDownList();
      Map<DropDownList.Pair<String, String>, DropDownList.Pair<String, String>> res_dropdown = ddl.processDetectDropdownList(dropdown, url); // map lưu pair[list, value] và pair[locator của select, value của option]
      System.out.println("Detect dropdown ");
      for (Entry<DropDownList.Pair<String, String>, DropDownList.Pair<String, String>> entry : res_dropdown.entrySet()) {
        DropDownList.Pair<String, String> pair1 = entry.getKey();
        DropDownList.Pair<String, String> pair2 = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + pair2.getFirst() + " " + pair2.getSecond());
      }

      Checkbox cb = new Checkbox();
      Map<Checkbox.Pair<String, String>, String> res_checkbox = cb.processDetectCheckboxElement(checkbox, url); // map lưu pair[question, answer] và locator(xpath) của checkbox
      System.out.print("Detect checkbox ");
      for (Entry<Checkbox.Pair<String, String>, String> entry : res_checkbox.entrySet()) {
        Checkbox.Pair<String, String> pair1 = entry.getKey();
        String xpath = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + xpath);
      }

      bufferedWriter.append(content);
      bufferedWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static void tempCreateDataSheetV2(String outline, String datasheetPath) {
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
//      List<Element> testCaseElements = new ArrayList<>();
//      for (int i = 0; i < testcases.getLength(); i++) {
//        Node testcase = testcases.item(i);
//        if (testcase.getNodeType() == Node.ELEMENT_NODE)
//          testCaseElements.add((Element) testcase);
//      }
      //This list will be passed to the finding locator API
      List<String> locatorsInput = new ArrayList<>();
      List<String> locatorsClickElement = new ArrayList<>();
      Map<String, List<String>> radioButton = new HashMap<>();
      Map<String, List<String>> checkbox = new HashMap<>();
      Map<String, List<String>> dropdown = new HashMap<>();

//      locators.add(url);
//      for (Element testcaseElement: testCaseElements) {
//        NodeList testCaseChildNodes = testcaseElement.getChildNodes();
//        List<Element> expressionActionElements = new ArrayList<>();
//        for (int i = 0; i < testCaseChildNodes.getLength(); i++) {
//          Node childNode = testCaseChildNodes.item(i);
//          if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals("LogicExpressionOfActions"))
//            expressionActionElements.add((Element) childNode);
//        }
      List<Element> expressionActionElements = new ArrayList<>();
      for (int i = 0; i < testcases.getLength(); i++) {
        searchLogicExpressionOfActions(testcases.item(i), expressionActionElements);
      }
        for (Element expressionActionElement: expressionActionElements) {
          String type = expressionActionElement.getElementsByTagName("type").item(0).getTextContent();
          System.out.println(type);
          if (!type.equals("and") && !type.equals("or")) {
            if (type.equals("Input Text")) {
              String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
              if (!locatorsInput.contains(locator))
                locatorsInput.add(locator);
              if (expressionActionElement.getElementsByTagName("text").getLength() > 0) {
                String text = expressionActionElement.getElementsByTagName("text").item(0).getTextContent();
                if (content.indexOf(text) == -1)
                  content.append(text).append("\n");
              }
            }
            if (type.equals("Click Element")) {
              String locator = expressionActionElement.getElementsByTagName("locator").item(0).getTextContent();
              if (!locatorsClickElement.contains(locator)) {
                locatorsClickElement.add(locator);
              }
            }
            if (type.equals("Radio Button")) {
              String choice = expressionActionElement.getElementsByTagName("choice").item(0).getTextContent();
              String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
              if (radioButton.containsKey(question)) {
                if (!radioButton.get(question).contains(choice)) {
                  radioButton.get(question).add(choice);
                }
              } else {
                List<String> choices = new ArrayList<>();
                choices.add(choice);
                radioButton.put(question, choices);
              }
            }
            if (type.equals("Select List")) {
              String value = expressionActionElement.getElementsByTagName("value").item(0).getTextContent();
              String list = expressionActionElement.getElementsByTagName("list").item(0).getTextContent();
              if (dropdown.containsKey(list)) {
                if (!dropdown.get(list).contains(value)) {
                  dropdown.get(list).add(value);
                }
              } else {
                List<String> values = new ArrayList<>();
                values.add(value);
                dropdown.put(list, values);
              }
            }
            if (type.equals("Select Checkbox")) {
              String answer = expressionActionElement.getElementsByTagName("answer").item(0).getTextContent();
              String question = expressionActionElement.getElementsByTagName("question").item(0).getTextContent();
              if (checkbox.containsKey(question)) {
                if (!checkbox.get(question).contains(answer)) {
                  checkbox.get(question).add(answer);
                }
              } else {
                List<String> answers = new ArrayList<>();
                answers.add(answer);
                checkbox.put(question, answers);
              }
            }


          }
        }

      Vector<String> vec_locatorsInput = new Vector<>(locatorsInput);
      Vector<String> vec_locatorsClickElement = new Vector<>(locatorsClickElement);
      Input ip = new Input();
      Vector<String> res_locatorsInput = ip.processDetectInputElement(vec_locatorsInput, url);
      Map<String, String> map_locatorsInput = new HashMap<>();  //map lưu locator_variable và locator_value(xpath) của các phần tử input
      for (int i = 0; i < vec_locatorsInput.size(); i++) {
        map_locatorsInput.put(vec_locatorsInput.get(i), res_locatorsInput.get(i));
      }
      System.out.println("Detect input " + res_locatorsInput);
      ClickableElement cl = new ClickableElement();
      Vector<String> res_locatorsClickElement = cl.processDetectClickableElement(vec_locatorsClickElement, url);
      Map<String, String> map_locatorsClickElement = new HashMap<>();  //map lưu locator_variable và locator_value(xpath) của các phần tử click element
      for (int i = 0; i < vec_locatorsClickElement.size(); i++) {
        map_locatorsClickElement.put(vec_locatorsClickElement.get(i), res_locatorsClickElement.get(i));
      }
      System.out.println("Detect click " + res_locatorsClickElement);
      RadioButton rb = new RadioButton();
      Map<Pair<String, String>, Pair<String, String>> res_radioButton = rb.processDetectRadioButtonElement(radioButton, url);  // map lưu pair[question, choice] và pair[group name, value] của radio button
      System.out.print("Detect radio button ");
      for (Entry<Pair<String, String>, Pair<String, String>> entry : res_radioButton.entrySet()) {
        Pair<String, String> pair1 = entry.getKey();
        Pair<String, String> pair2 = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + pair2.getFirst() + " " + pair2.getSecond());
      }

      DropDownList ddl = new DropDownList();
      Map<DropDownList.Pair<String, String>, DropDownList.Pair<String, String>> res_dropdown = ddl.processDetectDropdownList(dropdown, url); // map lưu pair[list, value] và pair[locator của select, value của option]
      System.out.println("Detect dropdown ");
      for (Entry<DropDownList.Pair<String, String>, DropDownList.Pair<String, String>> entry : res_dropdown.entrySet()) {
        DropDownList.Pair<String, String> pair1 = entry.getKey();
        DropDownList.Pair<String, String> pair2 = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + pair2.getFirst() + " " + pair2.getSecond());
      }

      Checkbox cb = new Checkbox();
      Map<Checkbox.Pair<String, String>, String> res_checkbox = cb.processDetectCheckboxElement(checkbox, url); // map lưu pair[question, answer] và locator(xpath) của checkbox
      System.out.print("Detect checkbox ");
      for (Entry<Checkbox.Pair<String, String>, String> entry : res_checkbox.entrySet()) {
        Checkbox.Pair<String, String> pair1 = entry.getKey();
        String xpath = entry.getValue();
        System.out.println(pair1.getFirst() + " " + pair1.getSecond() + " " + xpath);
      }

      bufferedWriter.append(content);
      bufferedWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static void searchLogicExpressionOfActions(Node testcase, List<Element> expressionActionElements) {
    if (testcase.getNodeType() == Node.ELEMENT_NODE && ((Element) testcase).getTagName().equals("LogicExpressionOfActions")) {
      expressionActionElements.add((Element) testcase);
    }
    for (int i = 0; i < testcase.getChildNodes().getLength(); i++) {
      searchLogicExpressionOfActions(testcase.getChildNodes().item(i), expressionActionElements);
    }
  }



}
