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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
      String type = expressionActionElements.get(i).getElementsByTagName("type").item(0).getTextContent();
      if (type.equals("and") || type.equals("or")) {
        continue;
      }
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

  public void changeDomAndCreateMockPageVersion3(String outline)
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
      String type = expressionActionElements.get(i).getElementsByTagName("type").item(0).getTextContent();
      if (type.equals("and") || type.equals("or")) {
        continue;
      }
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
    WebDriver driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.navigate().to(linkHtml);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    String inject_script = "// Create a container for the split-screen view\n" +
            "        var splitScreenContainer = document.createElement('div');\n" +
            "        splitScreenContainer.style.cssText = 'position: fixed; top: 0; left: 0; width: 75%; height: 100%; overflow: auto; z-index: 9999;';\n" +
            "\n" +
            "        // Create the left-hand side iframe to display the original webpage\n" +
            "        var iframe = document.createElement('iframe');\n" +
            "        iframe.id = \"myiframe\";\n" +
            "        iframe.src = window.location.href;\n" +
            "        iframe.style.cssText = 'width: 100%; height: 100%; border: none;';\n" +
            "        splitScreenContainer.appendChild(iframe);\n" +
            "\n" +
            "        // Create the right-hand side container for your custom code\n" +
            "        var customCodeContainer = document.createElement('div');\n" +
            "        customCodeContainer.style.cssText = 'position: fixed; top: 0; right: 0; width: 25%; height: 100%; background-color: #f0f0f0; padding: 20px; overflow: auto; z-index: 10000;';\n" +
            "\n" +
            "        // reate buttons in the custom code container\n" +
            "        var addButton = document.createElement('button');\n" +
            "        addButton.innerHTML = \"Add\";\n" +
            "        addButton.id = \"addButton\";\n" +
            "        \n" +
            "\n" +
            "        var saveButton = document.createElement('button');\n" +
            "        saveButton.textContent = 'Save';\n" +
            "        saveButton.id = 'save';\n" +
            "    \n" +
            "\n" +
            "var table = document.createElement('table');\n" +
            "table.id = 'myTable';\n" +
            "        customCodeContainer.appendChild(addButton);\n" +
            "        customCodeContainer.appendChild(saveButton);\n" +
            "customCodeContainer.appendChild(table);" +
            "\n" +
            "\n" +
            "        // Append the custom code container to the split-screen container\n" +
            "        splitScreenContainer.appendChild(customCodeContainer);\n" +
            "\n" +
            "        // Append the split-screen container to the body\n" +
            "        document.body.appendChild(splitScreenContainer);";

    js.executeScript(inject_script);
    driver.switchTo().frame(driver.findElement(By.id("myiframe")));
    for (String locator : input) {
      Element e = mapStoreLocatorAndElement.get(normalize(locator));
      String valueVariable = mapLocatorVariableAndValueVariable.get(locator);
      String xpath = getXpath(e);
      WebElement element = driver.findElement(By.xpath(xpath));
      js.executeScript("arguments[0].setAttribute('cong', arguments[1]);", element, valueVariable);
    }
    driver.switchTo().defaultContent();

    js.executeScript("var script_action = document.createElement('input');\n" +
            "        script_action.id = \"script_action\";\n" +
            "        script_action.hidden = \"true\";\n" +
            "        script_action.setAttribute(\"value\", arguments[0]);\n" +
            "        var all_value_variable = document.createElement('input');\n" +
            "        all_value_variable.id = \"all_value_variable\";\n" +
            "        all_value_variable.hidden = \"true\";\n" +
            "        all_value_variable.setAttribute(\"value\", arguments[1]);\n" +
            "        var value_variable_not_assert = document.createElement('input');\n" +
            "        value_variable_not_assert.id = \"value_variable_not_assert\";\n" +
            "        value_variable_not_assert.hidden = \"true\";\n" +
            "        value_variable_not_assert.setAttribute(\"value\", arguments[2]);\n" +
            "        document.body.appendChild(script_action);\n" +
            "        document.body.appendChild(all_value_variable);\n" +
            "        document.body.appendChild(value_variable_not_assert);", contentActions, contentAllValueVariable, contentValueVariableNotAssert);
