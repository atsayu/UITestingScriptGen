package detect;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleCheckbox {
    public static Map<String, Element> searchCheckboxInSubtree(Element e, List<String> choices) {
        Elements elems = HandleElement.selectInteractableElementsInSubtree(e);
        if (elems.size() == 0) {
            return searchCheckboxInSubtree(e.parent()
                    , choices);
        }
        for (Element elem : elems) {
            if (!TypeElement.isCheckboxElement(elem)) {
                return null;
            }
        }
        Map<String, Element> res = new HashMap<>();
        int cnt = 0;
        for (Element elem :elems) {
            String t = getTextForCheckbox(elem);
            if (choices.contains(t)) {
                if (!res.containsKey(t)) {
                    res.put(t, elem);
                    cnt++;
                }
            }
        }
        if (cnt == choices.size()) {
            return res;
        }
        return null;
    }

    public static String getTextForCheckboxElementInSubtree(Element e) {
        Elements elems = e.select("*");
        int cnt_checkbox = 0;
        int cnt_text = 0;
        String tmp = "";
        for (Element elem : elems) {
            if (TypeElement.isInteractableElement(elem) && !TypeElement.isCheckboxElement(e)) {
                return "";
            }
            if (TypeElement.isCheckboxElement(e)) {
                cnt_checkbox++;
            }
            String t = elem.ownText();
            if (!t.isEmpty()) {
                cnt_text++;
                tmp = t;
            }
        }

        if (cnt_checkbox == 1 && cnt_text == 0) {
            return getTextForCheckboxElementInSubtree(e.parent());
        }
        if (cnt_checkbox == 1 && cnt_text == 1) {
            return tmp;
        }
        return "";
    }

    public static String getTextForCheckbox(Element e) {
        if (e.hasAttr("id") && !e.attr("id").isEmpty()) {
            String text = HandleElement.getAssociatedLabel(e.attr("id"), e);
            if (!text.isEmpty()) {
                return text;
            }
        }
        String res = getTextForCheckboxElementInSubtree(e);
        return res;
    }
}
