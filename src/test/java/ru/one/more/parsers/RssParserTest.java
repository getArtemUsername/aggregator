package ru.one.more.parsers;


import co.unruly.matchers.OptionalMatchers;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import ru.one.more.model.Feed;
import ru.one.more.model.FeedSource;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 24.01.17.
 */

public class RssParserTest {
    //SUT
    RssParser parser = new RssParser();

    @Test
    public void shouldGrabRpcFeedSource() throws IOException {
        InputStream rbcIS = RssParserTest.class.getClassLoader().getResourceAsStream("rbc-rss.xml");
        String rbcXml = IOUtils.toString(rbcIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/rbc.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(rbcXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("РБК - Все материалы"));
        assertThat(f.getDescription(), is("rss.rbc.ru"));
        assertThat(f.getSourceType(), is(FeedSource.SourceType.RSS));
        assertThat(f.getLang(), isEmptyOrNullString());
        assertThat(f.getUrl(), is("http://www.rbc.ru/"));
        rbcIS.close();
    }

    @Test
    public void shouldGrabRpcFeeds() throws IOException {
        InputStream rbcIS = RssParserTest.class.getClassLoader().getResourceAsStream("rbc-rss.xml");
        String rbcXml = IOUtils.toString(rbcIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/rbc.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(rbcXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        List<Feed> f = feedSource.get().getFeeds();
        assertThat(f, not(empty()));

        Feed feed = f.get(0);
        assertThat(feed.getTitle(), is("Volkswagen обязали выплатить $1,2 млрд дилерам за «дизельгейт»"));
        assertThat(feed.getShortContent(), isEmptyOrNullString());
        assertThat(feed.getSource(), is(feedSource.get().getFeedSource()));
        assertThat(feed.getSourceLink(), is("http://www.rbc.ru/rbcfreenews/588704959a7947a05640857c"));
        rbcIS.close();
    }

    @Test
    public void shouldGrabGlunewsFeedSource() throws IOException {
        InputStream glunewsIS = RssParserTest.class.getClassLoader().getResourceAsStream("glunews-rss.xml");
        String glunewsXml = IOUtils.toString(glunewsIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/glunews.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(glunewsXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("GLUnews.ru / Интересные и Забавные Новости"));
        assertThat(f.getDescription(), is("GLUnews.ru / Интересные и Забавные Новости"));
        assertThat(f.getSourceType(), is(FeedSource.SourceType.RSS));
        assertThat(f.getLang(), is("ru-ru"));
        assertThat(f.getUrl(), is("http://www.glunews.ru"));
        glunewsIS.close();
    }
}
