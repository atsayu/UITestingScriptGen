package objects.assertion;

import objects.Expression;

public class ElementShouldContain implements Expression {
    private String locator;
    private String expected;

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public ElementShouldContain(String locator, String expected) {
        this.locator = locator;
        this.expected = expected;
    }

    public ElementShouldContain(ElementShouldContain elementShouldContain) {
        this.locator = elementShouldContain.locator;
        this.expected = elementShouldContain.expected;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }

    @Override
    public String exprToString() {
        return "Element Should Contain   " + locator + "   " + expected;
    }
}
