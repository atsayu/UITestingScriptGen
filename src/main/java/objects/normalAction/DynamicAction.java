package objects.normalAction;

public abstract class DynamicAction extends NormalAction {
    private String value;

    public DynamicAction(String elementLocator, boolean dynamic, boolean required, String value) {
        super(elementLocator,value, dynamic, required);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
