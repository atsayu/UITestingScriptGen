package invalid.strategies;


import objects.SelectCheckbox;

import static invalid.DataPreprocessing.selectCheckboxMap;

public class SelectCheckboxStrategy implements Strategy{
    @Override
    public String exprEncode(String expr) {
        for (String key : selectCheckboxMap.keySet()) {
            expr = expr.replaceAll(selectCheckboxMap.get(key).toString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        SelectCheckbox sc = new SelectCheckbox(component[1]);
        if (!selectCheckboxMap.containsValue(sc)) {
            selectCheckboxMap.put("sc" + (selectCheckboxMap.keySet().size() + 1), sc);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return selectCheckboxMap.get(expr).getLocator();
    }
}
