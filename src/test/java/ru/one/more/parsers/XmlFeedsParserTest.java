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
import java.net.URL;
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
        InputStream rbcXml = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/rbc-rss.xml");
        URL ruleUrl = XmlFeedsParserTest.class.getClassLoader().getResource("rules/rss.rule");
        Optional<ParserResult> feedSource = ParserRule.from(ruleUrl.getFile())
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(rbcXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("РБК - Все материалы"));
        assertThat(f.getDescription(), is("rss.rbc.ru"));
        assertThat(f.getLang(), isEmptyOrNullString());
        assertThat(f.getLink(), is("http://www.rbc.ru/"));
        rbcXml.close();
    }

    @Test
    public void shouldGrabRpcFeeds() throws IOException {
        InputStream rbcXml = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/rbc-rss.xml");
        URL ruleUrl = XmlFeedsParserTest.class.getClassLoader().getResource("rules/rss.rule");
        Optional<ParserResult> feedSource = ParserRule.from(ruleUrl.getFile())
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
        assertThat(feed.getLink(), is("http://www.rbc.ru/rbcfreenews/588704959a7947a05640857c"));
        rbcXml.close();
    }

    @Test
    public void shouldGrabGlunewsFeedSource() throws IOException {
        InputStream glunewsIS = XmlFeedsParserTest.class.getClassLoader().getResourceAsStream("in/rus-today-rss.xml");
        String glunewsXml = IOUtils.toString(glunewsIS);
        URL ruleUrl = XmlFeedsParserTest.class.getClassLoader().getResource("rules/rss._rt.rule");
        Optional<ParserResult> feedSource = ParserRule.from(ruleUrl.getFile())
                .flatMap(rule -> parser.withRule(rule))
                .flatMap(p -> p.parse(glunewsXml));

        assertThat(feedSource, OptionalMatchers.contains(anything()));

        FeedSource f = feedSource.get().getFeedSource();
        assertThat(f.getTitle(), is("RT на русском"));
        assertThat(f.getDescription(), is("Новости RT на русском языке"));
        assertThat(f.getLang(), is("ru"));
        assertThat(f.getLink(), is("https://russian.rt.com/rss"));
        glunewsIS.close();
    }
}
