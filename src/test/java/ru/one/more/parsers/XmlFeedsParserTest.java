package ru.one.more.parsers;


import co.unruly.matchers.OptionalMatchers;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by aboba on 24.01.17.
 */

public class XmlFeedsParserTest {
    //SUT
    XmlFeedsParser parser = new XmlFeedsParser();

    @Test
    public void shouldGrabRpcFeedSource() throws IOException {
        InputStream rbcIS = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/rbc-rss.xml");
        String rbcXml = IOUtils.toString(rbcIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/rss.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(rbcXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("РБК - Все материалы"));
        assertThat(f.getDescription(), is("rss.rbc.ru"));
        assertThat(f.getLang(), isEmptyOrNullString());
        assertThat(f.getUrl(), is("http://www.rbc.ru/"));
        rbcIS.close();
    }

    @Test
    public void shouldGrabRpcFeeds() throws IOException {
        InputStream rbcIS = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/rbc-rss.xml");
        String rbcXml = IOUtils.toString(rbcIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/rss.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(rbcXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        List<Feed> f = feedSource.get().getFeeds();
        assertThat(f, not(empty()));

        Feed feed = f.get(0);
        assertThat(feed.getTitle(), is("Volkswagen обязали выплатить $1,2 млрд дилерам за «дизельгейт»"));
        assertThat(feed.getShortContent(), isEmptyOrNullString());
        assertThat(feed.getSource(), is(feedSource.get().getFeedSource()));
        assertThat(new SimpleDateFormat("dd.MM.yyyy").format(feed.getPostDate()), is("24.01.2017"));
        assertThat(feed.getSourceLink(), is("http://www.rbc.ru/rbcfreenews/588704959a7947a05640857c"));
        rbcIS.close();
    }

    @Test
    public void shouldGrabGlunewsFeedSource() throws IOException {
        InputStream glunewsIS = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/glunews-rss.xml");
        String glunewsXml = IOUtils.toString(glunewsIS);
        Optional<ParserResult> feedSource = ParserRule.from("rules/rss.rule")
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(glunewsXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("GLUnews.ru / Интересные и Забавные Новости"));
        assertThat(f.getDescription(), is("GLUnews.ru / Интересные и Забавные Новости"));
        assertThat(f.getLang(), is("ru-ru"));
        assertThat(f.getUrl(), is("http://www.glunews.ru"));
        glunewsIS.close();
    }
}
