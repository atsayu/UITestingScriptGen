package objects.action;

import objects.Expression;

public class ClickElement implements Expression {
    private String locator;

    public ClickElement(String locator) {
        this.locator = locator;
    }

    public ClickElement(ClickElement clickElement) {
        this.locator = clickElement.locator;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ClickElement ce)) {
            return false;
        }

        return (this.getLocator().equals(ce.getLocator()));
    }

    @Override
    public String exprToString() {
        return "Click Element   " + this.getLocator();
    }
}
