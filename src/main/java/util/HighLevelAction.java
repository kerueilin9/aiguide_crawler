package util;

import java.util.List;

public class HighLevelAction {
    private List<Action> actions;

    public HighLevelAction(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActionSequence() {
        return actions;
    }
}
