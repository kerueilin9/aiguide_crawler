package directive_tree.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import directive_tree.Directive;
import directive_tree.InputPage;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class GraphDrawerTest {

    private GraphDrawer graphDrawer;

    @Before
    public void setUp() throws Exception {
        this.graphDrawer = new GraphDrawer();
    }

    @Test
    public void testDirectiveConvertToJson() {
        Directive directive = new Directive(null, null);
        JsonObject directiveJson = graphDrawer.convertDirectiveToJson(directive);

        assertEquals (directive.getID(), directiveJson.get("id").getAsString());
        assertEquals ("[]", directiveJson.get("children").toString());
        assertEquals("Directive", directiveJson.get("Type").getAsString());
    }

    @Test
    public void testInputPageConvertToJson() {
        InputPage inputPage = new InputPage(null, "test", "https://127.0.0.1/register", "", null, null);
        JsonObject inputPageJson = graphDrawer.convertInputPageToJson(inputPage);

        assertEquals("test", inputPageJson.get("stateID").getAsString());
        assertEquals("https://127.0.0.1/register", inputPageJson.get("targetURL").getAsString());
        assertEquals ("[]", inputPageJson.get("children").toString());
        assertEquals("InputPage",inputPageJson.get("Type").getAsString());
    }

    @Test
    public void testDirectiveHasOneChildren(){
        Directive directive = new Directive(null, null);
        InputPage inputPage = new InputPage(null, "test", "/main", "", null, null);
        directive.addInputPage(inputPage);

        JsonObject directiveJson = graphDrawer.convertDirectiveToJson(directive);
        assertEquals(1, directiveJson.get("children").getAsJsonArray().size());

        JsonObject directiveFirstChildren = directiveJson.get("children").getAsJsonArray().get(0).getAsJsonObject();
        assertEquals("test", directiveFirstChildren.get("stateID").getAsString());
    }

    @Test
    public void testDirectiveHasTowChildren(){
        Directive directive = new Directive(null, null);
        InputPage inputPage1 = new InputPage(directive, "test1", "https://127.0.0.1/main1", "", null, null);
        InputPage inputPage2 = new InputPage(directive, "test2", "https://127.0.0.1/main2", "", null, null);
        directive.addInputPage(inputPage1);
        directive.addInputPage(inputPage2);

        JsonObject directiveJson = graphDrawer.convertDirectiveToJson(directive);
        assertEquals(2, directiveJson.get("children").getAsJsonArray().size());

        JsonObject directiveFirstChildren = directiveJson.get("children").getAsJsonArray().get(0).getAsJsonObject();
        JsonObject directiveSecondChildren = directiveJson.get("children").getAsJsonArray().get(1).getAsJsonObject();
        assertEquals("test1", directiveFirstChildren.get("stateID").getAsString());
        assertEquals("test2", directiveSecondChildren.get("stateID").getAsString());
    }

    @Test
    public void testInputPageHasOneChildren(){
        InputPage inputPage = new InputPage(null, "test", "https://127.0.0.1/main33", "", null, null);
        Directive directive = new Directive(inputPage, null);
        inputPage.addDirective(directive);

        JsonObject inputPageJson = graphDrawer.convertInputPageToJson(inputPage);
        JsonArray inputPageChild = inputPageJson.get("children").getAsJsonArray();
        assertEquals(1, inputPageChild.size());

        JsonObject inputPageFirstChilder = inputPageChild.get(0).getAsJsonObject();
        assertEquals(directive.getID(), inputPageFirstChilder.get("id").getAsString());
    }

    @Test
    public void testInputPageHasTwoChildrenHas(){
        InputPage inputPage = new InputPage(null, "test", "main", "", null, null);
        Directive directive1 = new Directive(inputPage, null);
        Directive directive2 = new Directive(inputPage, null);
        inputPage.addDirective(directive1);
        inputPage.addDirective(directive2);

        JsonObject inputPageJson = graphDrawer.convertInputPageToJson(inputPage);
        JsonArray inputPageChild = inputPageJson.get("children").getAsJsonArray();
        assertEquals(2, inputPageChild.size());

        JsonObject inputPageFirstChilder = inputPageChild.get(0).getAsJsonObject();
        JsonObject inputPagesecondChilder = inputPageChild.get(1).getAsJsonObject();
        assertEquals(directive1.getID(), inputPageFirstChilder.get("id").getAsString());
        assertEquals(directive2.getID(), inputPagesecondChilder.get("id").getAsString());
    }

    @Test
    public void testChilderhasChilder(){
        Directive DTRoot = new Directive(null,null);
        InputPage inputPage1 = new InputPage(DTRoot, "register", "https://127.0.0.1/register", "", null, null);
        InputPage inputPage2 = new InputPage(DTRoot, "\"forgot-password\"", "https://127.0.0.1/forgot-password/", "", null, null);
        InputPage inputPage3 = new InputPage(DTRoot, "login","https://127.0.0.1/login/", "", null, null);
        Directive directive1 = new Directive(inputPage1,null);
        Directive directive2 = new Directive(inputPage1, null);

        DTRoot.addInputPage(inputPage1);
        DTRoot.addInputPage(inputPage2);
        DTRoot.addInputPage(inputPage3);

        inputPage1.addDirective(directive1);
        inputPage1.addDirective(directive2);

        JsonObject directiveTreeJson = graphDrawer.convertDirectiveToJson(DTRoot);

        JsonArray DTRootChildernJson = directiveTreeJson.get("children").getAsJsonArray();
        assertEquals(3, DTRootChildernJson.size());

        JsonArray inputPage1ChildernJson = DTRootChildernJson.get(0).getAsJsonObject().get("children").getAsJsonArray();
        assertEquals(2,inputPage1ChildernJson.size());
    }

    @Test
    public void testGenerateGraph(){
        Directive DTRoot = new Directive(null,null);
        InputPage inputPage1 = new InputPage(DTRoot, "register", "https://127.0.0.1/register", "", null, null);
        InputPage inputPage2 = new InputPage(DTRoot, "forgot-password", "https://127.0.0.1/forgot-password/", "", null, null);
        InputPage inputPage3 = new InputPage(DTRoot, "login","https://127.0.0.1/login/", "", null, null);
        Directive directive1 = new Directive(inputPage1,null);
        Directive directive2 = new Directive(inputPage1, null);

        DTRoot.addInputPage(inputPage1);
        DTRoot.addInputPage(inputPage2);
        DTRoot.addInputPage(inputPage3);

        inputPage1.addDirective(directive1);
        inputPage1.addDirective(directive2);

        JsonObject DTRootJson = graphDrawer.convertDirectiveToJson(DTRoot);
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());
        String graphSavePath = GraphConfig.GRAPH_PATH + "/" + timeStamp;
        System.out.println(DTRootJson.toString());
        graphDrawer.generateGraph(DTRootJson, graphSavePath, GraphConfig.LIBRARY_PATH);
        File directiveTreeFile = new File(graphSavePath);
        assertEquals(true, directiveTreeFile.exists());
    }
}