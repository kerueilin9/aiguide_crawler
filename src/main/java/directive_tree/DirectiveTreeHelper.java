package directive_tree;

import directive_tree.graph.GraphDrawer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningTask.LearningTask;
import util.Action;
import util.HighLevelAction;
import util.LogHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DirectiveTreeHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectiveTreeHelper.class);

    private final Directive DTRoot;
    private Queue<Directive> unprocessedLeaves;
    private Directive processingLeaf;

    public DirectiveTreeHelper(Directive dtRoot) {
        this.DTRoot = dtRoot;
        this.unprocessedLeaves = new LinkedList<>();
        this.unprocessedLeaves.offer(this.DTRoot);
    }

    public DirectiveTreeHelper() {
        this(new Directive(null, "",null, 0, 0, true));
    }

    public void addDirectives(List<LearningResult> results) {
        for(LearningResult result : results){
            InputPage page = DTRoot.findInputPageByStateID(result.getTaskID());
            if(result.isDone())
                page.setDone();
            else{
                Directive d = convertToDirective(result);
                unprocessedLeaves.offer(d);
                page.addDirective(d);
            }
        }
    }

    public void addInputPage(LearningTask task) {
        InputPage ip = convertToInputPage(task);
        processingLeaf.addInputPage(ip);
    }

    public List<CrawlerDirective> takeFirstUnprocessedCrawlerDirectives() {
        processingLeaf = unprocessedLeaves.poll();
        LOGGER.debug("The unprocessedLeaves size is {}", unprocessedLeaves.size());
        if(processingLeaf == null) return null;
        LogHelper.debug("Next processing Leaf is: " + processingLeaf.getID());
        return getCrawlerDirectives(processingLeaf);
    }

    public Boolean isTreeComplete() {
        return unprocessedLeaves.isEmpty();
    }

    private List<CrawlerDirective> getCrawlerDirectives(Directive d) {
        List<CrawlerDirective> crawlerDirectives = new ArrayList<>();
        Directive currentDirective = d;
        assert d != null: "Directive is null in getCrawlerDirectives method";
        while(!currentDirective.isDTRoot()){
            InputPage ip = currentDirective.getParent();
            crawlerDirectives.add(new CrawlerDirective(ip.getStateID(), ip.getDom(), currentDirective.getActionSequence()));
            crawlerDirectives.add(new CrawlerDirective(ip.getStateID(), ip.getDom(), ip.getActionSequence()));
            currentDirective = ip.getParent();
        }
        return crawlerDirectives;
    }

    private InputPage convertToInputPage(LearningTask task) {
        return new InputPage(processingLeaf, task.getStateID(), task.getTargetURL(), task.getDom(), task.getFormXPaths(), convertToHighLevelActionSequence(task.getActionSequence()));
    }

    private Directive convertToDirective(LearningResult result) {
        return new Directive(DTRoot.findInputPageByStateID(result.getTaskID()), result.getFormXPath(), result.getActionSequence(), result.getCoverageImproved(), result.getLearningTargetActionSequenceLength());
    }

    private List<HighLevelAction> convertToHighLevelActionSequence(List<List<Action>> actionSequence) {
        if(actionSequence == null) return null;
        List<HighLevelAction> highLevelActions = new LinkedList<>();
        for(List<Action> list: actionSequence) {
            highLevelActions.add(new HighLevelAction(list));
        }
        return highLevelActions;
    }

    public void writeDirectiveTree() {
        String file_name = "./directiveTreeGraphic/DT.txt";
        try {
            FileWriter DTWriter = new FileWriter(file_name);
            DTWriter.write("=========================DirectiveTree=========================" + '\n');
            String str = AllNodesToString(DTRoot);
            DTWriter.write(str);
            DTWriter.write("===============================================================" + '\n');
            DTWriter.close();
        }catch (IOException e){
            System.out.println("Write DT error!!!");
            e.printStackTrace();
        }
    }

    public void printDirectiveTree() {
        System.out.println("=========================DirectiveTree=========================");
        System.out.println(AllNodesToString(DTRoot));
        System.out.println("===============================================================");
    }

    public void drawDirectiveTree(){
        GraphDrawer graphDrawer = new GraphDrawer();
        graphDrawer.draw(this.DTRoot);
    }

    private String AllNodesToString(Directive directive) {
        String data = directive.toString() + "\n";
        for (InputPage ip : directive.getChild()) {
            data = data.concat(ip.toString() + "\n");
            for (Directive d : ip.getChild())
                data = data.concat(AllNodesToString(d));
        }
        return data;
    }
}
