package util;

import java.util.List;
import java.util.Map;

public class Config {
    public final JsonFileParser parser;
    public final String AUT_NAME;
    public final int AUT_PORT;
    public final String SERVER_IP;
    public final int SLEEP_TIME;
    public final List<Map<String, String>> AGENTS;
    public final int CRAWLER_DEPTH;
    public final String ROOT_URL;
    public final String CRAWLER;

    public Config(String configPath) {
        this.parser = new JsonFileParser(configPath);
        this.AUT_NAME = parser.getJsonFileValue("application");
        this.AUT_PORT = Integer.parseInt(parser.getJsonFileValue("application port"));
        this.SERVER_IP = parser.getJsonFileValue("server IP");
        this.AGENTS = parser.getJsonFileArrayValues("agents");
        this.SLEEP_TIME = Integer.parseInt(parser.getJsonFileValue("sleep time"));

        //Crawler Config
        this.CRAWLER = parser.getJsonFileValue("crawler config", "crawler");
        this.CRAWLER_DEPTH = Integer.parseInt(parser.getJsonFileValue("crawler config", "depth"));
        this.ROOT_URL = parser.getJsonFileValue("crawler config", "root url");
    }
}
