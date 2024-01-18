package objects2;

public class Expression implements Comparable<Expression>{
    private String locator;
    private String locatorValue;
    public String getType() {
        return "Click Element";
    }
    public Expression(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    @Override
    public int compareTo(Expression o) {
        return this.locator.compareTo(o.getLocator());
    }
}
