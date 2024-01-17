package invalid.strategies.action;


import invalid.strategies.Strategy;
import objects.action.SelectRadioButton;

import static invalid.DataPreprocessing.selectRadioButtonMap;

public class SelectRadioButtonStrategy implements Strategy {
    @Override
    public String exprEncode(String expr) {
        for (String key : selectRadioButtonMap.keySet()) {
            expr = expr.replaceAll(selectRadioButtonMap.get(key).exprToString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        SelectRadioButton it = new SelectRadioButton(component[1], component[2], component[3]);
        if (!selectRadioButtonMap.containsValue(it)) {
            selectRadioButtonMap.put("srb" + (selectRadioButtonMap.keySet().size() + 1), it);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return selectRadioButtonMap.get(expr).getValue();
    }
}
