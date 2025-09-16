package directive_tree;

import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningTask.LearningTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.Action;
import util.HighLevelAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;

public class DirectiveTreeHelperTest {
    private DirectiveTreeHelper DT;

    @Before
    public void setUp() throws Exception {
        DT = new DirectiveTreeHelper();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isTreeCompleteIsTrue() {
        DT.takeFirstUnprocessedCrawlerDirectives();
        assertTrue(DT.isTreeComplete());
    }

    @Test
    public void isTreeCompleteIsFalse() {
        assertFalse(DT.isTreeComplete());
    }

    @Test
    public void takeFirstUnprocessedCrawlerDirectives() {
        testForGetCrawlerDirectives();
    }

    @Test
    public void convertToInputPage() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertToInputPage = DirectiveTreeHelper.class.getDeclaredMethod("convertToInputPage", LearningTask.class);
        convertToInputPage.setAccessible(true);
        LearningTask task = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "https://www.facebook.com/", "135478965456", "", null, null);
        InputPage ip;
        ip = (InputPage) convertToInputPage.invoke(DT, task);
        assertTrue(ip.compareStateID("135478965456"));
    }

    @Test
    public void convertToDirective() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method convertToDirective = DirectiveTreeHelper.class.getDeclaredMethod("convertToDirective", LearningResult.class);
        convertToDirective.setAccessible(true);
        List<Action> as = new ArrayList<>();
        List<HighLevelAction> highLevelActionList = new ArrayList<>();
        highLevelActionList.add(new HighLevelAction(as));
        as.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "1234"));
        as.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", null));
        as.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "hello"));
        LearningResult result = new LearningResult(highLevelActionList, "eehrt4h564rth464h", "", 0, 0, false);
        Directive d;
        d = (Directive) convertToDirective.invoke(DT, result);
        assertEquals(d.getActionSequence().get(0).getActionSequence().get(1).getXpath(), "//*[@id=\"home_birthdays\"]/div/div/div/div");
    }

    @Test
    public void directiveTreeHelperTest() {
        DirectiveTreeHelper DTH = constructDTHelper();
        DTH.printDirectiveTree();
        DTH.drawDirectiveTree();
        List crawlerDirectives = DTH.takeFirstUnprocessedCrawlerDirectives();
        assertNull(crawlerDirectives);
        assertTrue(DTH.isTreeComplete());
    }

    /**
     * Tree structure (Source: https://i.imgur.com/lYe2UNW.png)
     *                                                    DTRoot
     *                                 /                    |                 \
     *                           register            forgot-password         login
     *                    /                 \               |
     *        D1(Register success)    D2(Register fail)   D3(success)
     *              /         \
     *          settings     edit
     *           /   \         |
     *          D4   D5       D6
     */
    private DirectiveTreeHelper constructDTHelper() {
        DirectiveTreeHelper DTH = new DirectiveTreeHelper();
        LearningResult result1, result2, result3, result4, result5, result6;
        LearningTask task1, task2, task3, task1_1, task1_2;

        List<Action> as = new ArrayList<>();
        List<HighLevelAction> highLevelActionList = new ArrayList<>();
        as.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "1234"));
        as.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", null));
        as.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "hello"));
        highLevelActionList.add(new HighLevelAction(as));

        List<List<Action>> actionsList = new LinkedList<>();
        List<Action> actionList1 = new LinkedList<>();
        actionList1.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "6666"));
        actionList1.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", "66666"));
        actionList1.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "666666"));
        List<Action> actionList2 = new LinkedList<>();
        actionList2.add(new Action("/html/body/div[1]/div/div[1]/div[3]/div/div/div/div/div/div[2]/div/div[2]", "7777"));
        List<Action> actionList3 = new LinkedList<>();
        actionList3.add(new Action("/html/body/div[1]/div/div[1]/div[1]/div[90]/div[2]/div[2]/div[1]/div/div", "8888"));
        actionList3.add(new Action("/html/body/div[1]/div/div[1]/div[1]/div[3]/div/div/div[1]/div[1]/div/div/a/div[1]/div/div/div/img", "88888"));
        actionsList.add(actionList1);
        actionsList.add(actionList2);
        actionsList.add(actionList3);

        task1 = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "https://localhost/register", "register", "", null, null);
        task2 = new LearningTask(actionsList, new Integer[]{0, 300, 300, 0}, "https://localhost/forgot-password/", "forgot-password", "", null, null);
        task3 = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "https://localhost/login/", "login", "", null, null);
        task1_1 = new LearningTask(actionsList, new Integer[]{0, 300, 300, 0}, "http://localhost:3000/settings/general/", "settings", "", null, null);
        task1_2 = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "http://localhost:3000/users/edit/", "edit", "", null, null);

        result1 = new LearningResult(highLevelActionList, "register", "", 0, 0, false);
        result2 = new LearningResult(highLevelActionList, "register", "", 0, 0, false);
        result3 = new LearningResult(highLevelActionList, "forgot-password", "", 0, 0, false);
        result4 = new LearningResult(highLevelActionList, "settings", "", 0, 0, false);
        result5 = new LearningResult(highLevelActionList, "settings", "", 0, 0, false);
        result6 = new LearningResult(highLevelActionList, "edit", "", 0, 0, false);

        // processing directive: Home(Root page)
        DTH.takeFirstUnprocessedCrawlerDirectives();

        DTH.addInputPage(task1);
        DTH.addInputPage(task2);
        DTH.addInputPage(task3);

        List<LearningResult> results = new ArrayList<>();
        results.add(result1);
        results.add(result2);
        results.add(result3);
        // Set all InputPage done
        results.add(new LearningResult(null, "register", "", 0, 0, true));
        results.add(new LearningResult(null, "forgot-password", "", 0, 0, true));
        results.add(new LearningResult(null, "login", "", 0, 0, true));
        DTH.addDirectives(results);

        // processing directive: Register success
        DTH.takeFirstUnprocessedCrawlerDirectives();

        DTH.addInputPage(task1_1);
        DTH.addInputPage(task1_2);

        // processing directive: Register fail
        DTH.takeFirstUnprocessedCrawlerDirectives();

        results.clear();
        results.add(result4);
        results.add(result5);
        results.add(new LearningResult(null, "settings", "", 0, 0, true));
        results.add(result6);
        results.add(new LearningResult(null, "edit", "", 0, 0, true));
        DTH.addDirectives(results);

        //processing directive: D3
        DTH.takeFirstUnprocessedCrawlerDirectives();
        //processing directive: D4
        DTH.takeFirstUnprocessedCrawlerDirectives();
        //processing directive: D5
        DTH.takeFirstUnprocessedCrawlerDirectives();
        //processing directive: D6
        DTH.takeFirstUnprocessedCrawlerDirectives();

        return DTH;
    }

    private void testForGetCrawlerDirectives() {
        DirectiveTreeHelper DTH = new DirectiveTreeHelper();
        LearningResult result1, result2, result3;
        LearningTask task1, task1_1, task1_1_1;
        int i = 0;

        List<Action> as = new ArrayList<>();
        List<HighLevelAction> highLevelActionList = new ArrayList<>();
        highLevelActionList.add(new HighLevelAction(as));
        as.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "1234"));
        as.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", null));
        as.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "hello"));

        List<List<Action>> actionsList = new LinkedList<>();
        List<Action> actionList1 = new LinkedList<>();
        actionList1.add(new Action("//*[@id=\"u_fetchstream_7_1s\"]/div[2]/div[1]/div[2]/div[3]/div/div/div/div/a/div", "6666"));
        actionList1.add(new Action("//*[@id=\"home_birthdays\"]/div/div/div/div", "66666"));
        actionList1.add(new Action("//*[@id=\"js_t\"]/div/div/div[1]/div[1]/h1/a/span", "666666"));
        List<Action> actionList2 = new LinkedList<>();
        actionList2.add(new Action("/html/body/div[1]/div/div[1]/div[3]/div/div/div/div/div/div[2]/div/div[2]", "7777"));
        List<Action> actionList3 = new LinkedList<>();
        actionList3.add(new Action("/html/body/div[1]/div/div[1]/div[1]/div[90]/div[2]/div[2]/div[1]/div/div", "8888"));
        actionList3.add(new Action("/html/body/div[1]/div/div[1]/div[1]/div[3]/div/div/div[1]/div[1]/div/div/a/div[1]/div/div/div/img", "88888"));
        actionsList.add(actionList1);
        actionsList.add(actionList2);
        actionsList.add(actionList3);

        task1 = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "https://localhost:3000/register", "register", "", null, null);
        task1_1 = new LearningTask(actionsList, new Integer[]{0, 300, 300, 0}, "https://localhost:3000/forgot-password/", "forgot-password", "", null, null);
        task1_1_1 = new LearningTask(null, new Integer[]{0, 300, 300, 0}, "https://localhost:3000/login/", "login", "", null, null);

        result1 = new LearningResult(highLevelActionList, "register", "", 0, 0, false);
        result2 = new LearningResult(highLevelActionList, "forgot-password", "", 0, 0, false);
        result3 = new LearningResult(highLevelActionList, "login", "", 0, 0, false);

        DTH.takeFirstUnprocessedCrawlerDirectives();

        DTH.addInputPage(task1);

        List<LearningResult> results = new ArrayList<>();
        results.add(result1);
        results.add(new LearningResult(null, "register", "", 0, 0, true));
        DTH.addDirectives(results);

        DTH.takeFirstUnprocessedCrawlerDirectives();
        DTH.addInputPage(task1_1);

        results.clear();
        results.add(result2);
        results.add(new LearningResult(null, "forgot-password", "", 0, 0, true));
        DTH.addDirectives(results);

        String[] actual = new String[]{"forgot-password", "register"};
