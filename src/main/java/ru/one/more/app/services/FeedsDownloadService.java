package ru.one.more.app.services;

/**
 * Created by aboba on 29.01.17.
 */
public interface FeedsDownloadService {
    boolean downloadFeeds(String url, String rule);
}
