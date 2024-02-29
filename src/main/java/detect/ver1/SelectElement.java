package detect.ver1;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import detect.Calculator;
import detect.Process;
import detect.TypeElement;
import java.util.*;

public class SelectElement {
    static Map<String, List<Element>> textAndElement = new HashMap<>();
    static List<String> listText = new ArrayList<>();

    public static void getAllTextForSelect(Element e) {
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
            getAllTextForSelect(child);
        }
    }


    public static Map<String, String> detectSelectElement(List<String> input, Document document) {
        Element body = document.body();
        getAllTextForSelect(body);
        System.out.println(listText);
        List<Weight> listWeight = new ArrayList<>();
        for (String s : input) {
            for (String text : listText) {
                if (Calculator.weightBetweenTwoString(s,text) > 0) {
                    List<Element> list = textAndElement.get(text);
                    Weight w = new Weight(s, text, list, document, "select");
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
}
