package invalid.strategies;

public interface Strategy {
    String exprEncode(String expr);

    void exprToMap(String expr);

    String searchValidValue(String expr);
}
