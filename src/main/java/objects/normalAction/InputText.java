package objects.normalAction;

import mockpage.Input;
import objects.Expression;

public class InputText extends DynamicAction {

    public InputText(String elementLocator, String value, boolean dynamic, boolean required) {
        super(elementLocator, dynamic, required, value);
    }

    public InputText(InputText inputText) {
        super(inputText.getElementLocator(), inputText.isDynamic(), inputText.isRequired(), inputText.getValue());
    }

    @Override
    public boolean isExprEquals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof InputText it)) return false;

        return this.getElementLocator().equals(it.getElementLocator()) && this.getValue().equals(it.getValue());
    }

    public String getType() {
        return "Input Text";
    }
    public String exprToString() {
        return "Input Text\t" + this.getElementLocator() + "\t" + this.getValue();
    }
    @Override
    public int compareTo(Expression o) {
        if (o instanceof InputText) {
            return this.getElementLocator().compareTo(((InputText) o).getElementLocator());
        }
        return this.getType().compareTo(o.getType());
    }

}
