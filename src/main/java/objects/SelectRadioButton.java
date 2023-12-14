package objects;

public class SelectRadioButton extends Expression{
    private String groupName;
    private String value;

    public SelectRadioButton(String locator, String groupName, String value) {
        super(locator);
        this.groupName = groupName;
        this.value = value;
    }

    public SelectRadioButton(SelectRadioButton selectRadioButton) {
        super("");
        this.groupName = selectRadioButton.groupName;
        this.value = selectRadioButton.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SelectRadioButton selectRadioButton)) {
            return false;
        }

        return (this.groupName.equals(selectRadioButton.groupName) && this.value.equals(selectRadioButton.value));
    }

    @Override
    public String toString() {
        return "Select Radio Button   " + this.groupName + "   " + this.value;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
