package util;

import java.util.Objects;

public class Action {
    private final String xpath;
    private final String value;

    public Action(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }

    public Action(String xpath) {
        this.xpath = xpath;
        this.value = null;
    }

    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(xpath, action.xpath) && Objects.equals(value, action.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xpath, value);
    }

    @Override
    public String toString() {
        return "Action{" +
                "xpath='" + xpath + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
