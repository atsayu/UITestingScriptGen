package invalid;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.core.ReactiveAdapterRegistry;
import valid.ScriptGen;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import static invalid.DataPreprocessing.*;
import static invalid.PythonTruthTableServer.cnfParse;
import static invalid.PythonTruthTableServer.dnfParse;


public class NewInvalid {
    public static void main(String[] args) throws IOException, ParseException {
        Map<String, JSONArray> res = ScriptGen.createValidIndex("src/main/resources/template/outline.json", 2);

        // TODO: *** INVALID BLOCK ***

        JSONArray validBlock = res.get("validBlocks");
        Iterator validBlockIterator = validBlock.iterator();

        while(validBlockIterator.hasNext()) {
            Map<String, JSONArray> actionsMap = (Map<String, JSONArray>) validBlockIterator.next();
            JSONArray actions = actionsMap.get("actions");
            Vector<String> template = new Vector<>();
            Iterator actionsIterator = actions.iterator();
            while(actionsIterator.hasNext()) {
                Map<String, String> testMap = (Map<String, String>) actionsIterator.next();
                String lineTemp = "";
                for (Map.Entry<String, String> e : testMap.entrySet()) {
                    lineTemp += "\t" + e.getValue();
                }
                template.add(lineTemp);
            }
            System.out.println(template);
        }

        // TODO: *** INVALID BLOCK ***

        JSONArray invalidBlock = res.get("invalidBlock");
        System.out.println("invalidActions:");
        Vector<String> template = parseInvalid(invalidBlock);
        System.out.println(template);
        int lineIndex = 1;
        for (String line : template) {
            exprToMap(line);
            String encodedExpr = exprEncode(line);
            dnfLineDict.put("LINE" + lineIndex, dnfParse(encodedExpr));
            cnfLineDict.put("LINE" + lineIndex, cnfParse(encodedExpr));
            lineIndex++;
        }
        System.out.println(cnfLineDict);
        System.out.println(dnfLineDict);

        // TODO: *** DATA BLOCK ***

        JSONArray data = res.get("data");
        Iterator dataIterator = data.iterator();
        while(dataIterator.hasNext()) {
            System.out.println("data:");
            Map<String, String> dataMap = (Map<String, String>) dataIterator.next();
            for (Map.Entry<String, String> e : dataMap.entrySet()) {
                System.out.println(e.getKey() + ": " + e.getValue());
            }
        }
    }

    private static Vector<String> parseInvalid(JSONArray actions) {
        Vector<String> template = new Vector<>();
        Iterator actionsIterator = actions.iterator();
        while(actionsIterator.hasNext()) {
            String actionString = "";
            Map<String, JSONArray> actionsMap = (Map<String, JSONArray>) actionsIterator.next();
            String type = String.valueOf(actionsMap.get("type"));
            if (type.equals("and") || type.equals("or")) {
                Vector<String> complexActions = parseInvalid(actionsMap.get("actions"));
                if (type.equals("and")) {
                    for (String simpleActions : complexActions) {
                        actionString += simpleActions + "%26";
                    }
                    actionString = actionString.substring(0, actionString.length()-3);
                } else if (type.equals("or")) {
                    for (String simpleActions : complexActions) {
                        actionString += simpleActions + "|";
                    }
                    actionString = actionString.substring(0, actionString.length()-1);
                }
                actionString = "%28" + actionString + "%29";
            } else if (type.equals("input")) {
                actionString += type + "\t" + actionsMap.get("describedLocator") + "\t" + actionsMap.get("value");
            } else if (type.equals("verifyURL")) {
                actionString += type + "\t" + actionsMap.get("url");
            } else if (type.equals("click")) {
                actionString += type + "\t" + actionsMap.get("describedLocator");
            }
            template.add(actionString);
        }
        return template;
    }
}
