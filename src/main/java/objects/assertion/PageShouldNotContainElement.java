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
    public String toString() {
        return "Page Should Not Contain Element   " + locator;
    }
}
