package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonFileParserTest {
    private JsonFileParser parser;

    @Before
    public void setUp() throws Exception {
        this.parser = new JsonFileParser("./src/test/configuration/configuration.json");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getJsonFileValueOneLayer() {
        String str = parser.getJsonFileValue("application");
        assertEquals(str, "timetimeoff-management");
    }

    @Test
    public void getJsonFileValueMultiLayer01() {
        String str = parser.getJsonFileValue("for testing", "test03", "test03-2");
        assertEquals(str, "hello 03-2");
    }

    @Test
    public void getJsonFileValueMultiLayer02() {
        String str = parser.getJsonFileValue("for testing", "test03", "test03-1", "test03-1-1", "test03-1-1-1");
        assertEquals(str, "hello 03-1-1-1");
    }

    @Test
    public void getJsonFileValues() {
        Map<String, String> data = parser.getJsonFileValues("for testing", "test02");
        assertNotNull(data.get("test02-2"));
        assertEquals(data.get("test02-2"), "hello 02-2");
        assertNotNull(data.get("test02-4"));
        assertEquals(data.get("test02-4"), "hello 02-4");
    }

    @Test
    public void getJsonFileArrayValuesOneLayer() {
        List<Map<String, String>> data = parser.getJsonFileArrayValues("agents");
        assertNotNull(data.get(0).get("ip"));
        assertEquals(data.get(0).get("ip"), "140.124.183.85");
        assertNotNull(data.get(1).get("javaPort"));
        assertEquals(data.get(1).get("javaPort"), "8888");
    }

    @Test
    public void getJsonFileArrayValuesMultiLayer01() {
        List<Map<String, String>> data = parser.getJsonFileArrayValues("for testing", "test agent");
        assertNotNull(data.get(0).get("name"));
        assertEquals(data.get(0).get("name"), "Tony");
        assertNotNull(data.get(1).get("level"));
        assertEquals(data.get(1).get("level"), "26");
        assertNotNull(data.get(2).get("play hours"));
        assertEquals(data.get(2).get("play hours"), "??hr");
    }

    @Test
    public void getJsonFileValuesAsArray() {
        Map<String, List<String>> map = parser.getJsonFileValuesAsArray("for testing", "branch");
        String[] actual_value = new String[]{"1", "1", "14", "14", "13", "0", "3", "10", "1", "12"};
        String[] actual_key = new String[]{"1", "2", "3", "4", "5"};
        int cnt_key = 0, cnt_value = 0;
        for(Map.Entry<String, List<String>> entry: map.entrySet()){
            assertEquals(entry.getKey(), actual_key[cnt_key++]);
            for(String str: entry.getValue()) {
                assertEquals(str, actual_value[cnt_value++]);
            }
        }
    }

    @Test
    public void getAllKeys() {
        String[] actual = {"test02-1", "test02-2", "test02-3", "test02-4"};
        int i = 0;
        for(String key: parser.getAllKeys("for testing", "test02")){
            assertEquals(key,actual[i++]);
        }
    }
}