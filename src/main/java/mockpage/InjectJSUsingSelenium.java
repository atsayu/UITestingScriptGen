package mockpage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InjectJSUsingSelenium {
//    public static void main(String[] args) throws InterruptedException {
//    System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64-119\\chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.navigate().to("https://www.saucedemo.com/");
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//        String inject_script = "// Create a container for the split-screen view\n" +
//                "        var splitScreenContainer = document.createElement('div');\n" +
//                "        splitScreenContainer.style.cssText = 'position: fixed; top: 0; left: 0; width: 75%; height: 100%; overflow: auto; z-index: 9999;';\n" +
//                "\n" +
//                "        // Create the left-hand side iframe to display the original webpage\n" +
//                "        var iframe = document.createElement('iframe');\n" +
//                "        iframe.id = \"myiframe\";\n" +
//                "        iframe.src = window.location.href;\n" +
//                "        iframe.style.cssText = 'width: 100%; height: 100%; border: none;';\n" +
//                "        splitScreenContainer.appendChild(iframe);\n" +
//                "\n" +
//                "        // Create the right-hand side container for your custom code\n" +
//                "        var customCodeContainer = document.createElement('div');\n" +
//                "        customCodeContainer.style.cssText = 'position: fixed; top: 0; right: 0; width: 25%; height: 100%; background-color: #f0f0f0; padding: 20px; overflow: auto; z-index: 10000;';\n" +
//                "\n" +
//                "        // Create buttons in the custom code container\n" +
//                "        var checkAdding = document.createElement('p');\n" +
//                "        checkAdding.id = \"checkAdding\";\n" +
//                "        checkAdding.textContent = 'Enter values, then click the Add button';\n" +
//                "\n" +
//                "        // reate buttons in the custom code container\n" +
//                "        var addButton = document.createElement('button');\n" +
//                "        addButton.innerHTML = \"Add\";\n" +
//                "        addButton.id = \"addButton\";\n" +
//                "        \n" +
//                "\n" +
//                "        var saveButton = document.createElement('button');\n" +
//                "        saveButton.textContent = 'Save';\n" +
//                "        saveButton.id = 'Save';\n" +
//                "    \n" +
//                "\n" +
//                "var table = document.createElement('table');\n" +
//                "table.id = 'myTable';\n" +
//                "        customCodeContainer.appendChild(checkAdding);\n" +
//                "        customCodeContainer.appendChild(addButton);\n" +
//                "        customCodeContainer.appendChild(saveButton);\n" +
//                "customCodeContainer.appendChild(table);" +
//                "\n" +
//                "\n" +
//                "        // Append the custom code container to the split-screen container\n" +
//                "        splitScreenContainer.appendChild(customCodeContainer);\n" +
//                "\n" +
//                "        // Append the split-screen container to the body\n" +
//                "        document.body.appendChild(splitScreenContainer);";
//
//        js.executeScript(inject_script);
//        driver.switchTo().frame(driver.findElement(By.id("myiframe")));
//        Map<String, String> mp = new HashMap<>();
//        mp.put("//*[@id=\"user-name\"]", "valid_user");
//        mp.put("//*[@id=\"password\"]", "valid_pass");
//
//        for (Map.Entry<String, String> entry : mp.entrySet()) {
//            String xpath = entry.getKey();
//            String value_variable = entry.getValue();
//            WebElement e = driver.findElement(By.xpath(xpath));
//            js.executeScript("arguments[0].setAttribute('cong', arguments[1]);", e, value_variable);
//
//
//        }
//        driver.switchTo().defaultContent();
//        String sc = " let addBtn = document.getElementById(\"addButton\");\n" +
//                "        let saveBtn = document.getElementById(\"Save\");\n" +
//                "        let values = [];\n" +
//                "        let inputElement = [];\n" +
//                "let iframe = document.getElementById(\"myiframe\");\n" +
//                "\n" +
//                "        let allElement = iframe.contentWindow.document.getElementsByTagName(\"*\");" +
//                "        for (let i = 0; i < allElement.length; i++) {\n" +
//                "            if (allElement[i].hasAttribute(\"cong\")) {\n" +
//                "              inputElement.push(allElement[i]);\n" +
//                "            }\n" +
//                "        }\n" +
//                "console.log(inputElement.length);" +
//                "        addBtn.addEventListener('click', function() {\n" +
//                "            for (let i = 0; i < inputElement.length; i++) {\n" +
//                "                values.push(inputElement[i].value);\n" +
//                "console.log(inputElement[i].value);" +
//                "            }\n" +
//                "            for (let i = 0; i < inputElement.length; i++) {\n" +
//                "                inputElement[i].value = null;\n" +
//                "            }\n" +
//                "        });\n" +
//                "        saveBtn.addEventListener('click', function() {\n" +
//                "            let fileContent = '';\n" +
//                "            for (let i = 0; i < values.length; i++) {\n" +
//                "                fileContent += values[i];\n" +
//                "            }\n" +
//                "            let blob = new Blob([fileContent], { type: 'text/csv' });\n" +
//                "            let url = URL.createObjectURL(blob);\n" +
//                "            let downloadLink = document.createElement('a');\n" +
//                "            downloadLink.href = url;\n" +
//                "            downloadLink.download = 'data.csv';\n" +
//                "            downloadLink.click();\n" +
//                "        });\n" +
//                "\n";
//        js.executeScript(sc);
//
//    }
    public static void main(String[] args) {
        Input ip = new Input();
        try {
            ip.changeDomAndCreateMockPageVersion3("E:\\LAB UI\\TestWebDemo\\SpringbootUITestingForm\\src\\main\\resources\\template\\outline.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
