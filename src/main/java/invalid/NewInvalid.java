package invalid;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import valid.ScriptGen;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


public class NewInvalid {
    public static void main(String[] args) throws IOException, ParseException {
        Map<String, JSONArray> res = ScriptGen.createValidIndex("src/main/resources/template/outline.json", 2);
        JSONArray validBlock = res.get("validBlocks");
        JSONArray data = res.get("data");
        Iterator validBlockIterator = validBlock.iterator();

        while(validBlockIterator.hasNext()) {
            Map<String, JSONArray> actionsMap = (Map<String, JSONArray>) validBlockIterator.next();
            JSONArray actions = actionsMap.get("actions");
            System.out.println("Test:");
            Vector<String> template = new Vector<>();
            Iterator actionsIterator = actions.iterator();
            while(actionsIterator.hasNext()) {
                Map<String, String> testMap = (Map<String, String>) actionsIterator.next();
                System.out.println("Action:");
                String lineTemp = "";
                for (Map.Entry<String, String> e : testMap.entrySet()) {
                    System.out.println(e.getKey() + ": " + e.getValue());
                    lineTemp += "\t" + e.getValue();
                }
                template.add(lineTemp);
                System.out.println("\n");
            }
            System.out.println(template);
        }

        Iterator dataIterator = data.iterator();
        while(dataIterator.hasNext()) {
            System.out.println("data:");
            Map<String, String> dataMap = (Map<String, String>) dataIterator.next();
            for (Map.Entry<String, String> e : dataMap.entrySet()) {
                System.out.println(e.getKey() + ": " + e.getValue());
            }
        }
    }
}
