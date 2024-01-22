package objects.assertion;

import objects.Expression;

import javax.xml.stream.Location;

public class LocationAssertion implements Expression {
    private String url;
    public String getType() {
        return "Location Assertion";
    }
    public LocationAssertion(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocationAssertion(LocationAssertion locationAssertion) {
        this.url = locationAssertion.getUrl();
    }

    public boolean isExprEquals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof LocationAssertion loc)) return false;
        return this.url.equals(loc.getUrl());
    }

    public String exprToString() {
        return "Location should be\t" + this.url;
    }
    @Override
    public int compareTo(Expression o) {
        if (o instanceof LocationAssertion) {
            return this.url.compareTo(((LocationAssertion) o).getUrl());
        }
        return this.getType().compareTo(o.getType());
    }

}
