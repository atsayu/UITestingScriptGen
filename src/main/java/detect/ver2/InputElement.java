package detect.ver2;

import detect.HandleInput;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.*;

public class InputElement {
    public static Map<String, String> detectInputElement(List<String> input, Document document) {
        Elements inputElements = HandleInput.getInputElements(document);
        List<Element> visited = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
//        List<Weight> list = new ArrayList<>();
        for (String s : input) {
            int max_weight = -1;
            double max_full = -1;
            Element res = null;
            String tmp = "";
            for (Element e : inputElements) {
                if (visited.contains(e)) {
                    continue;
                }
                String text = HandleInput.getTextForInput(e);
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
//        if (list.size() == 1) {
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
        String linkHtml = "https://www.saucedemo.com/";
//        String linkHtml = "https://form.jotform.com/233591551157458?fbclid=IwAR1ggczzG7OoN6Dgb2SDWtNyznCAAJNW-G8-_3gnejJwPFunwwBuN_NCvh0";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        List<String> input = new ArrayList<>();
        input.add("username");
        input.add("pass");
//        input.add("First-name_in_passenger");
//        input.add("last_name in passenger");
//        input.add("first_name in contact_person");
//        input.add("last-Name In contact_person");
//        input.add("title in contact person");
//        input.add("Title in passenger name");
//        input.add("e-mail");
//        input.add("area code");
//        input.add("phone");
//        input.add("street address");
//        input.add("street address 2");
//        input.add("city in address");
//        input.add("zip");
//        input.add("state or province");
        Map<String, String> res = detectInputElement(input, document);

    }
}
