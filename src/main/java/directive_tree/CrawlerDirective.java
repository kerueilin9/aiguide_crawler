package directive_tree;

import util.HighLevelAction;

import java.util.List;

public class CrawlerDirective {

    private final String stateId;
    private final String dom;
    private final List<HighLevelAction> highLevelActions;

    public CrawlerDirective(String stateId, String dom, List<HighLevelAction> highLevelActions) {
        this.stateId = stateId;
        this.dom = dom;
        this.highLevelActions = highLevelActions;
    }

    public String getStateId() {
        return stateId;
    }

    public String getDom() {
        return dom;
    }

    public List<HighLevelAction> getHighLevelActions() {
        return highLevelActions;
    }
}
