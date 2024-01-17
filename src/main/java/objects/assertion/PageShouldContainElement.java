package objects.assertion;

import objects.Expression;

public class PageShouldContainElement implements Expression {
    private String locator;

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public PageShouldContainElement(String locator) {
        this.locator = locator;
    }

    public PageShouldContainElement(PageShouldContainElement pageShouldContainElement) {
        this.locator = pageShouldContainElement.locator;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }

    @Override
    public String exprToString() {
        return "Page Should Contain Element   " + locator;
    }
}
