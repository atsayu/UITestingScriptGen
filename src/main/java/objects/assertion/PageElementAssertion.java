package objects.assertion;

import objects.Expression;

public class PageElementAssertion implements Expression {
    private String locator;

    public String getType() {
        return "Page Element Assertion";
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public PageElementAssertion(String locator) {
        this.locator = locator;
    }

    public PageElementAssertion(PageElementAssertion pageElementAssertion) {
        this.locator = pageElementAssertion.getLocator();
    }

    @Override
    public boolean isExprEquals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof PageElementAssertion pageElementAssertion)) return false;
        return this.locator.equals(pageElementAssertion.getLocator());
    }

    public String exprToString() {
        return "Page Should Contain\t" + this.locator;
    }

    @Override
    public int compareTo(Expression o) {
        if (o instanceof PageElementAssertion) {
            return this.locator.compareTo(((PageElementAssertion) o).locator);
        }
        return this.getType().compareTo(o.getType());
    }
}
