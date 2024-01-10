package invalid.strategies.action;


import invalid.strategies.Strategy;
import objects.action.InputText;

import static invalid.DataPreprocessing.inputTextMap;

public class SelectRadioButtonStrategy implements Strategy {
    @Override
    public String exprEncode(String expr) {
        for (String key : inputTextMap.keySet()) {
            expr = expr.replaceAll(inputTextMap.get(key).toString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        InputText it = new InputText(component[1], component[2]);
        if (!inputTextMap.containsValue(it)) {
            inputTextMap.put("srb" + (inputTextMap.keySet().size() + 1), it);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return inputTextMap.get(expr).getValue();
    }
}
