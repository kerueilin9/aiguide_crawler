package usecase.learningPool.learningTask;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningTask {
    private final List<List<util.Action>> actionSequence;
    private final Integer[] coverage;
    private final String targetURL;
    private final String stateID;
    private final String dom;
    private final List<String> formXPaths;
    private final Map<String, String> learningConfig;

    public LearningTask(List<List<util.Action>> actionSequence, String stateID, String dom, List<String> formXPaths) {
        this(actionSequence, new Integer[0], "", stateID, dom, formXPaths, new HashMap<>());
    }

    public LearningTask(List<List<util.Action>> actionSequence, Integer[] coverage, String targetURL, String stateID, String dom, List<String> formXpaths, Map<String, String> learningConfig) {
        this.actionSequence = actionSequence;
        this.coverage = coverage;
        this.targetURL = targetURL;
        this.stateID = stateID;
        this.dom = dom;
        this.formXPaths = formXpaths;
        this.learningConfig = learningConfig;
    }

    public List<List<util.Action>> getActionSequence() {
        return actionSequence;
    }

    public Integer[] getCoverage() {
        return coverage;
    }

    public String getTargetURL() {
        return this.targetURL;
    }

    public String getStateID() {
        return stateID;
    }

    public String getDom() {
        return dom;
    }

    public List<String> getFormXPaths() {
        return formXPaths;
    }

    public Map<String, String> getLearningConfig() {
        return learningConfig;
    }

    @Override
    public String toString() {
        return "LearningTask{" +
                "actionSequence=" + actionSequence +
                ", coverage=" + Arrays.toString(coverage) +
                ", targetURL='" + targetURL + '\'' +
                ", stateID='" + stateID + '\'' +
                ", formXPaths='" + formXPaths + '\'' +
                ", learningConfig=" + learningConfig +
                '}';
    }
}
