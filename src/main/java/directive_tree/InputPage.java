package directive_tree;

import util.Action;
import util.HighLevelAction;

import java.util.ArrayList;
import java.util.List;

public class InputPage {
    private final Directive parentDirective;
    private final String stateID;
    private final String targetURL;
    private final String dom;
    private final List<String> formXPaths;
    private final List<HighLevelAction> actionSequence;
    private final List<Directive> directiveList;
    private Boolean isDone;

    public InputPage(Directive parentDirective, String stateID, String targetURL, String dom, List<String> formXPaths, List<HighLevelAction> actionSequence) {
        this.parentDirective = parentDirective;
        this.stateID = stateID;
        this.targetURL = targetURL;
        this.dom = dom;
        this.formXPaths = formXPaths;
        this.actionSequence = actionSequence;
        this.directiveList = new ArrayList<>();
        this.isDone = false;
    }

    public void addDirective(Directive directive) { directiveList.add(directive); }

    public Directive getParent() {
        return parentDirective;
    }

    public List<Directive> getChild() {
        return directiveList;
    }

    public Boolean isDone() {
        return isDone;
    }

    public void setDone() {
        isDone = true;
    }

    public String getStateID() {
        return stateID;
    }

    public String getTargetURL() { return targetURL; }

    public String getDom() {
        return dom;
    }

    public List<String> getFormXPaths() {
        return formXPaths;
    }

    public Boolean compareStateID(String stateID) {
        return this.stateID.equals(stateID);
    }

    public List<HighLevelAction> getActionSequence() {return this.actionSequence; }

    @Override
    public String toString() {
        return "+++++InputPage+++++\nMy stateID is: " + stateID + "\nMy targetURL is: " + targetURL + "\nParent id is: " + parentDirective.getID() + "\n" + printActionSequence() + "\n";
    }

    private String printActionSequence() {
        if(actionSequence == null) return "";
        StringBuilder str;
        str = new StringBuilder();
        for(HighLevelAction ha : actionSequence){
            str.append("[");
            for(Action a : ha.getActionSequence()){
                String tmp = "('" + a.getXpath() + "', '" + a.getValue() + "')";
                str.append(tmp);
            }
            str.append("], ");
        }
        str.append("]");
        return str.toString();
    }
}
