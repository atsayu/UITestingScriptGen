package invalid.strategies.action;

import invalid.strategies.Strategy;
import objects.action.SelectFromListByValue;

import static invalid.DataPreprocessing.selectFromListByValueMap;

public class SelectFromListByValueStrategy implements Strategy {
    @Override
    public String exprEncode(String expr) {
        for (String key : selectFromListByValueMap.keySet()) {
            expr = expr.replaceAll(selectFromListByValueMap.get(key).exprToString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        SelectFromListByValue sflbv = new SelectFromListByValue(component[1], component[2]);
        if (!selectFromListByValueMap.containsValue(sflbv)) {
            selectFromListByValueMap.put("sflbv" + (selectFromListByValueMap.keySet().size() + 1), sflbv);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return selectFromListByValueMap.get(expr).getValue();
    }
}
