package mockpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Main {

  static Vector<Element> interactableElements = new Vector<>();
  static Set<String> variable_list = new HashSet<>();
  static HashSet<String> visited = new HashSet<>();
  static final double weightAttributeId = 0.6;
  static final double weightAttributeName = 0.2;
  static final double weightAnotherAttribute = 0.02;

  public static void main(String[] args) throws IOException {
    Main solve = new Main();
    String script_path = args[0];
    String linkHtml = args[1];
    String linkVariableFile = args[2];
    String htmlContent = solve.getHtmlContent(linkHtml);
    Document domTree = solve.getDomTree(htmlContent);
    Elements childRoot = domTree.children();
    for (Element child : childRoot) {
      searchInteractableElements(child);
    }
    System.out.println(interactableElements.size());
//    //get text
//    for (Element e : interactableElements) {
//      System.out.println(e.ownText());
//    }

//    for (Element e : interactableElements) {
//      getXpath(e);
//    }
    FileWriter variableFile = new FileWriter(linkVariableFile);
    variableFile.write("*** Variables ***" + "\n");
    searchLocator(script_path, variableFile);
    variableFile.close();
    for (Element e : interactableElements) {
      System.out.println(e);
    }
    for (String s : variable_list) {
      System.out.println(s);
    }
  }

  public String getHtmlContent(String linkHtml) {
//    System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    driver.get(linkHtml);
    String htmlContent = driver.getPageSource();
    driver.quit();
    return htmlContent;
  }

  public Document getDomTree(String htmlContent) {
    Document domTree = Jsoup.parse(htmlContent);
    return domTree;
  }


  public static void searchInteractableElements(Element e) {
    String[] tagNameInteractableElement = {"input", "button", "a", "select", "textarea"};
    for (String s : tagNameInteractableElement) {
      if (e.tagName().equals(s)) {
        interactableElements.add(e);
      }
    }
    for (Element child : e.children()) {
      searchInteractableElements(child);
    }
  }

  public static String getXpath(Element interactableElement) {
    int attributes_size = interactableElement.attributesSize();
    String xpath = "";
    if (attributes_size > 0) {
      Attributes attr = interactableElement.attributes();
      xpath += "//" + interactableElement.tagName() + "[";
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
      xpath += "]";
    }
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

  public static void searchLocator(String script_path, FileWriter variableFile)
      throws IOException {
    File variable = new File(script_path);
    Scanner myReader = new Scanner(variable);

    while (myReader.hasNextLine()) {
      String line = myReader.nextLine();
      System.out.println(line);
      String[] data = line.split(" ");
      for (String x : data) {
        if (x.startsWith("${") && x.endsWith("}") && !visited.contains(x)) {
          variable_list.add(x);
          Vector<String> x_value = word_analysis(x);
          int size = x_value.size();
          double max_weight = 0;
          if (interactableElements.size() == 0) {
            return;
          }
          Element elementHasMaxWeight = interactableElements.firstElement();
          for (Element e : interactableElements) {
            String beautiful_xpath = beautifulString(getXpath(e));
            boolean has_content_variable = true;
            for (String word : x_value) {
              if (!beautiful_xpath.contains(word)) {
                has_content_variable = false;
              }
            }
            if (!has_content_variable) {
              continue;
            }
            double weight = 0;
            Attributes attributes = e.attributes();
            for (Attribute attr : attributes) {
              if (attr.getKey().equals("id")) {
                String analysis_id = beautifulString(attr.getValue());
                for (String word : x_value) {
                  if (analysis_id.contains(word)) {
                    weight += weightAttributeId / size;
                  }
                }
              }
              else if (attr.getKey().equals("name")) {
                String analysis_name = beautifulString(attr.getValue());
                for (String word : x_value) {
                  if (analysis_name.contains(word)) {
                    weight += weightAttributeName / size;
                  }
                }
              }
              else {
                String analysis_another_attribute = beautifulString(attr.getValue());
                for (String word : x_value) {
                  if (analysis_another_attribute.contains(word)) {
                    weight += weightAnotherAttribute / size;
                  }
                }
              }
            }
            if (weight > max_weight) {
              max_weight = weight;
              elementHasMaxWeight = e;
            }
          }
          if (max_weight > 0) {
            variableFile.write(x + "     xpath=" + getXpath(elementHasMaxWeight) + "\n");
            interactableElements.remove(elementHasMaxWeight);
            variable_list.remove(x);
            visited.add(x);
          }
        }
      }
    }
    myReader.close();
  }
}