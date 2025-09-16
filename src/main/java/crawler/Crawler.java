package crawler;

import directive_tree.CrawlerDirective;
import usecase.learningPool.learningTask.LearningTask;
import util.Config;
import util.HighLevelAction;

import java.util.List;
import java.util.Map;

public interface Crawler {
    List<LearningTask> crawlingWithDirectives(Config config, List<CrawlerDirective> crawlerDirectives);
    void generateGraph();
}
