package objects2;

public class SelectCheckbox extends Expression{

    public SelectCheckbox(String locator) {
        super(locator);
    }

    public SelectCheckbox(SelectCheckbox clickElement) {
        super(clickElement.getLocator());
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
