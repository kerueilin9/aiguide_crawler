package usecase.learningPool.action.dto;

public class ActionDTO {
    private final String xpath;
    private final String value;

    public ActionDTO(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }

    public ActionDTO(String xpath) {
        this(xpath, "");
    }

    public String getXpath() {
        return xpath;
    }

    public String getValue() {
        return value;
    }
}
