package invalid.strategies.action;


import invalid.strategies.Strategy;
import objects.action.SelectCheckbox;

import static invalid.DataPreprocessing.selectCheckboxMap;


public class SelectCheckboxStrategy implements Strategy {
    @Override
    public String exprEncode(String expr) {
        for (String key : selectCheckboxMap.keySet()) {
            expr = expr.replaceAll(selectCheckboxMap.get(key).exprToString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        SelectCheckbox it = new SelectCheckbox(component[1]);
        if (!selectCheckboxMap.containsValue(it)) {
            selectCheckboxMap.put("sc" + (selectCheckboxMap.keySet().size() + 1), it);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return selectCheckboxMap.get(expr).getLocator();
    }
}
