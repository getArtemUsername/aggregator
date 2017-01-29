package ru.one.more.app.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import ru.one.more.app.entities.Feed;
import ru.one.more.app.services.FeedsDownloadService;
import ru.one.more.app.services.ReadFeedsService;
import ru.one.more.app.services.RefreshService;
import ru.one.more.app.services.impl.RefreshServiceImpl;

import java.util.List;

/**
 * Created by aboba on 26.01.17.
 */
public class Index {
    @Property
    private List<Feed> feedList;

    @Property
    @Persist
    private Integer page;

    @Property
    @Persist
    private String searchString;

    @Inject
    private ReadFeedsService readFeedsService;

    @Inject
    private RefreshService refreshService;


    public boolean getIsNotFeedsEnd() {
        return page > 0;
    }

    public boolean getIsNotFeedsStart() {
        return !readFeedsService.noMoreFeeds(searchString, page);
    }

    public void onRefreshFeeds() {
        if (refreshService.refreshFeeds()) {
            feedList = readFeedsService.getFeeds(0);
        }
    }

    public void onPrev() {
        page++;
    }

    public void onNext() {
        page--;
    }

    public void onActivate() {
        if (searchString == null || page == null) reset();
        feedList = readFeedsService.getFeeds(searchString, page);
    }

    @OnEvent(value = EventConstants.SUCCESS)
    public void onSearch() {
        page = 0;
        feedList = readFeedsService.getFeeds(searchString, page);
    }

    private void reset() {
        page = 0;
        this.searchString = "";
    }

}
