package detect;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class HandleClick {
    public static Elements getClickableElements (Document document) {
        Elements button_tag = document.getElementsByTag("button");
        Elements a_tag = document.getElementsByTag("a");
        Elements img_tag = document.getElementsByTag("img");
        Elements input_tag = document.getElementsByTag("input");
        Elements res = new Elements();
        res.addAll(button_tag);
        res.addAll(a_tag);
        res.addAll(img_tag);
        for (Element e : input_tag) {
            if (TypeElement.isClickElementTagInput(e)) {
                res.add(e);
            }
        }
        return res;
    }

    public static String getTextForClickableElement (Element e) {
        if (!TypeElement.isClickElementTagInput(e)) {
            return e.text();
        }
        return "";
    }

}
