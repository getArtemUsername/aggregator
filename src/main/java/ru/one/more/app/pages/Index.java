package ru.one.more.app.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.services.FeedsService;
import ru.one.more.app.services.RefreshService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by aboba on 26.01.17.
 */
public class Index {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private List<Feed> feedList;

    @Property
    private Integer tripleNum;

    @Property
    private Feed feed;

    @Property
    @Persist
    private Integer page;

    @Property
    @Persist
    private String searchString;

    @Inject
    private FeedsService feedsService;

    @Inject
    private RefreshService refreshService;

    public boolean getIsNotFeedsEnd() {
        return page > 0;
    }

    public boolean getIsNotFeedsStart() {
        return !feedsService.noMoreFeeds(searchString, page);
    }

    public void onGoHome() {
        reset();
    }

    public void onRefreshFeeds() {
        if (refreshService.refreshFeeds()) {
            reset();
            feedList = feedsService.getFeeds(0);
        }
    }

    public int getTriplesCount() {
        return feedList != null && feedList.size() > 0
                ? (feedList.size() / 3) + (feedList.size() % 3) - 1
                : 0;
    }

    public List<Feed> feeds(int tripletNum) {
        List<Feed> res = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            int idx =  (tripletNum * 3) + i;
            if (idx < feedList.size()) res.add(feedList.get(idx));
        }
        return res;
    }

    public String dateToString(Date d) {
        return DATE_FORMAT.format(d);
    }

    public void onPrev() {
        page++;
    }

    public void onNext() {
        page--;
    }

    public void onActivate() {
        if (searchString == null || page == null) reset();
        feedList = feedsService.getFeeds(searchString, page);
    }

    @OnEvent(value = EventConstants.SUCCESS)
    public void onSearch() {
        page = 0;
        feedList = feedsService.getFeeds(searchString, page);
    }

    @PageReset
    private void reset() {
        page = 0;
        searchString = "";
    }

}
