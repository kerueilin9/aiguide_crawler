package directive_tree;

import util.Action;
import util.HighLevelAction;

import java.util.List;
import java.util.ArrayList;

public class Directive {
    private final List<InputPage> inputPageList;
    private final InputPage parentInputPage;
    private String formXPath;
    private final List<HighLevelAction> actionSequence;

    private final int learningTargetActionSequenceLength;

    private final int coverageImproved;
    private final String id;

    private final boolean isRoot;

    public Directive(InputPage parentInputPage, List<HighLevelAction> actionSequence) {
        this.parentInputPage = parentInputPage;
        this.actionSequence = actionSequence;
        this.coverageImproved = 0;
        this.learningTargetActionSequenceLength = 0;
        this.inputPageList = new ArrayList<>();
        this.id = this.getClass().getName() + "@" + Integer.toHexString(hashCode());
        this.isRoot = false;
    }

    public Directive(InputPage parentInputPage, String formXPath, List<HighLevelAction> actionSequence, int coverageImproved, int learningTargetActionSequenceLength) {
        this(parentInputPage, formXPath, actionSequence, coverageImproved, learningTargetActionSequenceLength, false);
    }

    public Directive(InputPage parentInputPage, String formXPath , List<HighLevelAction> actionSequence, int coverageImproved, int learningTargetActionSequenceLength, boolean isRoot) {
        this.parentInputPage = parentInputPage;
        this.formXPath = formXPath;
        this.actionSequence = actionSequence;
        this.coverageImproved = coverageImproved;
        this.learningTargetActionSequenceLength = learningTargetActionSequenceLength;
        this.inputPageList = new ArrayList<>();
        this.id = this.getClass().getName() + "@" + Integer.toHexString(hashCode());
        this.isRoot = isRoot;
    }

    public List<HighLevelAction> getActionSequence() {
        return actionSequence;
    }

    public Boolean isDTRoot() {
        return this.isRoot;
    }

    public void addInputPage(InputPage ip) {
//        if (ip.getParent().getParent() != null) {
//            ip.getParent().getParent().setDone();
//        }
        inputPageList.add(ip);
    }

    public InputPage getParent() {
        return parentInputPage;
    }

    public String getID() {
        return id;
    }

    public String getFormXPath() {
        return formXPath;
    }

    public int getCoverageImproved() {
        return coverageImproved;
    }

    public int getLearningTargetActionSequenceLength() {
        return learningTargetActionSequenceLength;
    }


    public List<InputPage> getChild() {
        return inputPageList;
    }

    public InputPage findInputPageByStateID(String stateID) {
        for (InputPage ip : inputPageList) {
            if (ip.compareStateID(stateID))
                return ip;
            for (Directive d : ip.getChild()){
                InputPage page = d.findInputPageByStateID(stateID);
                if (page != null) return page;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if(parentInputPage == null) return "+++++directive+++++\nThis is root directive" + "\nMy id is: " + id + "\n";
        return "+++++directive+++++\nParent stateID: " + parentInputPage.getStateID() + "\nMy id is: " + id + "\n" + printActionSequence() + "\n";
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
