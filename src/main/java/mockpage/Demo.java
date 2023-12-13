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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

public class Demo {

  public static Vector<Element> element_interactable = new Vector<>();
  public static Vector<String> locator = new Vector<>();
  public static Vector<String> result = new Vector<>();

  public static void main(String[] args) throws IOException {
//    System.setProperty("Webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
    WebDriver driver = new ChromeDriver();
    //driver.get("https://demoqa.com/login");
    driver.get(args[0]);
    String htmlContent = driver.getPageSource();
    driver.quit();
    Document doc = Jsoup.parse(htmlContent);
    FileWriter result_file = new FileWriter("C:\\Users\\PC\\Desktop\\locator_variable.txt");

    Elements childElement = doc.children();
    for (Element child : childElement) {
      searchElements(child);
    }
    for (Element e : element_interactable) {
      getXpath(e);
    }

    try {
      //File variable = new File("C:\\Users\\PC\\Desktop\\test.robot");
      File variable = new File(args[1]);
      Scanner myReader = new Scanner(variable);
      HashSet<String> visited = new HashSet<>();
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine();
        String[] data = line.split(" ");
        for (String x : data) {
          if (x.startsWith("${") && x.endsWith("}") && !visited.contains(x)) {
            SearchLocator(x, result_file);
            visited.add(x);
          }
        }

      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    for (String x : result) {
      System.out.println(x);
      // result_file.write(x);
      //result_file.write("\n");
    }
    result_file.close();
  }

  public static void searchElements(Element e) {
    if (e.tagName().equals("input")) {
      element_interactable.add(e);
    }
    if (e.tagName().equals("a")) {
      element_interactable.add(e);
    }
    if (e.tagName().equals("button")) {
      element_interactable.add(e);
    }
    if (e.tagName().equals("select")) {
      element_interactable.add(e);
    }
//        if(e.tagName().equals("audio")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("details")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("embed")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("iframe")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("img")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("keygen")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("menu")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("object")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("textarea")){
//            element_interactable.add(e);
//        }
//        if(e.tagName().equals("video")){
//            element_interactable.add(e);
//        }
    for (Element child : e.children()) {
      searchElements(child);
    }
  }

  public static void SearchLocator(String key, FileWriter writer) throws IOException {
    String beautiful_key = beautifulString(key);
    int length = beautiful_key.length();
    for (String s : locator) {
      String beautiful_s = beautifulString(s);
      if (beautiful_s.contains(beautiful_key)) {
        result.add(key + " has " + s);
        writer.write(key + " xpath=" + s + "\n");
      }
//            String s_lower = s.toLowerCase();
//            int count = 0;
//            int index = 0;
//            int cur = 0;
//            while(cur < s_lower.length() && index < length){
//                if(s_lower.charAt(cur) == beautiful_key.charAt(index)){
//                   count++;
//                   index++;
//                }
//                cur++;
//            }
//            if(count == length){
//                result.add(key + " has " + s);
//                writer.write(key + " xpath=" + s + "\n");
//            }
    }
  }

  public static String beautifulString(String x) {
    String temp = "";
    String x_lower = x.toLowerCase();
    for (int i = 0; i < x_lower.length(); i++) {
      if (x_lower.charAt(i) >= 'a' && x_lower.charAt(i) <= 'z') {
        temp += x_lower.charAt(i);
      }
    }
    return temp;
  }

  public static void getXpath(Element e) {
    int attributes_size = e.attributesSize();
    System.out.print(attributes_size + " ");
    if (attributes_size > 0) {
      Attributes attr = e.attributes();
      for (Attribute temp : attr) {
        locator.add(
            "//" + e.tagName() + "[" + "@" + temp.getKey() + "=" + "'" + temp.getValue() + "'"
                + "]");
      }
    }
  }
}
