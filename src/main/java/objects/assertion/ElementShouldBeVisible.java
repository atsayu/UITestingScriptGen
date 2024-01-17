package objects.assertion;

import objects.Expression;

public class ElementShouldBeVisible implements Expression {
    private String locator;

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public ElementShouldBeVisible(String locator) {
        this.locator = locator;
    }

    public ElementShouldBeVisible(ElementShouldBeVisible elementShouldBeVisible) {
        this.locator = elementShouldBeVisible.locator;
    }

    @Override
    public String exprToString() {
        return "Element Should Be Visible   " + locator;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }
}
