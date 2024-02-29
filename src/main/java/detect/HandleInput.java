package detect;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HandleInput {
    public static Element searchInputElementInSubtree(String text, Element e) {
        Elements input = HandleElement.selectInteractableElementsInSubtree(e);
        if (input.size() > 1 || (input.size() == 1 && !TypeElement.isInputElement(input.get(0))) || !e.text().equals(text)) {
            return null;
        }
        if (input.size() == 1 && TypeElement.isInputElement(input.get(0)) && e.text().equals(text)) {
            return input.get(0);
        }
        return searchInputElementInSubtree(text, e.parent());
    }
}
