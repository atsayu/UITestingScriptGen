package objects.action;

import objects.Expression;

public class SelectCheckbox implements Expression {
    private String locator;

    public SelectCheckbox(String locator) {
        this.locator = locator;
    }

    public SelectCheckbox(SelectCheckbox clickElement) {
        this.locator = clickElement.locator;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SelectCheckbox selectCheckbox)) {
            return false;
        }

        return (this.getLocator().equals(selectCheckbox.getLocator()));
    }

    @Override
    public String toString() {
        return "Select Checkbox   " + this.getLocator();
    }
}
