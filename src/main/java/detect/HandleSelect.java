package detect;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HandleSelect {
    public static Element searchSelectElementInSubtree(String text, Element e) {
        Elements elems = e.select("*");
        int cnt_interact = 0;
        Element select = null;
        for (Element ele : elems) {
            if (TypeElement.isInteractableElement(ele) && !TypeElement.isSelectElement(ele)) {
                return null;
            }
            if (ele.tagName().equals("option")) {
                continue;
            }
            String t = e.ownText();
            if (!t.isEmpty() && !t.equals(text)) {
                return null;
            }
            if (TypeElement.isSelectElement(ele)) {
                select = ele;
            }
        }
        if (select == null) {
            return searchSelectElementInSubtree(text, e.parent());
        }
        return select;
    }
}
