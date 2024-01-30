//package invalid.strategies.assertion;
//
//import invalid.strategies.Strategy;
//import objects.assertion.ElementShouldContain;
//
//import static invalid.DataPreprocessing.elementShouldContainMap;
//
//public class ElementShouldContainStrategy implements Strategy {
//
//    @Override
//    public String exprEncode(String expr) {
//        for (String key : elementShouldContainMap.keySet()) {
//            expr = expr.replaceAll(elementShouldContainMap.get(key).exprToString(), key);
//        }
//
//        return expr;
//    }
//
//    @Override
//    public void exprToMap(String expr) {
//        String[] component = expr.split(" {3}");
//        ElementShouldContain esc = new ElementShouldContain(component[1], component[2]);
//        if (!elementShouldContainMap.containsValue(esc)) {
//            elementShouldContainMap.put("esc" + (elementShouldContainMap.keySet().size() + 1), esc);
//        }
//    }
//
//    @Override
//    public String searchValidValue(String expr) {
//        return elementShouldContainMap.get(expr).getExpected();
//    }
//}
