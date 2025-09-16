package directive_tree.graph;

import com.google.gson.JsonObject;
import util.FileHelper;

import java.io.*;

public class Graph {
    private JsonObject directiveTreeJson;
    private String graphPath;

    public JsonObject getDirectiveTreeJson() {
        return directiveTreeJson;
    }

    public void setTreeStructure(JsonObject directiveTreeJson) {
        this.directiveTreeJson = directiveTreeJson;
    }

    public void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
    }

    public void saveFile() {
        String dataContent = "var data = " + directiveTreeJson.toString();
        FileHelper.createFile(graphPath + "/data.js", dataContent);
    }

}
