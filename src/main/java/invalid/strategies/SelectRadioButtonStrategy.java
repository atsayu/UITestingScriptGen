package invalid.strategies;

import objects.SelectRadioButton;

import static invalid.DataPreprocessing.selectRadioButtonMap;


public class SelectRadioButtonStrategy implements Strategy{
    @Override
    public String exprEncode(String expr) {
        for (String key : selectRadioButtonMap.keySet()) {
            expr = expr.replaceAll(selectRadioButtonMap.get(key).toString(), key);
        }
        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        SelectRadioButton srb = new SelectRadioButton(component[1], component[2], component[3]);
        if (!selectRadioButtonMap.containsValue(srb)) {
            selectRadioButtonMap.put("srb" + (selectRadioButtonMap.keySet().size() + 1), srb);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return selectRadioButtonMap.get(expr).getValue();
    }
}
