package detect.ver2;

import detect.HandleInput;
import detect.HandleSelect;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;



public class SelectElement {

    public static Map<String, String> detectSelectElement(List<String> input, Document document) {
        Elements selectElements = HandleSelect.getSelectElements(document);
        Map<String, String> result = new HashMap<>();
        List<Element> visited = new ArrayList<>();
        List<Weight> list = new ArrayList<>();
        for (String s : input) {
            int max_weight = -1;
            double max_full = -1;
            Element res = null;
            String tmp = "";
            for (Element e : selectElements) {
                if (visited.contains(e)) {
                    continue;
                }
                String text = HandleSelect.getTextForSelect(e);
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
            visited.add(res);

        }
//        Map<String, String> res = new HashMap<>();
//        if (list.size() == 0) {
//            Weight w = list.get(0);
//            String source = w.source;
//            Element e = w.e;
//            System.out.println(source + " " + e + " " + w.getFull() + " " + w.getWeight());
//            res.put(w.source, Process.getXpath(e));
//        } else {
//            Collections.sort(list);
//            Map<String, Element> visited = new HashMap<>();
//            List<String> visitedInput = new ArrayList<>();
//            for (int i = list.size() - 1; i >= 0; i--) {
//                String source = list.get(i).source;
//                Element e = list.get(i).e;
//                if (!visited.containsValue(e) && !visited.containsKey(source)) {
//                    visited.put(source, e);
//                    visitedInput.add(source);
//                    res.put(source, Process.getXpath(e));
//                    System.out.println(source + " " + Process.getXpath(e) + " " + list.get(i).text  + " " + list.get(i).getFull() + " " + list.get(i).getWeight());
//                }
//                if (visitedInput.size() == input.size()) {
//                    break;
//                }
//            }
//        }
        return result;
    }

    public static void main(String[] args) {
        String linkHtml = "https://demoqa.com/select-menu";
//        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        List<String> input = new ArrayList<>();
//        input.add("departing");
//        input.add("Destination");
//        input.add("airline");
//        input.add("Fare");
//        input.add("country in address");
//        input.add("month");
//        input.add("day");
//        input.add("year");
        input.add("Old Style Select Menu");
        Map<String, String> res = detectSelectElement(input, document);
    }
}
