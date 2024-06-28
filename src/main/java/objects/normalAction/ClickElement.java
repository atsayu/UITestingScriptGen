package objects.normalAction;

import objects.Expression;
import objects.NormalAction;

public class ClickElement extends NormalAction {
    public String getType() {
        return "Click Element";
    }

    public ClickElement(String elementLocator, boolean dynamic, boolean required) {
        super(elementLocator,null, dynamic, required);
    }

    public ClickElement(String elementLocator) {
        super(elementLocator,null, false, false);
    }
    public ClickElement(ClickElement clickElement) {
        super(clickElement.getElementLocator(),null, clickElement.isDynamic(), clickElement.isRequired());
    }
    @Override
    public boolean isExprEquals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ClickElement ce)) {
            return false;
        }

        return (this.getElementLocator().equals(ce.getElementLocator()));
    }

    @Override
    public String exprToString() {
        return "click\t" + this.getElementLocator();
    }

    @Override
    public int compareTo(Expression o) {
        if (o instanceof NormalAction) {
            return this.getElementLocator().compareTo(((NormalAction) o).getElementLocator());
        }
        return this.getType().compareTo(o.getType());
    }
}
