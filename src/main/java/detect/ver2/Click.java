package detect.ver2;

import detect.HandleClick;
import detect.HandleInput;
import detect.HandleString;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class Click {
    public static Map<String, String> detectClickElement(List<String> input, Document document) {
        Map<String, String> result = new HashMap<>();
        List<String> inputForClickable = new ArrayList<>();
        List<String> inputForNonClickable = new ArrayList<>();
        for (String source : input) {
            List<String> wordsInSource = HandleString.separateWordsInString(source);
            HandleString.lowercaseWordsInList(wordsInSource);
            if (wordsInSource.contains("button") || wordsInSource.contains("btn")) {
                inputForClickable.add(source);
            } else {
                inputForNonClickable.add(source);
            }
        }
        System.out.println(inputForClickable);
        List<Element> clickableElements = HandleClick.getClickableElements(document);
        List<Element> visitedClickableElements = new ArrayList<>();
        for (String s : inputForClickable) {
            int max_weight = -1;
            double max_full = -1;
            Element res = null;
            String tmp = "";
            for (Element e : clickableElements) {
                if (visitedClickableElements.contains(e)) {
                    continue;
                }
                String text = HandleClick.getTextForClickableElement(e);
                Weight w = new Weight(s, e, text);
                double full = w.getFull();
                int weight = w.getWeight();
                if (full > max_full) {
                    res = e;
                    max_full = full;
                    max_weight = weight;
                    tmp = text;
                } else {
                    if (full == max_full) {
                        if (weight > max_weight) {
                            res = e;
                            max_weight = weight;
                            tmp = text;
                        }
                    }
                }

//                list.add(w);
            }
            if (res != null) {
                result.put(s, Process.getXpath(res));
                System.out.println(s + " " + Process.getXpath(res) + max_full + " " + max_weight + " " + tmp);
            }
            visitedClickableElements.add(res);
        }
        return result;
    }

    public static void main(String[] args) {

        String linkHtml = "https://form.jotform.com/233591551157458";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        List<String> click = Arrays.asList("next_btn", "back_btn", "submit_Button");
        Map<String, String> res = detectClickElement(click, document);
    }


}
