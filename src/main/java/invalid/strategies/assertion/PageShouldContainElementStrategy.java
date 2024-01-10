package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.action.ClickElement;
import objects.assertion.PageShouldContainElement;

import static invalid.DataPreprocessing.pageShouldContainElementMap;

public class PageShouldContainElementStrategy implements Strategy {

    @Override
    public String exprEncode(String expr) {
        for (String key : pageShouldContainElementMap.keySet()) {
            expr = expr.replaceAll(pageShouldContainElementMap.get(key).toString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        PageShouldContainElement psce = new PageShouldContainElement(component[1]);
        if (!pageShouldContainElementMap.containsValue(psce)) {
            pageShouldContainElementMap.put("psce" + (pageShouldContainElementMap.keySet().size() + 1), psce);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return pageShouldContainElementMap.get(expr).getLocator();
    }
}
