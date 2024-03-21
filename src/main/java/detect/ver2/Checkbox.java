package detect.ver2;

import detect.*;
import detect.Process;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

public class Checkbox {

    public static void getAllTextForCheckbox(Element e, Map<String, List<Element>> textAndElement, List<String> listText, List<Element> listCheckbox) {
        if (TypeElement.isCheckboxElement(e)) {
            listCheckbox.add(e);
        }
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
            getAllTextForCheckbox(child, textAndElement, listText, listCheckbox);
        }
    }


    public static Map<Pair<String, String>, String> detectCheckboxElement(Map<String, List<String>> map, Document document) {
        Map<Pair<String, String>, String> res = new HashMap<>();
        Element body = document.body();
        Map<String, List<Element>> textAndElement = new HashMap<>();
        List<String> listText = new ArrayList<>();
        List<Element> listCheckbox = new ArrayList<>();
        getAllTextForCheckbox(body, textAndElement, listText, listCheckbox);
        List<Element> visitedCheckbox = new ArrayList<>();
        List<String> choicesHasNoCorrespondingQuestion = new ArrayList<>();
        if (map.containsKey("")) {
            choicesHasNoCorrespondingQuestion = map.get("");
        }
        System.out.println(choicesHasNoCorrespondingQuestion.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String question = entry.getKey();
            if (question.isEmpty()) {
                continue;
            }
            List<String> choices = entry.getValue();
            int max_weight = 0;
            double max_full = 0;
            String tmp = "";
            List<String> wordsInQuestion = HandleString.separateWordsInString(question);
            HandleString.lowercaseWordsInList(wordsInQuestion);
            List<String> distinctWordsInQuestion = HandleString.distinctWordsInString(wordsInQuestion);
            for (String text : listText) {
                Set<String> visitedWords = new HashSet<>();
                int weight = Calculator.weightBetweenTwoString(question, text);
                Calculator.calculatePercentBetweenTwoStrings(question, text, visitedWords);
                double full = 1.0 * visitedWords.size() / distinctWordsInQuestion.size();
                if (Calculator.compareWeight(max_weight, max_full, weight, full) > 0) {
                    tmp = text;
                    max_full = full;
                    max_weight = weight;
                }
            }
            for (Element ele : textAndElement.get(tmp)) {
                Map<String, Element> mp = HandleCheckbox.searchCheckboxInSubtree(ele, choices);
                if (mp != null) {
                    for (String choice : choices) {
                        Element checkbox = mp.get(choice);
                        visitedCheckbox.add(checkbox);
                        res.put(new Pair<>(question, choice), Process.getXpath(checkbox));
                    }
                }
            }

        }

        if (choicesHasNoCorrespondingQuestion.size() > 0) {
            List<String> visitedChoices = new ArrayList<>();
            for (Element checkbox : listCheckbox) {
                if (!visitedCheckbox.contains(checkbox)) {
                    String text = HandleCheckbox.getTextForCheckbox(checkbox);
                    if (!visitedChoices.contains(text) && choicesHasNoCorrespondingQuestion.contains(text)) {
                        res.put(new Pair<>("", text), Process.getXpath(checkbox));
                        visitedCheckbox.add(checkbox);
                        visitedChoices.add(text);
                    }
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String linkHtml = "https://form.jotform.com/233591762291461";
        String htmlContent = Process.getHtmlContent(linkHtml);
        Document document = Process.getDomTree(htmlContent);
        Map<String, List<String>> map = new HashMap<>();
//        map.put("hobbies", Arrays.asList("Sports", "Music"));
        map.put("check symptoms",  Arrays.asList("Chest pain", "Other"));
        map.put("", Arrays.asList("Cancer", "Asthma"));
        Map<Pair<String, String>, String> res = detectCheckboxElement(map, document);
        for (Map.Entry<Pair<String, String>, String> entry : res.entrySet()) {
            Pair<String, String> pair = entry.getKey();
            String loc = entry.getValue();
            System.out.println(pair.getFirst() + " " + pair.getSecond() + " " + loc);
        }
    }
}
