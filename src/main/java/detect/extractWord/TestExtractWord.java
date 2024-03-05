package detect.extractWord;

import detect.Process;
import detect.ver2.Click;
import detect.ver2.InputElement;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExtractWord {
    public static Map<String, List<String>> detectFromExtractWords(String linkHtml, Map<String, List<String>> map) {
        Map<String, List<String>> res = new HashMap<>();
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String type = entry.getKey();
            List<String> input = entry.getValue();
            if (type.equals("Input Text") && !input.isEmpty()) {
                Map<String, String> tmp = InputElement.detectInputElement(input, document);
                List<String> locators = new ArrayList<>();
                for (String s : input) {
                    locators.add(tmp.get(s));
                }
                res.put("Input Text", locators);
            }

            if (type.equals("Click") && !input.isEmpty()) {
                Map<String, String> tmp = Click.detectClickElement(input, document);
                List<String> locators = new ArrayList<>();
                for (String s : input) {
                    locators.add(tmp.get(s));
                }
                res.put("Click", locators);
            }

        }
        return res;

    }
}
