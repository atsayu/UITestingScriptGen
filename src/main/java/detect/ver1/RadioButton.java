package detect.ver1;

import detect.Calculator;
import detect.TypeElement;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

public class RadioButton {
    static Map<String, List<Element>> textAndElement = new HashMap<>();
    static List<String> listText = new ArrayList<>();

    public static void getAllTextForRadioButton(Element e) {
        if (e == null || TypeElement.isInteractableElement(e)) {
            return;
        }
        String text = e.ownText();
        if (!text.isEmpty() && !listText.contains(text)) {
            listText.add(text);
        }
        if (!text.isEmpty()) {
            if (textAndElement.containsKey(text)) {
                List<Element> list = textAndElement.get(text);
                if (!list.contains(e)) {
                    list.add(e);
                }
            } else {
                List<Element> list = new ArrayList<>();
                list.add(e);
                textAndElement.put(text, list);
            }
        }
        for (Element child : e.children()) {
            getAllTextForRadioButton(child);
        }
    }


    public static Map<String, String> detectRadioButtonElement(Map<String, String> map, Document document) {
        Element body = document.body();
        getAllTextForRadioButton(body);
        List<WeightRadioButton> listWeight = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String question = entry.getKey();
            String choice = entry.getValue();
            for (String text : listText) {
                if (Calculator.weightBetweenTwoString(question,text) > 0) {
                    List<Element> list = textAndElement.get(text);
                    WeightRadioButton w = new WeightRadioButton(question, text, list, choice);
                    listWeight.add(w);
                }
            }
        }
        if (listWeight.size() == 1) {
            listWeight.get(0).getWeight();
        } else {
            Collections.sort(listWeight);
        }


        Map<String, String> storeInputAndLocator = new HashMap<>();
        Map<String, Element> storeInputAndElement = new HashMap<>();
        for (int i = listWeight.size() - 1; i >= 0; i--) {
            String source = listWeight.get(i).source;
            Element result = listWeight.get(i).result;
            if (result != null) {
                if (!storeInputAndElement.containsKey(source) && !storeInputAndElement.containsValue(result)) {
                    storeInputAndElement.put(source, result);
                    String loc = Process.getXpath(result);
                    storeInputAndLocator.put(source, loc);
                    System.out.println(1 + " " + source + " " +  listWeight.get(i).text + " " + result + " " + listWeight.get(i).weight + " " + listWeight.get(i).full) ;
                }
            }
        }
        return storeInputAndLocator;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591762291461";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Map<String, String> map = new HashMap<>();
        map.put("Are you currently taking any medication?", "No");
        map.put("Do you have any medication allergies?", "Yes");
        map.put(" consume alcohol", "Never");
        Map<String, String> res = detectRadioButtonElement(map, document);
    }
}
