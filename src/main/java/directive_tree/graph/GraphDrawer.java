package directive_tree.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import directive_tree.Directive;
import directive_tree.InputPage;
import util.Action;
import util.FileHelper;
import util.HighLevelAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GraphDrawer {

    public JsonObject convertDirectiveToJson(Directive directive) {
        JsonObject directiveJson = new JsonObject();
        directiveJson.addProperty("Type","Directive");
        directiveJson.addProperty("id", directive.getID());
        directiveJson.addProperty("formXPath", directive.getFormXPath());
        directiveJson.addProperty("coverageImproved", String.valueOf(directive.getCoverageImproved()));
        directiveJson.addProperty("learningTargetActionSequenceLength", String.valueOf(directive.getLearningTargetActionSequenceLength()));
        directiveJson.add("ActionSequence", this.convertHighLevelActionSequenceToJsonArray(directive.getActionSequence()));

        JsonArray directiveChildren = new JsonArray();
        Iterator<InputPage> inputPageIterator = directive.getChild().iterator();
        while(inputPageIterator.hasNext()){
            directiveChildren.add(this.convertInputPageToJson(inputPageIterator.next()));
        }

        directiveJson.add("children", directiveChildren);

        return directiveJson;
    }

    public JsonObject convertInputPageToJson(InputPage inputPage) {
        JsonObject inputPageJson = new JsonObject();
        inputPageJson.addProperty("Type","InputPage");
        inputPageJson.addProperty("stateID", inputPage.getStateID());

        String targetURL = inputPage.getTargetURL();
//        inputPageJson.addProperty("targetURL",targetURL.substring(targetURL.indexOf("/", "https://".length()),targetURL.length()));
        inputPageJson.addProperty("targetURL", targetURL);
//        inputPageJson.addProperty("formXPath", inputPage.getFormXPath());


        JsonArray inputPageChildren = new JsonArray();
        Iterator<Directive> directiveIterator = inputPage.getChild().iterator();
        while(directiveIterator.hasNext()){
            inputPageChildren.add(this.convertDirectiveToJson(directiveIterator.next()));
        }

        inputPageJson.add("children", inputPageChildren);

        return inputPageJson;
    }

    private JsonArray convertHighLevelActionSequenceToJsonArray(List<HighLevelAction> highLevelActionSequence) {
        JsonArray actionSequenceJsonArray = new JsonArray();

        if (highLevelActionSequence == null){
            return actionSequenceJsonArray;

        }

        Iterator<HighLevelAction> highLevelActionIterator = highLevelActionSequence.iterator();
        while(highLevelActionIterator.hasNext()){
            JsonArray HighLevelActionJsonArray = this.convertHighLevelActionToJsonArray(highLevelActionIterator.next());
            actionSequenceJsonArray.add(HighLevelActionJsonArray);
        }
        return actionSequenceJsonArray;
    }

    private JsonArray convertHighLevelActionToJsonArray(HighLevelAction highLevelAction) {
        JsonArray highLevelActionJsonArray = new JsonArray();

        Iterator<Action> highLevelActionIterator = highLevelAction.getActionSequence().iterator();
        while(highLevelActionIterator.hasNext()){
            JsonObject actionJson = new JsonObject();
            Action action = highLevelActionIterator.next();
            actionJson.addProperty("xpath", action.getXpath());
            actionJson.addProperty("value", action.getValue());
            highLevelActionJsonArray.add(actionJson);
        }

        return highLevelActionJsonArray;
    }

    public void generateGraph(JsonObject directiveTreeJson, String graphSavePath, String graphLibraryPath) {
        FileHelper.copyFolder(graphLibraryPath, graphSavePath);
        Graph DTGraph = new Graph();
        DTGraph.setGraphPath(graphSavePath);
        DTGraph.setTreeStructure(directiveTreeJson);
        DTGraph.saveFile();
    }

    public void draw(Directive directiveRoot){
        JsonObject DTRootJson = this.convertDirectiveToJson(directiveRoot);
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());
        String graphSavePath = GraphConfig.GRAPH_PATH + "/" + timeStamp;
        generateGraph(DTRootJson, graphSavePath, GraphConfig.LIBRARY_PATH);
    }
}