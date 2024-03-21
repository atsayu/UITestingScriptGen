package detect.ver2;

import detect.Calculator;
import detect.HandleElement;
import detect.HandleSelect;
import detect.Pair;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectElement {
    public static Map<Pair<String, String>, Element> detectSelectElement(List<Pair<String, String>> list, Elements selectElements) {
        Map<Pair<String, String>, Element> res = new HashMap<>();
        for (Pair<String, String> pair : list) {
            String question = pair.getFirst();
            String choice = pair.getSecond();
            Element tmp = null;
            int max_weight = -1;
            double max_full = -1;
            for (Element e : selectElements) {
                if (HandleSelect.hasOption(e, choice)) {
                    if (question.isEmpty()) {
                        tmp = e;
                        break;
                    } else {
                        String t = HandleSelect.getTextForSelect(e);
                        System.out.println(question + " " + t);
                        Weight w = new Weight(question, e, t);
                        double full = w.getFull();
                        int weight = w.getWeight();
                        if (Calculator.compareWeight(max_weight, max_full, weight, full) > 0) {
                            tmp = e;
                            max_full = full;
                            max_weight = weight;
                        }
                    }
                }
            }
            if (tmp != null) {
                res.put(pair, tmp);
            } else {
                System.out.println("Cant detect element with pair " + "question is " + question + " and choice is " + choice);
            }

        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Elements selectElements = HandleSelect.getSelectElements(document);
        List<Pair<String, String >> list = new ArrayList<>();
        list.add(new Pair<>("", "Cong"));
        list.add(new Pair<>("Country", "Aruba"));
        list.add(new Pair<>("Happy", "One Way"));
        list.add(new Pair<>("Airline", "Airline 1"));
        Map<Pair<String, String>, Element> res = detectSelectElement(list, selectElements);
        for (Map.Entry<Pair<String, String>, Element> entry : res.entrySet()) {
            Pair<String, String> pair = entry.getKey();
            Element e = entry.getValue();
            System.out.println(pair.getFirst() + " " + pair.getSecond() + " " + Process.getXpath(e));
        }
    }
}
