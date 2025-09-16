package directive_tree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Action;
import util.HighLevelAction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DirectiveTest {
    List<HighLevelAction> as;

    @Before
    public void setUp() throws Exception {
        List<Action> tmp;
        as = new ArrayList<>();
        tmp = new ArrayList<>();
        tmp.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "1234"));
        tmp.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", null));
        tmp.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "hello"));
        as.add(new HighLevelAction(tmp));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetActionSequenceIsNull() {
        Directive root;
        root = new Directive(null, "",  null,0, 0);
        assertNull(root.getActionSequence());
    }

    @Test
    public void testGetActionSequence() {
        Directive root;
        root = new Directive(null, as);
        assertNotNull(root.getActionSequence());
        assertEquals(root.getActionSequence().get(0).getActionSequence().size(), 3);
    }

    @Test
    public void testAddInputPage() {
        Directive root = new Directive(null, "", null,0, 0);
        InputPage ip = new InputPage(null, "", "", "", null, null);
        root.addInputPage(ip);
        assertEquals(root.getChild().size(), 1);
    }

    @Test
    public void testGetParent() {
        InputPage parent = new InputPage(null, "", "", "", null, null);
        Directive root = new Directive(parent, null);
        assertEquals(root.getParent(), parent);
    }

    @Test
    public void testFindChildByTaskFirstLayer() {
        Directive DTRoot = constructDT();
        InputPage ip = DTRoot.findInputPageByStateID("IP0");
        assertEquals(ip.getStateID(), "IP0");
    }

    @Test
    public void testFindChildByTaskSecondLayer() {
        Directive DTRoot = constructDT();
        InputPage ip = DTRoot.findInputPageByStateID("IP1-1");
        assertEquals(ip.getStateID(), "IP1-1");
    }

    /**
     * Tree structure (The parenthesized string represents the stateID)
     *                  DTRoot
     *              /           \
     *           IP(IP0)        IP(IP1)
     *          /      \          \
     *       D1        D2         D3
     *       /      /      \        \
     * IP(IP0-1) IP(IP0-2) IP(IP0-3) IP(IP1-1)
     */
    private Directive constructDT() {
        Directive DTRoot = new Directive(null, null);
        Directive d1, d2, d3;
        InputPage ip0, ip1;

        ip0 = new InputPage(DTRoot, "IP0", "", "", null, null);
        ip1 = new InputPage(DTRoot, "IP1", "", "", null, null);

        d1 = new Directive(ip0, null);
        d2 = new Directive(ip0, null);
        d3 = new Directive(ip1, null);

        d1.addInputPage(new InputPage(d1, "IP0-1", "", "", null, null));
        d2.addInputPage(new InputPage(d2, "IP0-2", "", "", null, null));
        d2.addInputPage(new InputPage(d2, "IP0-3", "", "", null, null));
        d3.addInputPage(new InputPage(d3, "IP1-1", "", "", null, null));

        ip0.addDirective(d1);
        ip0.addDirective(d2);
        ip1.addDirective(d3);

        DTRoot.addInputPage(ip0);
        DTRoot.addInputPage(ip1);

        return DTRoot;
    }
}