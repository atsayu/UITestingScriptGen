package mockpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class newSolve {

  static Vector<Element> interactableElements = new Vector<>();
  static Set<String> variable_list = new HashSet<>();
  static Set<String> visited = new HashSet<>();
  static Map<Pair<String, Element>, Double> storeVarEleAndWeight = new HashMap<>();
  static Map<Pair<String, Element>, Double> sortedMapStoreVarEleAndWeight = new LinkedHashMap<>();
  static Map<Pair<String,Element>, Vector<Attribute>> storeVarEleAndAttributesContainsVar = new HashMap<>();
  static Map<Element, String> mapElementAndLocatorVariable = new HashMap<>();
  static Map<String, Vector<String>> mapValueVariableAnData = new HashMap<>();
  static Vector<String> result = new Vector<>();
  static final double weightAttributeId = 0.5;
  static final double weightAttributeName = 0.2;
  static final double weightOfText = 0.1;
  static final double weightAnotherAttribute = 0.01;


  public static Vector<String> getLocator(String[] input) {
    int length = input.length;

    for (int i = 0; i < length; i++) {
      if (input[i].contains(":")) {
        String linkHtml = input[i];
        int j;
        for (j = i + 1; j < length; j++) {
          if (!input[j].contains(":")) {
            variable_list.add(input[j]);
          } else {
            break;
          }
        }
        processGetLocator(linkHtml);
        setUp();
        i = j - 1;
        if (i == length - 1) {
          break;
        }
      }
    }
    return result;
  }
  public static void processGetLocator(String linkHtml) {
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      searchInteractableElements(child);
    }
    setStoreVarEleAndWeight();
    List<Entry<Pair<String, Element>, Double>> list = new ArrayList<>(storeVarEleAndWeight.entrySet());
    Collections.sort(list, new Comparator<Entry<Pair<String, Element>, Double>>() {
      @Override
      public int compare(Entry<Pair<String, Element>, Double> o1, Entry<Pair<String, Element>, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });


    for (Entry<Pair<String, Element>, Double> entry : list) {
      sortedMapStoreVarEleAndWeight.put(entry.getKey(), entry.getValue());
    }
    matchVarAndELe();
  }

  public static void setUp() {
    interactableElements = new Vector<>();
    variable_list = new HashSet<>();
    visited = new HashSet<>();
    storeVarEleAndWeight = new HashMap<>();
    sortedMapStoreVarEleAndWeight = new LinkedHashMap<>();
    storeVarEleAndAttributesContainsVar = new HashMap<>();
  }
  public static class Pair<F, S> {

    private  F first;
    private  S second;

    public Pair() {

    }
    public Pair(F first, S second) {
      this.first = first;
      this.second = second;
    }

    public F getFirst() {
      return first;
    }

    public S getSecond() {
      return second;
    }
  }
  public static String getHtmlContent(String linkHtml) {
//    System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64-119\\chromedriver.exe");
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


  public static void searchInteractableElements(Element e) {
    String[] tagNameInteractableElement = {"input", "button", "a", "select", "textarea"};
    for (String s : tagNameInteractableElement) {
      if (e.tagName().equals(s)) {
        interactableElements.add(e);
        System.out.println(e);
      }
    }
    for (Element child : e.children()) {
      searchInteractableElements(child);
    }
  }

  public static String getXpath(String variable, Element interactableElement, Vector<Attribute> storeAttributesContainsVar) {
    int attributes_size = storeAttributesContainsVar.size();
    String xpath = "";
    boolean havingPreviousAttribute = false;
    if (attributes_size > 0) {
      xpath += "//" + interactableElement.tagName() + "[";
      for (Attribute temp : storeAttributesContainsVar) {
        if (havingPreviousAttribute) {
          xpath += " and " + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
        } else {
          xpath += "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'";
          havingPreviousAttribute = true;
        }
      }
    }
    Vector<String> wordsOfVariable = word_analysis(variable);
    String textOfElement = interactableElement.ownText();
    String analysisTextOfElement = beautifulString(textOfElement);
    boolean textOfElementHasVariable = false;
    for (String word : wordsOfVariable) {
      if (analysisTextOfElement.contains(word)) {
        textOfElementHasVariable = true;
      }
    }
    if (textOfElementHasVariable) {
      if (havingPreviousAttribute) {
        xpath += " and " + "normalize-space()=" + "'" + textOfElement + "'";
      } else {
        xpath += "normalize-space()=" + "'" + textOfElement + "'";
      }
    }
    xpath += "]";
    return xpath;
  }

  public static String beautifulString(String x) {
    String temp = "";
    String x_lower = x.toLowerCase();
    for (int i = 0; i < x_lower.length(); i++) {
      if ((x_lower.charAt(i) >= 'a' && x_lower.charAt(i) <= 'z') || (x_lower.charAt(i) >= '0'
              && x_lower.charAt(i) <= '9')) {
        temp += x_lower.charAt(i);
      }
    }
    return temp;
  }

  public static Vector<String> word_analysis(String variable) {
    Vector<String> single_word = new Vector<>();
    String[] words = variable.split("_");
    for (String x : words) {
      String x_value = "";
      for (int i = 0; i < x.length(); i++) {
        if ((x.charAt(i) >= 'a' && x.charAt(i) <= 'z') || (x.charAt(i) >= '0'
                && x.charAt(i) <= '9') || (x.charAt(i) >= 'A'
                && x.charAt(i) <= 'Z')) {
          x_value += x.charAt(i);
        }
      }
      boolean type = true;
      if (x_value.charAt(0) >= '0' && x_value.charAt(0) <= '9') {
        type = false;
      }
      String temp = "";
      temp += x_value.charAt(0);
      for (int i = 1; i < x_value.length(); i++) {
        if (x_value.charAt(i) >= 'A' && x_value.charAt(i) <= 'Z') {
          single_word.add(temp.toLowerCase());
          type = true;
          temp = "";
          temp += x_value.charAt(i);
        } else if (x_value.charAt(i) >= '0' && x_value.charAt(i) <= '9') {
          if (type) {
            single_word.add(temp.toLowerCase());
            temp = "";
            temp += x_value.charAt(i);
            type = false;
          } else {
            temp += x_value.charAt(i);
          }
        } else {
          temp += x_value.charAt(i);
        }
        if (i == x_value.length() - 1) {
          single_word.add(temp.toLowerCase());
        }
      }
    }

    return single_word;
  }

  public static double calculateWeight(String variable, Element e, Vector<Attribute> storeAttributesContainsVar) {
    Vector<String> wordsOfVariable = word_analysis(variable);
    int size = wordsOfVariable.size();
    double weight = 0;
    Attributes attributes = e.attributes();
    for (Attribute attr : attributes) {
      if (attr.getKey().equals("id")) {
        String analysis_id = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_id.contains(word)) {
            weight += weightAttributeId / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      } else if (attr.getKey().equals("name")) {
        String analysis_name = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_name.contains(word)) {
            weight += weightAttributeName / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      } else {
        String analysis_another_attribute = beautifulString(attr.getValue());
        boolean hasContainsVar = false;
        for (String word : wordsOfVariable) {
          if (analysis_another_attribute.contains(word)) {
            weight += weightAnotherAttribute / size;
            hasContainsVar = true;
          }
        }
        if (hasContainsVar) {
          storeAttributesContainsVar.add(attr);
        }
      }
    }
    String textOfElement = e.ownText();
    String analysisTextOfElement = beautifulString(textOfElement);
    for (String word : wordsOfVariable) {
      if (analysisTextOfElement.contains(word)) {
        weight += weightOfText / size;
      }
    }
    return weight;
  }

  public static void setStoreVarEleAndWeight() {
    for (String variable : variable_list) {
      for (Element e : interactableElements) {
        Pair<String, Element> mapVarEle = new Pair<>(variable, e);
        Vector<Attribute> storeAttributesContainsVar = new Vector<>();
        double weight = calculateWeight(variable, e, storeAttributesContainsVar);
        storeVarEleAndWeight.put(mapVarEle, weight);
        storeVarEleAndAttributesContainsVar.put(mapVarEle, storeAttributesContainsVar);
      }
    }
  }

  public static void matchVarAndELe () {
    for (Entry<Pair<String, Element>, Double> entry : sortedMapStoreVarEleAndWeight.entrySet()) {
      Pair<String, Element> pairVarAndEle = entry.getKey();
      String variable = pairVarAndEle.getFirst();
      Element e = pairVarAndEle.getSecond();

      if (variable_list.contains(variable) && interactableElements.contains(e) && entry.getValue() != 0) {
        Vector<Attribute> storeAttributesContainsVar = storeVarEleAndAttributesContainsVar.get(pairVarAndEle);
        System.out.println(entry.getValue());
        result.add(variable + ": xpath=" + getXpath(variable,e, storeAttributesContainsVar) + "\n");
        variable_list.remove(variable);
        interactableElements.remove(e);
      }
      if (variable_list.size() == 0 || interactableElements.size() == 0) {
        return;
      }
    }
  }

  public static void changDomAndCreateMockPage(String[] input, Map<String, String> mapLocatorVariableAndValueVariable ) throws IOException {
    int length = input.length;
    String linkHtml = "";
    for (int i = 0; i < length; i++) {
      if (input[i].contains(":")) {
        linkHtml = input[i];
        int j;
        for (j = i + 1; j < length; j++) {
          if (!input[j].contains(":")) {
            variable_list.add(input[j]);
          } else {
            break;
          }
        }
        i = j - 1;
        if (i == length - 1) {
          break;
        }
      }
    }
    String htmlContent = getHtmlContent(linkHtml);
    Document domTree = getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      searchInteractableElements(child);
    }
    setStoreVarEleAndWeight();
    List<Entry<Pair<String, Element>, Double>> list = new ArrayList<>(
            storeVarEleAndWeight.entrySet());
    Collections.sort(list, new Comparator<Entry<Pair<String, Element>, Double>>() {
      @Override
      public int compare(Entry<Pair<String, Element>, Double> o1,
                         Entry<Pair<String, Element>, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });

    for (Entry<Pair<String, Element>, Double> entry : list) {
      sortedMapStoreVarEleAndWeight.put(entry.getKey(), entry.getValue());
    }

    for (Entry<Pair<String, Element>, Double> entry : sortedMapStoreVarEleAndWeight.entrySet()) {
      Pair<String, Element> pairVarAndEle = entry.getKey();
      String variable = pairVarAndEle.getFirst();
      Element e = pairVarAndEle.getSecond();

      if (variable_list.contains(variable) && interactableElements.contains(e) && entry.getValue() != 0) {
        Vector<Attribute> storeAttributesContainsVar = storeVarEleAndAttributesContainsVar.get(pairVarAndEle);
        mapElementAndLocatorVariable.put(e, variable);
        result.add(variable + ": xpath=" + getXpath(variable,e, storeAttributesContainsVar) + "\n");
        variable_list.remove(variable);
        interactableElements.remove(e);
      }
      if (variable_list.size() == 0 || interactableElements.size() == 0) {
        break;
      }
    }
    for (Element e : childRoot) {
      traversalDom(domTree, e, mapLocatorVariableAndValueVariable);
    }
//    addNewElementToDom(domTree);
//    writeDomToHtmlFile(domTree, "src/main/resources/html/test.html");
    File mockWebContent = new File("src/main/resources/html/mockweb.html");
    mockWebContent.createNewFile();
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mockWebContent));
//    System.out.println(domTree.body().toString());
    bufferedWriter.append(domTree.body().html());
    bufferedWriter.close();
  }

  public static void traversalDom(Document domTree, Element e, Map<String, String> mapLocatorVariableAndValueVariable) {
    if (mapElementAndLocatorVariable.containsKey(e)) {
      String locatorVariable = mapElementAndLocatorVariable.get(e);
      String valueVariable = mapLocatorVariableAndValueVariable.get(locatorVariable);
      e.attr("cong", valueVariable);
    }
    for (Element child : e.children()) {
      traversalDom(domTree, child, mapLocatorVariableAndValueVariable);
    }
  }

  public static String  contentOfMockWeb(Document domTree) {
    String htmlContent = "<html>\n"
        + "<link\n"
        + "    href=\"https://fonts.googleapis.com/css?family=Montserrat\"\n"
        + "    rel=\"stylesheet\"\n"
        + "/>\n"
        + "<head>\n";
    Element head = domTree.head();
    for (Element child : head.children()) {
      htmlContent += String.valueOf(child);
      htmlContent += "\n";
    }
    htmlContent += "  <style>\n"
        + "    :root {\n"
        + "      --color-darker-grey: #dbdbdb;\n"
        + "      --color-grey: #f4f9fa;\n"
        + "      --color-white: #fefeff;\n"
        + "      --color-black: #08162a;\n"
        + "      --color-red: #f24b4b;\n"
        + "      --color-blue: #215faf;\n"
        + "    }\n"
        + "\n"
        + "    .item1 {\n"
        + "      grid-area: main;\n"
        + "    }\n"
        + "\n"
        + "    .item2 {\n"
        + "      grid-area: custom;\n"
        + "      background-color: var(--color-black);\n"
        + "      cursor: ew-resize;\n"
        + "    }\n"
        + "\n"
        + "    .item3 {\n"
        + "      font-family: \"Montserrat\";\n"
        + "      grid-area: gap;\n"
        + "      background-color: var(--color-white);\n"
        + "      padding: 20px; /* Add padding for better spacing */\n"
        + "    }\n"
        + "\n"
        + "    .grid-container {\n"
        + "      display: grid;\n"
        + "      grid-template-areas: \"main custom gap\";\n"
        + "      grid-template-columns: 3fr 0.5% 1fr;\n"
        + "      width: 100%;\n"
        + "      height: 100vh;\n"
        + "    }\n"
        + "\n"
        + "    .save,\n"
        + "    .new-testcase {\n"
        + "      display: inline-block;\n"
        + "      background-color: var(--color-blue);\n"
        + "      color: var(--color-white);\n"
        + "      width: 150px; /* Adjust the percentage based on your design */\n"
        + "      border-radius: 40px;\n"
        + "      border: 0px;\n"
        + "      margin-bottom: 10px;\n"
        + "      height: 40px;\n"
        + "      font-weight: bold;\n"
        + "    }\n"
        + "\n"
        + "    #myTable {\n"
        + "      width: 100%;\n"
        + "      border-collapse: collapse;\n"
        + "      margin-top: 20px;\n"
        + "      overflow-x: auto; /* Enable horizontal scrolling */\n"
        + "      border-radius: 10px;\n"
        + "      font-size: 12;\n"
        + "    }\n"
        + "\n"
        + "    #myTable tr:first-child {\n"
        + "      font-weight: bold;\n"
        + "      background-color: var(--color-grey);\n"
        + "      font-size: 15;\n"
        + "    }\n"
        + "\n"
        + "    #myTable th,\n"
        + "    #myTable td {\n"
        + "      border: 2px solid var(--color-darker-grey);\n"
        + "      padding: 8px;\n"
        + "      text-align: left;\n"
        + "    }\n"
        + "\n"
        + "    /* Style the first column */\n"
        + "    #myTable td:nth-child(1) {\n"
        + "      width: 40px;\n"
        + "      text-align: center;\n"
        + "      font-weight: bold;\n"
        + "    }\n"
        + "\n"
        + "    #myTable td:nth-child(3) {\n"
        + "      width: 50px;\n"
        + "    }\n"
        + "\n"
        + "    #myTable td:nth-child(3) button {\n"
        + "      background-color: transparent; /* Set background color to transparent */\n"
        + "      background-repeat: no-repeat;\n"
        + "      background-position: center;\n"
        + "      background-size: contain;\n"
        + "      border: none;\n"
        + "      cursor: pointer;\n"
        + "      padding: 5px;\n"
        + "      width: 20px;\n"
        + "      height: 20px;\n"
        + "      background-position: center;\n"
        + "    }\n"
        + "\n"
        + "    .collapse-button,\n"
        + "    .expand-button {\n"
        + "      display: inline-block;\n"
        + "      margin: auto;\n"
        + "      background-position: right;\n"
        + "      border: 0px;\n"
        + "      background-color: var(--color-blue);\n"
        + "      color: var(--color-white);\n"
        + "      text-align: center;\n"
        + "      border-radius: 40px;\n"
        + "    }\n"
        + "\n"
        + "    #myTable td:nth-child(3) button.delete_row {\n"
        + "      background-image: url(\"/png/delete.png\");\n"
        + "      /* Add additional styles as needed */\n"
        + "    }\n"
        + "\n"
        + "    /* Style for the edit button */\n"
        + "    #myTable td:nth-child(3) button.edit_row {\n"
        + "      background-image: url(\"/png/pen.png\");\n"
        + "      /* Add additional styles as needed */\n"
        + "    }\n"
        + "\n"
        + "    #myTable td:nth-child(3) button.save_row {\n"
        + "      background-image: url(\"/png/save.png\");\n"
        + "      /* Add additional styles as needed */\n"
        + "    }\n"
        + "\n"
        + "    @media screen and (max-width: 600px) {\n"
        + "      .save,\n"
        + "      .new-testcase {\n"
        + "        width: 100%; /* Take up the full width on smaller screens */\n"
        + "      }\n"
        + "    }\n"
        + "  </style>\n"
        + "</head>\n"
        + "<body>\n"
        + "<div class=\"grid-container\">\n"
        + "  <div class=\"item1\">";
    Element body = domTree.body();
    for (Element child : body.children()) {
      htmlContent += "\n";
      htmlContent += "      ";
      htmlContent += String.valueOf(child);
    }
    htmlContent += "\n";
    htmlContent += "</div>\n"
        + "  <div class=\"item3\">\n"
        + "    <button id=\"new-testcase\" class=\"new-testcase\" type=\"button\">\n"
        + "      New Testcase\n"
        + "    </button>\n"
        + "    <button id=\"save\" class=\"save\" type=\"button\">Save</button>\n"
        + "    <table id=\"myTable\"></table>\n"
        + "  </div>\n"
        + "  <div class=\"item2\" id=\"resizeBar\"></div>\n"
        + "</div>" +
            "    <script>\n" +
            "let inputValArr = [];\n"
        + "  let inputValues = [];\n"
        + "  let allElement = document.getElementsByTagName(\"*\");\n"
        + "  let inputElement = [];\n"
        + "  var isEditing = 0;\n"
        + "  for (let i = 0; i < allElement.length; i++) {\n"
        + "    if (\n"
        + "        allElement[i].hasAttribute(\"cong\") &&\n"
        + "        allElement[i].getAttribute(\"cong\").length != 0\n"
        + "    ) {\n"
        + "      if (allElement[i].tagName.localeCompare(\"TEXTAREA\")) {\n"
        + "        inputElement.push(allElement[i]);\n"
        + "      } else if (allElement[i].tagName.localeCompare(\"INPUT\")) {\n"
        + "        if (allElement[i].hasAttribute(\"type\")) {\n"
        + "          let typeAttributeValue = allElement[i].getAttribute(\"type\");\n"
        + "          if (\n"
        + "              typeAttributeValue.localeCompare(\"submit\") != 0 &&\n"
        + "              typeAttributeValue.localeCompare(\"button\") != 0 &&\n"
        + "              typeAttributeValue.localeCompare(\"reset\") != 0 &&\n"
        + "              typeAttributeValue.localeCompare(\"image\") != 0 &&\n"
        + "              typeAttributeValue.localeCompare(\"hidden\") != 0\n"
        + "          ) {\n"
        + "            inputElement.push(allElement[i]);\n"
        + "          }\n"
        + "        } else {\n"
        + "          inputElement.push(allElement[i]);\n"
        + "        }\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "  let numberInputElements = inputElement.length;\n"
        + "  function attributeElement(e) {\n"
        + "    return e.getAttribute(\"cong\");\n"
        + "  }\n"
        + "  for (let i = 0; i < numberInputElements; i++) {\n"
        + "    inputValues.push(attributeElement(inputElement[i]));\n"
        + "  }\n"
        + "\n"
        + "  const saveButton = document.getElementById(\"save\");" +
            "        saveButton.addEventListener(\"click\", function () {\n" +
            "          var table = document.getElementById(\"myTable\");\n" +
            "          for (var i = 1; i < table.rows.length; i++) {\n" +
            "            var row = table.rows[i];\n" +
            "            let values = row.cells[1].innerHTML.split(',');\n" +
            "            for (var j = 0; j < values.length; j++) {\n" +
            "              if (values[j].trim() == \"\") {\n" +
            "                inputValues.push(\"null\");\n" +
            "              } else {\n" +
            "                inputValues.push(values[j].trim());\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "          console.log(inputValues);\n" +
            "          let fileContent = \"\";\n" +
            "          for (let i = 0; i < numberInputElements; i++) {\n" +
            "            for (let k = i; k < inputValues.length; k += numberInputElements) {\n" +
            "              if (\n" +
            "                k <\n" +
            "                i +\n" +
            "                  (inputValues.length / numberInputElements - 1) *\n" +
            "                    numberInputElements\n" +
            "              ) {\n" +
            "                fileContent += inputValues[k] + \",\";\n" +
            "              } else {\n" +
            "                fileContent += inputValues[k] + \"\\n\";\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "          console.log(fileContent);"+
            "         let xhr = new XMLHttpRequest();\n" +
            "         xhr.open(\"POST\", \"/createtest\");\n" +
            "         // xhr.setRequestHeader(\"Accept\", \"application/json\");\n" +
            "         xhr.setRequestHeader(\"Content-Type\", \"application/json\");\n" +
            "         let data = {\n" +
            "          \"values\" : fileContent,\n" +
            "         }\n" +
            "         xhr.onload = () => {\n" +
            "          if (xhr.readyState === 4 && xhr.status === 200) {\n" +
            "           window.location.href = '/script';\n" +
            "          } else {\n" +
            "           console.log(`Error: ${xhr.status}`);\n" +
            "          }\n" +
            "         };\n" +
            "\n" +
            "         xhr.send(JSON.stringify(data));\n" +
            "        });\n"+
            "const newTestcaseButton = document.getElementById(\"new-testcase\");\n"
        + "  newTestcaseButton.addEventListener(\"click\", function () {\n"
        + "    // for (let i = 0; i < numberInputElements; i++) {\n"
        + "    //   if (inputElement[i].value.length == 0) {\n"
        + "    //     inputValues.push(\"null\");\n"
        + "    //   } else {\n"
        + "    //     inputValues.push(inputElement[i].value);\n"
        + "    //   }\n"
        + "\n"
        + "    // }\n"
        + "    var table = document.getElementById(\"myTable\");\n"
        + "    var row = table.insertRow();\n"
        + "    var val = \"\";\n"
        + "    var valArr = [];\n"
        + "    if (table.rows.length == 1) {\n"
        + "      var headerRow = table.insertRow(0);\n"
        + "      headerRow.classList.add(\"table-header\");\n"
        + "      var thead = table.createTHead();\n"
        + "      var index_cell = headerRow.insertCell();\n"
        + "      index_cell.innerHTML = \"INDEX\";\n"
        + "      var index_cell = headerRow.insertCell();\n"
        + "      index_cell.innerHTML = \"VALUE\";\n"
        + "      var index_cell = headerRow.insertCell();\n"
        + "      index_cell.innerHTML = \"ACTION\";\n"
        + "    }\n"
        + "    for (let i = 0; i < numberInputElements; i++) {\n"
        + "      valArr.push(inputElement[i].value);\n"
        + "      if (i != 0) {\n"
        + "        val += \", \" + inputElement[i].value;\n"
        + "      } else {\n"
        + "        val += inputElement[i].value;\n"
        + "      }\n"
        + "    }\n"
        + "    inputValArr.push(valArr);\n"
        + "    var index_cell = row.insertCell(0);\n"
        + "    index_cell.innerHTML = table.rows.length - 1;\n"
        + "\n"
        + "    var val_cell = row.insertCell(1);\n"
        + "\n"
        + "    if (val.length > 20) {\n"
        + "      // Display only a part of the text if it's too long\n"
        + "      val_cell.innerHTML =\n"
        + "          val.substring(0, 20) + \"<span class='ellipsis'> ...</span>\";\n"
        + "      // Add a button to expand and show the full value\n"
        + "      val_cell.innerHTML += `<button class='expand-button' onclick='expandText(this, &#39${val}&#39)'>Expand</button>`;\n"
        + "    } else {\n"
        + "      val_cell.innerHTML = val;\n"
        + "    }\n"
        + "\n"
        + "    var cell_action = row.insertCell(2);\n"
        + "    cell_action.innerHTML =\n"
        + "        \"<button onclick='deleteRow(this)' class='delete_row'></button> <button onclick='editRow(this)' class='edit_row'></button>\";\n"
        + "\n"
        + "    // Function to expand and show the full value\n"
        + "\n"
        + "    for (let i = 0; i < numberInputElements; i++) {\n"
        + "      inputElement[i].value = null;\n"
        + "    }\n"
        + "  });\n"
        + "\n"
        + "  function expandText(button, fullText) {\n"
        + "    var cell = button.parentNode;\n"
        + "    cell.innerHTML =\n"
        + "        fullText +\n"
        + "        \"<button class='collapse-button' onclick='collapseText(this)'>Collapse</button>\";\n"
        + "  }\n"
        + "\n"
        + "  // Function to collapse the text\n"
        + "  function collapseText(span) {\n"
        + "    var cell = span.parentNode;\n"
        + "    var val = cell.innerText;\n"
        + "    var string = val.substring(0,val.length - 8);\n"
        + "    cell.innerHTML =\n"
        + "        val.substring(0, 20) +\n"
        + "        \"<span class='ellipsis'> ...</span>\" +\n"
        + "        \"<button class='expand-button' onclick='expandText(this, &#39\" + string.replace(/\\n/g, ' ') + \"&#39)'>Expand</button>\";\n"
        + "  }\n"
        + "\n"
        + "  function deleteRow(row) {\n"
        + "    var i = row.parentNode.parentNode.rowIndex;\n"
        + "    document.getElementById(\"myTable\").deleteRow(i);\n"
        + "    inputValArr.splice(i - 1, 1);\n"
        + "    console.log(inputValArr);\n"
        + "  }\n"
        + "\n"
        + "  function editRow(button) {\n"
        + "    if (isEditing == 0) {\n"
        + "      var resizeBar = document.getElementById(\"resizeBar\");\n"
        + "      resizeBar.style.backgroundColor = \"var(--color-blue)\";\n"
        + "      isEditing = 1;\n"
        + "      var i = button.parentNode.parentNode.rowIndex;\n"
        + "      var table = document.getElementById(\"myTable\");\n"
        + "      for (var index in inputElement) {\n"
        + "        inputElement[index].value = inputValArr[i - 1][index];\n"
        + "      }\n"
        + "      button.setAttribute(\"onclick\", \"saveRow(this)\");\n"
        + "      button.setAttribute(\"class\", \"save_row\");\n"
        + "    }\n"
        + "  }\n"
        + "\n"
        + "  function saveRow(button) {\n"
        + "    if (isEditing == 1) {\n"
        + "      isEditing = 0;\n"
        + "      var resizeBar = document.getElementById(\"resizeBar\");\n"
        + "      resizeBar.style.backgroundColor = \"var(--color-black)\";\n"
        + "      var i = button.parentNode.parentNode.rowIndex;\n"
        + "      var table = document.getElementById(\"myTable\");\n"
        + "      var val = \"\";\n"
        + "      for (var index in inputElement) {\n"
        + "        if (index == 0) {\n"
        + "          val += inputElement[index].value;\n"
        + "        } else {\n"
        + "          val += \", \" + inputElement[index].value;\n"
        + "        }\n"
        + "        inputValArr[i - 1][index] = inputElement[index].value;\n"
        + "      }\n"
        + "      var editableRow = table.rows.item(i);\n"
        + "\n"
        + "      if (val.length > 20) {\n"
        + "        // Display only a part of the text if it's too long\n"
        + "        editableRow.cells[1].innerHTML =\n"
        + "            val.substring(0, 20) + \"<span class='ellipsis'> ...</span>\";\n"
        + "        // Add a button to expand and show the full value\n"
        + "        editableRow.cells[1].innerHTML += `<button class='expand-button' onclick='expandText(this, &#39${val}&#39)'>Expand</button>`;\n"
        + "      } else {\n"
        + "        editableRow.cells[1].innerHTML = val;\n"
        + "      }\n"
        + "      button.setAttribute(\"onclick\", \"editRow(this)\");\n"
        + "      button.setAttribute(\"class\", \"edit_row\");\n"
        + "\n"
        + "      for (let i = 0; i < numberInputElements; i++) {\n"
        + "        inputElement[i].value = null;\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "\n"
        + "  const resizeBar = document.getElementById(\"resizeBar\");\n"
        + "  let isResizing = false;\n"
        + "\n"
        + "  resizeBar.addEventListener(\"mousedown\", (event) => {\n"
        + "    isResizing = true;\n"
        + "    document.addEventListener(\"mousemove\", handleMouseMove);\n"
        + "    document.addEventListener(\"mouseup\", () => {\n"
        + "      isResizing = false;\n"
        + "      document.removeEventListener(\"mousemove\", handleMouseMove);\n"
        + "    });\n"
        + "  });\n"
        + "\n"
        + "  function handleMouseMove(event) {\n"
        + "    if (isResizing) {\n"
        + "      const container = document.querySelector(\".grid-container\");\n"
        + "      const newWidth = event.clientX;\n"
        + "      container.style.gridTemplateColumns = `${newWidth}px 0.5% 1fr`;\n"
        + "    }\n"
        + "  }\n"
        + "</script>\n"
        + "</body>\n"
        + "</html>";
    return htmlContent;
  }

  public static void addNewElementToDom(Document domTree) {
    Element newTestCaseBtn = domTree.body().appendElement("button");
    newTestCaseBtn.attr("id", "new-testcase");
    newTestCaseBtn.attr("type", "button");
    newTestCaseBtn = newTestCaseBtn.text("New Testcase");
    Element saveBtn = domTree.body().appendElement("button");
    saveBtn.attr("id", "save");
    saveBtn.attr("type", "button");
    saveBtn = saveBtn.text("Save");
    Element script = domTree.body().appendElement("script");
    script.text("       let inputValArr = [];\n" +
            "        let inputValues = [];\n" +
            "        let allElement = document.getElementsByTagName(\"*\");\n" +
            "        let inputElement = [];\n" +
            "        for (let i = 0; i < allElement.length; i++) {\n" +
            "          if (\n" +
            "            allElement[i].hasAttribute(\"cong\") &&\n" +
            "            allElement[i].getAttribute(\"cong\").length != 0\n" +
            "          ) {\n" +
            "            if (allElement[i].tagName.localeCompare(\"TEXTAREA\")) {\n" +
            "              inputElement.push(allElement[i]);\n" +
            "            } else if (allElement[i].tagName.localeCompare(\"INPUT\")) {\n" +
            "              if (allElement[i].hasAttribute(\"type\")) {\n" +
            "                let typeAttributeValue = allElement[i].getAttribute(\"type\");\n" +
            "                if (\n" +
            "                  typeAttributeValue.localeCompare(\"submit\") != 0 &&\n" +
            "                  typeAttributeValue.localeCompare(\"button\") != 0 &&\n" +
            "                  typeAttributeValue.localeCompare(\"reset\") != 0 &&\n" +
            "                  typeAttributeValue.localeCompare(\"image\") != 0 &&\n" +
            "                  typeAttributeValue.localeCompare(\"hidden\") != 0\n" +
            "                ) {\n" +
            "                  inputElement.push(allElement[i]);\n" +
            "                }\n" +
            "              } else {\n" +
            "                inputElement.push(allElement[i]);\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "        let numberInputElements = inputElement.length;\n" +
            "        function attributeElement(e) {\n" +
            "          return e.getAttribute(\"cong\");\n" +
            "        }\n" +
            "        for (let i = 0; i < numberInputElements; i++) {\n" +
            "          inputValues.push(attributeElement(inputElement[i]));\n" +
            "        }\n" +
            "\n" +
            "        const saveButton = document.getElementById(\"save\");\n" +
            "        saveButton.addEventListener(\"click\", function () {\n" +
            "          var table = document.getElementById(\"myTable\");\n" +
            "          for (var i = 1; i < table.rows.length; i++) {\n" +
            "            var row = table.rows[i];\n" +
            "            let values = row.cells[1].innerHTML.split(',');\n" +
            "            for (var j = 0; j < values.length; j++) {\n" +
            "              if (values[j].trim() == \"\") {\n" +
            "                inputValues.push(\"null\");\n" +
            "              } else {\n" +
            "                inputValues.push(values[j].trim());\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "          console.log(inputValues);\n" +
            "          let fileContent = \"\";\n" +
            "          for (let i = 0; i < numberInputElements; i++) {\n" +
            "            for (let k = i; k < inputValues.length; k += numberInputElements) {\n" +
            "              if (\n" +
            "                k <\n" +
            "                i +\n" +
            "                  (inputValues.length / numberInputElements - 1) *\n" +
            "                    numberInputElements\n" +
            "              ) {\n" +
            "                fileContent += inputValues[k] + \",\";\n" +
            "              } else {\n" +
            "                fileContent += inputValues[k] + \"\\n\";\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "          console.log(fileContent);"+
            "            \"         let xhr = new XMLHttpRequest();\\n\" +\n" +
            "            \"         xhr.open(\\\"POST\\\", \\\"/createtest\\\");\\n\" +\n" +
            "            \"         // xhr.setRequestHeader(\\\"Accept\\\", \\\"application/json\\\");\\n\" +\n" +
            "            \"         xhr.setRequestHeader(\\\"Content-Type\\\", \\\"application/json\\\");\\n\" +\n" +
            "            \"         let data = {\\n\" +\n" +
            "            \"          \\\"values\\\" : fileContent,\\n\" +\n" +
            "            \"         }\\n\" +\n" +
            "            \"         xhr.onload = () => {\\n\" +\n" +
            "            \"          if (xhr.readyState === 4 && xhr.status === 200) {\\n\" +\n" +
            "            \"           window.location.href = '/script';\\n\" +\n" +
            "            \"          } else {\\n\" +\n" +
            "            \"           console.log(`Error: ${xhr.status}`);\\n\" +\n" +
            "            \"          }\\n\" +\n" +
            "            \"         };\\n\" +\n" +
            "            \"\\n\" +\n" +
            "            \"         xhr.send(JSON.stringify(data));\\n\" +\n" +
            "            \"        });\\n\"+"
            + "        const newTestcaseButton = document.getElementById('new-testcase');\n"
            + "        newTestcaseButton.addEventListener('click', function () {\n"
            + "            for (let i = 0; i < numberInputElements; i++) {\n"
            + "                if (inputElement[i].value.length == 0) {\n"
            + "                    inputValues.push(\"null\");\n"
            + "                } else {\n"
            + "                    inputValues.push(inputElement[i].value);\n"
            + "                }\n"
            + "\n"
            + "            }\n"
            + "            const para = document.createElement(\"p\");\n"
            + "            let tmp = \"\";\n"
            + "            for (let i = 0; i < numberInputElements; i++) {\n"
            + "                tmp += inputElement[i].value + \" \";\n"
            + "            }\n"
            + "            para.innerText = \"You entered \" + tmp;\n"
            + "            document.body.appendChild(para);\n"
            + "            for (let i = 0; i < numberInputElements; i++) {\n"
            + "                inputElement[i].value = '';\n"
            + "            }\n"
            + "        });");
  }

  public static void writeDomToHtmlFile(Document domTree, String pathToHtmlFile)
          throws IOException {
    File myFile = new File(pathToHtmlFile);
    if (myFile.createNewFile()) {
      System.out.println("File created!");
    }
    FileWriter mockWebHtml = new FileWriter(myFile);
    String htmlContent = contentOfMockWeb(domTree);
    mockWebHtml.write(htmlContent);
    mockWebHtml.close();
  }
  public static Map<String, Vector<String>> getDataFromCSV(String pathDownloadCSV) throws FileNotFoundException {
    File downloadCSV = new File(pathDownloadCSV);
    Scanner readDownloadCSV = new Scanner(downloadCSV);
    while (readDownloadCSV.hasNextLine()) {
      String line = readDownloadCSV.nextLine();
      String[] splitDataLine = line.split(",");
      Vector<String> storeData = new Vector<>();
      for (int i = 0; i < splitDataLine.length; i++) {
        if (i != 0) {
          storeData.add(splitDataLine[i]);
        }
      }
      mapValueVariableAnData.put(splitDataLine[0], storeData);
    }
    //System.out.println(mapValueVariableAnData);
    for (Entry<String, Vector<String>> entry : mapValueVariableAnData.entrySet()) {
      String valueVariable = entry.getKey();
      System.out.print(entry.getValue().size() + " ");
      System.out.print(valueVariable + " ");
      for (int i = 0; i < entry.getValue().size(); i++) {
        int j = i + 1;
        System.out.print(j + entry.getValue().elementAt(i) + " ");
      }
      System.out.print("\n");
    }
    return mapValueVariableAnData;
  }

  public static void fillInCSV(String pathCSV, String pathTempCSV, Map<String, Vector<String>> mapValueVariableAnData) throws IOException {
    File CSV = new File(pathCSV);
    File tempCSV = new File(pathTempCSV);
    Scanner readCSV = new Scanner(CSV);
    FileWriter writeToTempCSV = new FileWriter(pathTempCSV);
    while (readCSV.hasNextLine()) {
      String line = readCSV.nextLine();
      String[] splitDataLine =  line.split(",");
      if (splitDataLine.length == 1) {
        String[] valueVariables = splitDataLine[0].split(" & ");
        int numberTest = mapValueVariableAnData.get(valueVariables[0]).size();
        int numberValueVariables = valueVariables.length;
        writeToTempCSV.write(line);
        for (int i = 0; i < numberTest; i++) {
          int countNullValueVariables = 0;
          String data = "";
          for (int j = 0; j < numberValueVariables; j++) {
//            if (j < valueVariables.length - 1) {
//              data += mapValueVariableAnData.get(valueVariables[j]).get(i) + " & ";
//            } else {
//              writeToTempCSV.write(mapValueVariableAnData.get(valueVariables[j]).get(i));
//              if (i < numberTest - 1) {
//                writeToTempCSV.write(",");
//              } else {
//                writeToTempCSV.write("\n");
//              }
//            }
            if (j < numberValueVariables - 1) {
              data += mapValueVariableAnData.get(valueVariables[j]).get(i) + " & ";
              if (mapValueVariableAnData.get(valueVariables[j]).get(i).equals("null")) {
                countNullValueVariables++;
              }
            } else {
              data +=  mapValueVariableAnData.get(valueVariables[j]).get(i);
              if (mapValueVariableAnData.get(valueVariables[j]).get(i).equals("null")) {
                countNullValueVariables++;
              }
              if (countNullValueVariables == numberValueVariables || countNullValueVariables == 0) {
                writeToTempCSV.write("," + data);
              }
              if (i == numberTest - 1) {
                writeToTempCSV.write("\n");
              }
            }

          }
        }
      } else {
        writeToTempCSV.write(line + "\n");
      }
    }
    readCSV.close();
    writeToTempCSV.close();
//    CSV.delete();
//    File newCSV = new File(pathCSV);
//    tempCSV.renameTo(newCSV);
  }

  public static void main(String[] args) {
    String htmlContent = getHtmlContent("https://demoqa.com/automation-practice-form");
    System.out.println(htmlContent);
  }
}