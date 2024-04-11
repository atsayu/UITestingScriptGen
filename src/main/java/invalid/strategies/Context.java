package invalid.strategies;

import invalid.strategies.action.ClickElementStrategy;
import invalid.strategies.action.InputTextStrategy;
import invalid.strategies.assertion.LocationAssertionStrategy;
import invalid.strategies.assertion.PageElementAssertionStrategy;

import java.util.Vector;

public class Context {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public String exprEncode(String expr) {
        this.setStrategy(new InputTextStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new ClickElementStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new LocationAssertionStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new PageElementAssertionStrategy());
        expr = strategy.exprEncode(expr);
        return expr;
    }

    public void exprToMap(String expr) {
        if (expr.contains("Input Text")) {
            this.setStrategy(new InputTextStrategy());
        } else if (expr.contains("Click Element")) {
            this.setStrategy(new ClickElementStrategy());
        } else if (expr.contains("Location Should Be")) {
            this.setStrategy(new LocationAssertionStrategy());
        } else if (expr.contains("Element Should Contain")) {
            this.setStrategy(new PageElementAssertionStrategy());
        }
        strategy.exprToMap(expr);
    }

    public String searchValidValue(String expr) {
        if (expr.contains("it")) {
            this.setStrategy(new InputTextStrategy());
        } else if (expr.contains("ce")) {
            this.setStrategy(new ClickElementStrategy());
        } else if (expr.contains("pea")) {
            this.setStrategy(new PageElementAssertionStrategy());
        } else if (expr.contains("la")) {
            this.setStrategy(new LocationAssertionStrategy());
        }
        return strategy.searchValidValue(expr);
    }

    public Vector<String> getMultiValid (Vector<String> header) {
        Vector<String> headerKey = new Vector<>();
        for (String expr : header) {
            if (expr.contains("it")) {
                this.strategy = new InputTextStrategy();
            } else if (expr.contains("ce")) {
                this.strategy = new ClickElementStrategy();
            } else if (expr.contains("la")) {
                this.strategy = new LocationAssertionStrategy();
            } else if (expr.contains("pea")) {
                this.strategy = new PageElementAssertionStrategy();
            }
            if (!headerKey.contains(strategy.searchValidValue(expr))) {
                headerKey.add(strategy.searchValidValue(expr));
            }
        }
        return headerKey;
    }

    public static boolean isAssertion(String expr) {
        return expr.contains("la") || expr.contains("pea");
    }

    public static boolean isDynamic(String expr) {
        return expr.contains("la") || expr.contains("it");
    }
}
