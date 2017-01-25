package ru.one.more.parsers.rule;

import ru.one.more.app.entities.Feed;
import ru.one.more.app.entities.FeedSource;

import java.util.List;

/**
 * Created by aboba on 24.01.17.
 */
public class ParserResult {
    final FeedSource feedSource;
    final List<Feed> feeds;

    public ParserResult(FeedSource feedSource, List<Feed> feeds) {
        this.feedSource = feedSource;
        this.feeds = feeds;
    }

    public FeedSource getFeedSource() {
        return feedSource;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }
}
