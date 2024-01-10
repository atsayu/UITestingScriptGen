package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.assertion.ElementShouldBeVisible;

import static invalid.DataPreprocessing.elementShouldBeVisibleMap;

public class ElementShouldBeVisibleStrategy implements Strategy {
    @Override
    public String exprEncode(String expr) {
        for (String key : elementShouldBeVisibleMap.keySet()) {
            expr = expr.replaceAll(elementShouldBeVisibleMap.get(key).toString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        ElementShouldBeVisible esbv = new ElementShouldBeVisible(component[1]);
        if (!elementShouldBeVisibleMap.containsValue(esbv)) {
            elementShouldBeVisibleMap.put("esbv" + (elementShouldBeVisibleMap.keySet().size() + 1), esbv);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return elementShouldBeVisibleMap.get(expr).getLocator();
    }
}
