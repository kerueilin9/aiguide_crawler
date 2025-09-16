package learning_data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import usecase.learningPool.learningResult.LearningResult;
import usecase.learningPool.learningTask.LearningTask;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class LearningPoolTest {
    private LearningPool pool;

    @Before
    public void setUp() throws Exception {
        this.pool = new LearningPool();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addTask() {
        pool.addTask(new LearningTask(new ArrayList<>(), new Integer[]{1, 0, 1, 0}, "http://localhost:3000", String.valueOf("uui34yush2hd89heri".hashCode()), "", null, new TreeMap<>()));
        assertEquals(1, pool.getTaskSize());
    }

    @Test
    public void addResult() {
        pool.addResult(new LearningResult(new ArrayList<>() , "1234", "", 0, 0, false));
        assertEquals(1, pool.getResultSize());
    }

    @Test
    public void takeTaskHasNothing() {
        LearningTask task = pool.takeTask();
        assertNull(task);
    }

    @Test
    public void takeTaskHasOneTask() {
        pool.addTask(new LearningTask(new ArrayList<>(), new Integer[]{1, 0, 1, 0}, "http://localhost:3000", String.valueOf("uui34yush2hd89heri".hashCode()), "", null, new TreeMap<String, String>()));
        LearningTask task = pool.takeTask();
        assertNotNull(task);
        assertEquals(pool.getTaskSize(), 0);
    }

    @Test
    public void takeResultHasNothing() {
        LearningTask result = pool.takeTask();
        assertNull(result);
    }

    @Test
    public void takeResultHasOneResult() {
        pool.addResult(new LearningResult(new ArrayList<>(), "1234", "",0, 0, false));
        LearningResult result = pool.takeResult();
        assertNotNull(result);
        assertEquals(pool.getResultSize(), 0);
    }
}