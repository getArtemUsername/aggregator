package ru.one.more.app.services;

import ru.one.more.app.entities.SourceRule;

/**
 * Created by aboba on 29.01.17.
 */
public interface FeedsDownloadService {
    boolean downloadFeeds(String url, String rule);
    boolean downloadFeeds(String url, SourceRule rule);
}
