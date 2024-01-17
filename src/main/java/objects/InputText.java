package objects;

public class InputText extends Expression{
    private String text;
    private int size;
    public String getType() {
        return "Input Text";
    }

    public InputText(String locator, String text) {
        super(locator);
        this.text = text;
    }

    public InputText(InputText inputText) {
        super(inputText.getLocator());
        this.text = inputText.getValue();
        this.size = inputText.getSize();
    }

    public String getValue() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof InputText it)) {
            return false;
        }

        return (this.getLocator().equals(it.getLocator()) && this.text.equals(it.getValue()));
    }

    @Override
    public String toString() {
        return "Input Text   " + this.getLocator() + "   " + this.text;
    }
}
