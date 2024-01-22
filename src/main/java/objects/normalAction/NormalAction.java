package objects.normalAction;

import objects.Expression;

public abstract class NormalAction implements Expression {
    private String elementLocator;
    private boolean dynamic;
    private boolean required;

    public NormalAction(String elementLocator, boolean dynamic, boolean required) {
        this.elementLocator = elementLocator;
        this.dynamic = dynamic;
        this.required = required;
    }

    public String getElementLocator() {
        return elementLocator;
    }

    public void setElementLocator(String elementLocator) {
        this.elementLocator = elementLocator;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public abstract boolean isExprEquals(Object obj);
    public abstract String exprToString();
    public abstract String getType();

    @Override
    public abstract int compareTo(Expression o);

}
