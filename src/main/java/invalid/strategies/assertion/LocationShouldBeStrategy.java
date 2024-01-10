package invalid.strategies.assertion;

import invalid.strategies.Strategy;
import objects.action.ClickElement;
import objects.assertion.LocationShouldBe;

import static invalid.DataPreprocessing.locationShouldBeMap;

public class LocationShouldBeStrategy implements Strategy {

    @Override
    public String exprEncode(String expr) {
        for (String key : locationShouldBeMap.keySet()) {
            expr = expr.replaceAll(locationShouldBeMap.get(key).toString(), key);
        }

        return expr;
    }

    @Override
    public void exprToMap(String expr) {
        String[] component = expr.split(" {3}");
        LocationShouldBe lsb = new LocationShouldBe(component[1]);
        if (!locationShouldBeMap.containsValue(lsb)) {
            locationShouldBeMap.put("lsb" + (locationShouldBeMap.keySet().size() + 1), lsb);
        }
    }

    @Override
    public String searchValidValue(String expr) {
        return locationShouldBeMap.get(expr).getUrl();
    }
}
