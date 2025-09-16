package directive_tree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputPageTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addDirective() {
        InputPage ip = new InputPage(null, "", "", "", null, null);
        ip.addDirective(new Directive(null, null));
        ip.addDirective(new Directive(null, null));
        assertEquals(ip.getChild().size(), 2);
    }

    @Test
    public void getParent() {
        Directive d = new Directive(null, null);
        InputPage ip = new InputPage(d, null, "", "", null, null);
        assertEquals(ip.getParent(), d);
    }

    @Test
    public void getCompareStateIDIsTrue() {
        InputPage ip = new InputPage(null, "1234", "", "", null, null);
        assertEquals(ip.compareStateID("1234"), true);
    }

    @Test
    public void getCompareStateIDIsFalse() {
        InputPage ip = new InputPage(null, "1234", "", "", null, null);
        assertEquals(ip.compareStateID("123"), false);
    }
}