//
//    String sc = " let addBtn = document.getElementById(\"addButton\");\n" +
//            "        let saveBtn = document.getElementById(\"save\");\n" +
//            "        let values = [];\n" +
//            "        let inputElement = [];\n" +
//            "let iframe = document.getElementById(\"myiframe\");\n" +
//            "\n" +
//            "        let allElement = iframe.contentWindow.document.getElementsByTagName(\"*\");" +
//            "        for (let i = 0; i < allElement.length; i++) {\n" +
//            "            if (allElement[i].hasAttribute(\"cong\")) {\n" +
//            "              inputElement.push(allElement[i]);\n" +
//            "            }\n" +
//            "        }\n" +
//            "console.log(inputElement.length);" +
//            "        addBtn.addEventListener('click', function() {\n" +
//            "            for (let i = 0; i < inputElement.length; i++) {\n" +
//            "                values.push(inputElement[i].value);\n" +
//            "console.log(inputElement[i].value);" +
//            "            }\n" +
//            "            for (let i = 0; i < inputElement.length; i++) {\n" +
//            "                inputElement[i].value = null;\n" +
//            "            }\n" +
//            "        });\n" +
//            "        saveBtn.addEventListener('click', function() {\n" +
//            "            let fileContent = '';\n" +
//            "            for (let i = 0; i < values.length; i++) {\n" +
//            "                fileContent += values[i];\n" +
//            "            }\n" +
//            "            let blob = new Blob([fileContent], { type: 'text/csv' });\n" +
//            "            let url = URL.createObjectURL(blob);\n" +
//            "            let downloadLink = document.createElement('a');\n" +
//            "            downloadLink.href = url;\n" +
//            "            downloadLink.download = 'data.csv';\n" +
//            "            downloadLink.click();\n" +
//            "        });\n" +
//            "\n";
//    js.executeScript(sc);
      String script_function = " const map = new Map();   //map luu key la value_variable_not_assert va phan tu tuong ung\n" +
              "        let all_value_variable = document.getElementById(\"all_value_variable\").getAttribute(\"value\").split(\",\");\n" +
              "        let value_variable_not_assert = document.getElementById(\"value_variable_not_assert\").getAttribute(\"value\").split(\",\");\n" +
              "        let inputValArr = [];\n" +
              "        let inputElement = [];\n" +
              "        let iframe = document.getElementById(\"myiframe\");\n" +
              "        let allElementInFrame = iframe.contentWindow.document.getElementsByTagName(\"*\");\n" +
              "        var script_action = document.getElementById(\"script_action\");\n" +
              "        var content_script_action = script_action.getAttribute(\"value\");\n" +
              "\n" +
              "        for (let j = 0; j < value_variable_not_assert.length; j++) {\n" +
              "            console.log(value_variable_not_assert[j]);\n" +
              "            for (let i = 0; i < allElementInFrame.length; i++) {\n" +
              "                if (allElementInFrame[i].hasAttribute(\"cong\") && allElementInFrame[i].getAttribute(\"cong\").localeCompare(value_variable_not_assert[j]) == 0) {\n" +
              "                    map.set(value_variable_not_assert[j], allElementInFrame[i]);\n" +
              "                    inputElement.push(allElementInFrame[i]);\n" +
              "                }\n" +
              "            }\n" +
              "        }\n" +
              "\n" +
              "\n" +
              "        let numberInputElements = inputElement.length;\n" +
              "        const saveButton = document.getElementById(\"save\");\n" +
              "        saveButton.addEventListener(\"click\", function () {\n" +
              "            let allInputValues = [];\n" +
              "            let assertArr = [];\n" +
              "            const table = document.getElementById(\"myTable\");\n" +
              "            const rows = table.querySelectorAll(\"tbody tr:not(.table-header)\");\n" +
              "\n" +
              "            rows.forEach(row => {\n" +
              "                const valueCell = row.cells[1];\n" +
              "                const inputs = valueCell.querySelectorAll(\"input\");\n" +
              "                let assert = [];\n" +
              "                inputs.forEach(input => {\n" +
              "                    const inputValue = input.value;\n" +
              "                    assert.push(inputValue);\n" +
              "                });\n" +
              "                assertArr.push(assert);\n" +
              "            });\n" +
              "            for (let i = 0; i < inputValArr.length; i++) {\n" +
              "                let indexInputNotAssert = 0;\n" +
              "                let indexAssert = 0;\n" +
              "                let allInputValue = [];\n" +
              "                for (let j = 0; j < all_value_variable.length; j++) {\n" +
              "                    if (value_variable_not_assert.includes(all_value_variable[j])) {\n" +
              "                        allInputValue.push(inputValArr[i][indexInputNotAssert]);\n" +
              "                        indexInputNotAssert++;\n" +
              "                    } else {\n" +
              "                        allInputValue.push(assertArr[i][indexAssert]);\n" +
              "                        indexAssert++;\n" +
              "                    }\n" +
              "                }\n" +
              "                allInputValues.push(allInputValue);\n" +
              "            }\n" +
              "            console.log(allInputValues);\n" +
              "            let fileContent = \"\";\n" +
              "            for (let i = 0; i < all_value_variable.length; i++) {\n" +
              "                fileContent += all_value_variable[i];\n" +
              "                for (let j = 0; j < allInputValues.length; j++) {\n" +
              "                    fileContent += \",\" + allInputValues[j][i];\n" +
              "                }\n" +
              "                if (i != all_value_variable.length - 1) {\n" +
              "                    fileContent += \"\\n\";\n" +
              "                }\n" +
              "            }\n" +
              "            console.log(fileContent);\n" +
              "            // let xhr = new XMLHttpRequest();\n" +
              "            // xhr.open(\"POST\", \"/createtest\");\n" +
              "            // // xhr.setRequestHeader(\"Accept\", \"application/json\");\n" +
              "            // xhr.setRequestHeader(\"Content-Type\", \"application/json\");\n" +
              "            // let data = {\n" +
              "            //     \"values\": fileContent,\n" +
              "            // }\n" +
              "            // xhr.onload = () => {\n" +
              "            //     if (xhr.readyState === 4 && xhr.status === 200) {\n" +
              "            //         window.location.href = '/script';\n" +
              "            //     } else {\n" +
              "            //         console.log(`Error: ${xhr.status}`);\n" +
              "            //     }\n" +
              "            // };\n" +
              "\n" +
              "            // xhr.send(JSON.stringify(data));\n" +
              "        });\n" +
              "        const newTestcaseButton = document.getElementById(\"addButton\");\n" +
              "        newTestcaseButton.addEventListener(\"click\", function () {\n" +
              "            let val = content_script_action;\n" +
              "            var table = document.getElementById(\"myTable\");\n" +
              "            var row = table.insertRow();\n" +
              "            var valArr = [];\n" +
              "            if (table.rows.length == 1) {\n" +
              "                var headerRow = table.insertRow(0);\n" +
              "                headerRow.classList.add(\"table-header\");\n" +
              "                var thead = table.createTHead();\n" +
              "                var index_cell = headerRow.insertCell();\n" +
              "                index_cell.innerHTML = \"INDEX\";\n" +
              "                var index_cell = headerRow.insertCell();\n" +
              "                index_cell.innerHTML = \"VALUE\";\n" +
              "                var index_cell = headerRow.insertCell();\n" +
              "                index_cell.innerHTML = \"ACTION\";\n" +
              "            }\n" +
              "            for (let i = 0; i < value_variable_not_assert.length; i++) {\n" +
              "                let value = map.get(value_variable_not_assert[i]).value;\n" +
              "                if (value.length == 0) {\n" +
              "                    value = \"null\";\n" +
              "                }\n" +
              "                val = val.replace(value_variable_not_assert[i], '\"' + value + '\"');\n" +
              "            }\n" +
              "            for (let i = 0; i < numberInputElements; i++) {\n" +
              "                if (inputElement[i].value.length == 0) {\n" +
              "                    valArr.push(\"null\");\n" +
              "                } else {\n" +
              "                    valArr.push(inputElement[i].value);\n" +
              "                }\n" +
              "            }\n" +
              "            inputValArr.push(valArr);\n" +
              "            console.log(inputValArr);\n" +
              "            var index_cell = row.insertCell(0);\n" +
              "            index_cell.innerHTML = table.rows.length - 1;\n" +
              "\n" +
              "            var val_cell = row.insertCell(1);\n" +
              "            val_cell.innerHTML = val;\n" +
              "            var cell_action = row.insertCell(2);\n" +
              "            cell_action.innerHTML =\n" +
              "                \"<button onclick='deleteRow(this)' class='delete_row'></button> <button onclick='editRow(this)' class='edit_row'></button>\";\n" +
              "            for (let i = 0; i < numberInputElements; i++) {\n" +
              "                inputElement[i].value = null;\n" +
              "            }\n" +
              "\n" +
              "        });\n" +
              "\n" +
              "        function deleteRow(row) {\n" +
              "            var i = row.parentNode.parentNode.rowIndex;\n" +
              "            document.getElementById(\"myTable\").deleteRow(i);\n" +
              "            inputValArr.splice(i - 1, 1);\n" +
              "            console.log(inputValArr);\n" +
              "        }\n" +
              "\n" +
              "        function editRow(button) {\n" +
              "            if (isEditing == 0) {\n" +
              "                // var resizeBar = document.getElementById(\"resizeBar\");\n" +
              "                // resizeBar.style.backgroundColor = \"var(--color-blue)\";\n" +
              "                isEditing = 1;\n" +
              "                var i = button.parentNode.parentNode.rowIndex;\n" +
              "                var table = document.getElementById(\"myTable\");\n" +
              "                for (var index in inputElement) {\n" +
              "                    inputElement[index].value = inputValArr[i - 1][index];\n" +
              "                }\n" +
              "                let assertValues = \"\";\n" +
              "                const inputs = table.rows[i].cells[1].querySelectorAll(\"input\");\n" +
              "                for (let j = 0; j < inputs.length; j++) {\n" +
              "                    if (j != inputs.length - 1) {\n" +
              "                        assertValues += inputs[j].value + \",\";\n" +
              "                    } else {\n" +
              "                        assertValues += inputs[j].value;\n" +
              "                    }\n" +
              "                }\n" +
              "                button.setAttribute(\"onclick\", `saveRow(this, '${assertValues}')`);\n" +
              "                button.setAttribute(\"class\", \"save_row\");\n" +
              "            }\n" +
              "        }\n" +
              "\n" +
              "        function saveRow(button, assert) {\n" +
              "            if (isEditing == 1) {\n" +
              "                isEditing = 0;\n" +
              "                // var resizeBar = document.getElementById(\"resizeBar\");\n" +
              "                // resizeBar.style.backgroundColor = \"var(--color-black)\";\n" +
              "                var i = button.parentNode.parentNode.rowIndex;\n" +
              "                var table = document.getElementById(\"myTable\");\n" +
              "                let val = content_script_action;\n" +
              "                let assertValues = assert.split(\",\");\n" +
              "                console.log(assertValues);\n" +
              "                var editableRow = table.rows.item(i);\n" +
              "                for (let j = 0; j < value_variable_not_assert.length; j++) {\n" +
              "                    let value = map.get(value_variable_not_assert[j]).value;\n" +
              "                    if (value.length == 0) {\n" +
              "                        value = \"null\";\n" +
              "                    }\n" +
              "                    val = val.replace(value_variable_not_assert[j], '\"' + value + '\"');\n" +
              "                    inputValArr[i - 1][j] = value;\n" +
              "                }\n" +
              "                editableRow.cells[1].innerHTML = val;\n" +
              "                const inputs = editableRow.cells[1].querySelectorAll(\"input\");\n" +
              "                for (let j = 0; j < inputs.length; j++) {\n" +
              "                    inputs[j].value = assertValues[j];\n" +
              "                }\n" +
              "                button.setAttribute(\"onclick\", \"editRow(this)\");\n" +
              "                button.setAttribute(\"class\", \"edit_row\");\n" +
              "\n" +
              "                for (let i = 0; i < numberInputElements; i++) {\n" +
              "                    inputElement[i].value = null;\n" +
              "                }\n" +
              "                console.log(inputValArr);\n" +
              "            }\n" +
              "        }";
      js.executeScript(script_function);
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
    if (e == null) {
      return false;
    }
    if (e.tagName().equals("input")) {
      if (e.hasAttr("type") && (e.attr("type").equals("submit") || e.attr("type").equals("checkbox") || e.attr("type").equals("radio")) ||  e.attr("hidden").equals("true")) {
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
          ip.changeDomAndCreateMockPageVersion3(outline);
      } catch (IOException e) {
          throw new RuntimeException(e);
      } catch (ParserConfigurationException e) {
          throw new RuntimeException(e);
      } catch (SAXException e) {
          throw new RuntimeException(e);
      }
  }
}
