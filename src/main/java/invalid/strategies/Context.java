package invalid.strategies;

import invalid.strategies.action.*;
import invalid.strategies.assertion.ElementShouldContainStrategy;
import invalid.strategies.assertion.LocationShouldBeStrategy;

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
        this.setStrategy(new LocationShouldBeStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new ElementShouldContainStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new SelectCheckboxStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new SelectRadioButtonStrategy());
        expr = strategy.exprEncode(expr);
        this.setStrategy(new SelectFromListByValueStrategy());
        expr = strategy.exprEncode(expr);
        return expr;
    }

    public void exprToMap(String expr) {
        if (expr.contains("Input Text")) {
            this.setStrategy(new InputTextStrategy());
        } else if (expr.contains("Click Element")) {
            this.setStrategy(new ClickElementStrategy());
        } else if (expr.contains("Location Should Be")) {
            this.setStrategy(new LocationShouldBeStrategy());
        } else if (expr.contains("Element Should Contain")) {
            this.setStrategy(new ElementShouldContainStrategy());
        } else if (expr.contains("Select Checkbox")) {
            this.setStrategy(new SelectCheckboxStrategy());
        } else if (expr.contains("Select Radio Button")) {
            this.setStrategy(new SelectRadioButtonStrategy());
        } else if (expr.contains("Select From List By Value")) {
            this.setStrategy(new SelectFromListByValueStrategy());
        }
        strategy.exprToMap(expr);
    }

    public String searchValidValue(String expr) {
        if (expr.contains("it")) {
            this.setStrategy(new InputTextStrategy());
        } else if (expr.contains("ce")) {
            this.setStrategy(new ClickElementStrategy());
        } else if (expr.contains("esc")) {
            this.setStrategy(new ElementShouldContainStrategy());
        } else if (expr.contains("lsb")) {
            this.setStrategy(new LocationShouldBeStrategy());
        } else if (expr.contains("sc")) {
            this.setStrategy(new SelectCheckboxStrategy());
        } else if (expr.contains("srb")) {
            this.setStrategy(new SelectRadioButtonStrategy());
        } else if (expr.contains("sflbv")) {
            this.setStrategy(new SelectFromListByValueStrategy());
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
            } else if (expr.contains("lsb")) {
                this.strategy = new LocationShouldBeStrategy();
            } else if (expr.contains("esc")) {
                this.strategy = new ElementShouldContainStrategy();
            } else if (expr.contains("sc")) {
                this.strategy = new SelectCheckboxStrategy();
            } else if (expr.contains("srb")) {
                this.strategy = new SelectRadioButtonStrategy();
            } else if (expr.contains("sflbv")) {
                this.strategy = new SelectFromListByValueStrategy();
            }
            if (!headerKey.contains(strategy.searchValidValue(expr))) {
                headerKey.add(strategy.searchValidValue(expr));
            }
        }
        return headerKey;
    }

    public static boolean isAssertion(String expr) {
        return expr.contains("lsb") || expr.contains("esc");
    }
}
