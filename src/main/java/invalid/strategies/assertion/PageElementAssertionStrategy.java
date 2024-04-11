package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.assertion.PageElementAssertion;

import static invalid.DataPreprocessing.pageShouldContainElementMap;

public class PageElementAssertionStrategy implements Strategy {

    @Override
    public String exprEncode(String expr) {
        for (String key : pageShouldContainElementMap.keySet()) {
            expr = expr.replaceAll(pageShouldContainElementMap.get(key).exprToString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split("\t");
        PageElementAssertion pea = new PageElementAssertion(component[1]);
        if (!pageShouldContainElementMap.containsValue(pea)) {
            pageShouldContainElementMap.put("pea" + (pageShouldContainElementMap.keySet().size() + 1), pea);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return pageShouldContainElementMap.get(expr).getLocator();
    }
}
