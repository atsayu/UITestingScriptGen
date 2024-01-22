package objects.action;

import objects.Expression;

public class SelectFromListByValue implements Expression {
    private String locator;
    private String value;

    public SelectFromListByValue(String locator, String value) {
        this.locator = locator;
        this.value = value;
    }

    public SelectFromListByValue(SelectFromListByValue selectFromListByValue) {
        this.locator = selectFromListByValue.locator;
        this.value = selectFromListByValue.value;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SelectFromListByValue sflbv)) {
            return false;
        }

        return (this.getLocator().equals(sflbv.getLocator()) && this.getValue().equals(sflbv.getValue()));
    }

    @Override
    public String exprToString() {
        return "Select From List By Value   " + this.locator + "   " + this.value;
    }
}
