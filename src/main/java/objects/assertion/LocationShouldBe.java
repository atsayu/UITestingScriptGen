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
    public String toString() {
        return "Location Should Be   " + url;
    }
}
