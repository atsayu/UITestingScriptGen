package objects.action;

import objects.Expression;

public class InputText implements Expression {
    private String value;
    private String locator;


    public InputText(String locator, String value) {
        this.locator = locator;
        this.value = value;
    }

    public InputText(InputText inputText) {
        this.locator = inputText.locator;
        this.value = inputText.value;
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
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof InputText it)) {
            return false;
        }

        return (this.getLocator().equals(it.getLocator()) && this.getValue().equals(it.getValue()));
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }

    @Override
    public String toString() {
        return "Input Text   " + this.getLocator() + "   " + this.value;
    }
}
