package objects.assertion;

import objects.Expression;

public class PageShouldNotContainElement implements Expression {
    private String locator;

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public PageShouldNotContainElement(String locator) {
        this.locator = locator;
    }

    public PageShouldNotContainElement(PageShouldNotContainElement pageShouldNotContainElement) {
        this.locator = pageShouldNotContainElement.locator;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }

    @Override
    public String exprToString() {
        return "Page Should Not Contain Element   " + locator;
    }
}