//        Map<String, List<HighLevelAction>> m;
//        m = DTH.takeFirstUnprocessedCrawlerDirectives();
//        for(String s : actual){
//            assertNotNull(m.get(s));
//        }
//        for(String id: m.keySet()){
//            assertEquals(id, actual[i++]);
//        }
        List<CrawlerDirective> crawlerDirectives;
        crawlerDirectives = DTH.takeFirstUnprocessedCrawlerDirectives();
        for(CrawlerDirective crawlerDirective: crawlerDirectives){
            assertEquals(crawlerDirective.getStateId(), actual[i++]);
        }

        DTH.addInputPage(task1_1_1);

        results.clear();
        results.add(result3);
        results.add(new LearningResult(null, "login", "", 0, 0, true));
        DTH.addDirectives(results);

        actual = new String[]{"login", "forgot-password", "register"};
//        m = DTH.takeFirstUnprocessedCrawlerDirectives();
//        for(String s : actual){
//            assertNotNull(m.get(s));
//        }
        i = 0;
//        for(String id: m.keySet()){
//            assertEquals(id, actual[i++]);
//        }
        crawlerDirectives = DTH.takeFirstUnprocessedCrawlerDirectives();
        for(CrawlerDirective crawlerDirective: crawlerDirectives){
            assertEquals(crawlerDirective.getStateId(), actual[i++]);
        }
        assertTrue(DTH.isTreeComplete());
    }
}