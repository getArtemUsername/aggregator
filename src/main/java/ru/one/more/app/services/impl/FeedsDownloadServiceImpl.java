package ru.one.more.app.services.impl;

import ru.one.more.app.entities.SourceRule;
import ru.one.more.app.services.FeedsDownloadService;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.workers.FeedGrabber;

/**
 * Created by aboba on 29.01.17.
 */
public class FeedsDownloadServiceImpl implements FeedsDownloadService {
    @Override
    public boolean downloadFeeds(String url, String ruleFilePath) {
        FeedGrabber feedGrabber = new FeedGrabber();
        return ParserRule.from(ruleFilePath).map(rule-> feedGrabber.grabFeeds(url, rule))
                .orElse(false);
    }

    @Override
    public boolean downloadFeeds(String url, SourceRule rule) {
        FeedGrabber feedGrabber = new FeedGrabber();
        ParserRule parserRule = ParserRule.from(rule);
        return feedGrabber.grabFeeds(url, parserRule);
    }
}
