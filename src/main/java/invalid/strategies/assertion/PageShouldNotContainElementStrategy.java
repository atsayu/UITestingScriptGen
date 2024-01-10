package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.assertion.PageShouldContainElement;
import objects.assertion.PageShouldNotContainElement;

import static invalid.DataPreprocessing.pageShouldNotContainElementMap;

public class PageShouldNotContainElementStrategy implements Strategy {

    @Override
    public String exprEncode(String expr) {
        for (String key : pageShouldNotContainElementMap.keySet()) {
            expr = expr.replaceAll(pageShouldNotContainElementMap.get(key).toString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        PageShouldNotContainElement psnce = new PageShouldNotContainElement(component[1]);
        if (!pageShouldNotContainElementMap.containsValue(psnce)) {
            pageShouldNotContainElementMap.put("psnce" + (pageShouldNotContainElementMap.keySet().size() + 1), psnce);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return pageShouldNotContainElementMap.get(expr).getLocator();
    }
}
