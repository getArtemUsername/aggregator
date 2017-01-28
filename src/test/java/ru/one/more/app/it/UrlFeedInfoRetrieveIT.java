package ru.one.more.app.it;

import co.unruly.matchers.OptionalMatchers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.parsers.XmlFeedsParser;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by aboba on 28.01.17.
 */
public class UrlFeedInfoRetrieveIT {
    Logger logger = LoggerFactory.getLogger(UrlFeedInfoRetrieveIT.class);
    @Test
    public void fetchRssXml() throws UnirestException {
        HttpResponse<String> response  = Unirest.get("http://static.feed.rbc.ru/rbc/logical/footer/news.rss")
                .asString();
        XmlFeedsParser parser = new XmlFeedsParser();
        Optional<ParserResult> result = ParserRule.from("rules/rss.rule")
                .flatMap(parser::withRule)
                .flatMap(p -> p.parse(response.getBody()));
        logger.info(response.getBody().substring(0, 100));
        assertThat(result, OptionalMatchers.contains(any(ParserResult.class)));
        FeedSource feedSource = result.get().getFeedSource();
        logger.info("{}", feedSource);
        List<Feed> feeds = result.get().getFeeds();
        assertThat(feedSource.getTitle(), is("РБК - Все материалы"));
        assertThat(feeds, hasSize(greaterThan(0)));

    }

}
