package objects.normalAction;

import objects.Expression;

public class SelectRadioButton extends DynamicAction {
    // groupName = elementLocator

    public String getType() {
        return "Select Radio Button";
    }



    public SelectRadioButton(String elementLocator, String value, boolean dynamic, boolean required) {
        super(elementLocator, dynamic, required, value);
    }

    public SelectRadioButton(SelectRadioButton selectRadioButton) {
        super(selectRadioButton.getElementLocator(), selectRadioButton.isDynamic(), selectRadioButton.isDynamic(), selectRadioButton.getValue());
    }

    public boolean isExprEquals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SelectRadioButton selectRadioButton)) {
            return false;
        }

        return this.getElementLocator().equals(selectRadioButton.getElementLocator()) && this.getValue().equals(selectRadioButton.getValue());
    }

    public String exprToString() {
        return "Select Radio Button\t" + this.getElementLocator() + "\t" + this.getValue();
    }
    @Override
    public int compareTo(Expression o) {
        if (o instanceof SelectRadioButton) {
            int locatorComparison = this.getElementLocator().compareTo(((NormalAction) o).getElementLocator());
            return locatorComparison != 0? locatorComparison : this.getValue().compareTo(((SelectRadioButton) o).getValue());
        }
        return this.getType().compareTo(o.getType());
    }

}
