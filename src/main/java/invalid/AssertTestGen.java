package invalid;

import java.util.Vector;

import static invalid.DataPreprocessing.lineDict;

public class AssertTestGen {
    Vector<Vector<String>> assertMap = new Vector<>();
    public static Vector<String> assertTestGenInit() {
        int i = 1;
        Vector<String> assertHeap = new Vector<>();
        while (lineDict.get("LINE" + i) != null) {
            if (lineDict.get("LINE" + i).size() > 1) {
                continue;
            } else {

            }
            i++;
        }
        return new Vector<>();
    }
}