package ru.one.more.app.services;

import ru.one.more.app.entities.Feed;

import java.util.List;

/**
 * Created by aboba on 29.01.17.
 */
public interface ReadFeedsService {
    List<Feed> getFeeds(int page);
    List<Feed> getFeeds(String searchString, int page);
    boolean noMoreFeeds(String searchString, Integer page);
}
