package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.assertion.LocationAssertion;

import static invalid.DataPreprocessing.locationShouldBeMap;

public class LocationAssertionStrategy implements Strategy {

    @Override
    public String exprEncode(String expr) {
        for (String key : locationShouldBeMap.keySet()) {
            expr = expr.replaceAll(locationShouldBeMap.get(key).exprToString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split("\t");
        LocationAssertion la = new LocationAssertion(component[1]);
        if (!locationShouldBeMap.containsValue(la)) {
            locationShouldBeMap.put("la" + (locationShouldBeMap.keySet().size() + 1), la);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return locationShouldBeMap.get(expr).getUrl();
    }
}
