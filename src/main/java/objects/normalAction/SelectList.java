package objects.normalAction;

import objects.Expression;
import objects.NormalAction;

public class SelectList extends NormalAction {

    public String getType() {
        return "Select List";
    }
    public SelectList(String elementLocator, String value, boolean dynamic, boolean required) {
        super(elementLocator, value, dynamic, required);
    }

    public boolean isExprEquals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SelectList selectList)) {
            return false;
        }

        return this.getElementLocator().equals(selectList.getElementLocator()) && this.getValue().equals(selectList.getValue());
    }

    public String exprToString() {
        return "Select From List By Value\t" + this.getElementLocator() + "\t" + this.getValue();
    }

    @Override
    public int compareTo(Expression o) {
        if (o instanceof SelectList) {
            int locatorComparison = this.getElementLocator().compareTo(((NormalAction) o).getElementLocator());
            return locatorComparison != 0? locatorComparison : this.getValue().compareTo(((SelectList) o).getValue());
        }
        return this.getType().compareTo(o.getType());
    }

}
