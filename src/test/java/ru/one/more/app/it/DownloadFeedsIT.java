package ru.one.more.app.it;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.XmlFeedsParser;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.workers.DataAccessHelper;
import ru.one.more.workers.FeedGrabber;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by SBT-Klevakin-AN on 30.01.2017.
 */
public class DownloadFeedsIT {

    @Test
    public void downloadFeedsTest() throws InterruptedException, ExecutionException {
        URL rssRule = DownloadFeedsIT.class.getClassLoader().getResource("rules/rss.rule");
        boolean successDownload = ParserRule.from(rssRule.getPath())
                .map(rule -> new FeedGrabber().grabFeeds("http://static.feed.rbc.ru/rbc/logical/footer/news.rss", rule))
                .orElse(false);
        assertTrue(successDownload);
        Collection<Feed> feeds = DataAccessHelper.getInst().fetchFeeds("", 0);
        assertFalse(feeds.isEmpty());
        //same rule upload
        successDownload = ParserRule.from(rssRule.getPath())
                .map(rule -> new FeedGrabber().grabFeeds("https://rg.ru/xml/index.xml", rule))
                .orElse(false);
        assertTrue(successDownload);
        List<SourceRule> srcRules = DataAccessHelper.getInst().fetchRules();
        assertThat(srcRules, hasSize(1));

        feeds = DataAccessHelper.getInst().fetchFeeds("", 0);
        assertFalse(srcRules.isEmpty());
        //refresh news
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<FeedSource> feedSources = DataAccessHelper.getInst().fetchFeedSources();
        assertThat(feedSources, hasSize(2));
        List<Future<Boolean>> tasks = new ArrayList<>();
        for (FeedSource feedSource : feedSources) {
            tasks.add(executorService.submit(() ->
                    new FeedGrabber().grabFeeds(feedSource.getSourceLink(), ParserRule.from(feedSource.getParseRule()))));
        }
        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.MINUTES);
        Collection<Feed> updatedFeeds = DataAccessHelper.getInst().fetchFeeds("", 0);
        assertThat(updatedFeeds, containsInAnyOrder(feeds.toArray()));
    }
}
