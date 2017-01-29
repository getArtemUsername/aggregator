package ru.one.more.app.services.impl;

import org.apache.tapestry5.ioc.annotations.Inject;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.services.ReadFeedsService;
import ru.one.more.workers.DataAccessHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aboba on 29.01.17.
 */
public class ReadFeedsServiceImpl implements ReadFeedsService {

    @Override
    public List<Feed> getFeeds(int page) {
        return getFeeds(null, page);
    }

    @Override
    public List<Feed> getFeeds(String searchString, int page) {
        return DataAccessHelper.getInst().fetchFeeds(searchString, page);
    }

    @Override
    public boolean noMoreFeeds(String searchString, Integer page) {
        return DataAccessHelper.getInst().noMoreFeeds(searchString, page);
    }
}
