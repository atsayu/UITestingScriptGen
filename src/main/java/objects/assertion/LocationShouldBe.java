package objects.assertion;

import objects.Expression;

public class LocationShouldBe implements Expression {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocationShouldBe(String url) {
        this.url = url;
    }

    public LocationShouldBe(LocationShouldBe locationShouldBe) {
        this.url = locationShouldBe.url;
    }

    @Override
    public boolean isExprEquals(Object obj) {
        return false;
    }

    @Override
    public String exprToString() {
        return "Location Should Be   " + url;
    }
}
