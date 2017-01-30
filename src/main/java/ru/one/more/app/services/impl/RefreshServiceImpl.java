package ru.one.more.app.services.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.tapestry5.BooleanHook;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.one.more.app.entities.FeedSource;
import ru.one.more.app.services.FeedsDownloadService;
import ru.one.more.app.services.RefreshService;
import ru.one.more.parsers.rule.ParserRule;
import ru.one.more.workers.DataAccessHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by aboba on 29.01.17.
 */
public class RefreshServiceImpl implements RefreshService {
    @Inject
    FeedsDownloadService feedsDownloadService;


    @Override
    public boolean refreshFeeds() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<FeedSource> feedSourceList = DataAccessHelper.getInst().fetchFeedSources();
        List<Future<Boolean>> tasks = new ArrayList<>(feedSourceList.size());
        for (FeedSource fs : feedSourceList) {
            Future<Boolean> task = executorService
                    .submit(() -> feedsDownloadService
                            .downloadFeeds(fs.getSourceLink(), fs.getParseRule()));
            tasks.add(task);
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            return false;
        }
        for (Future<Boolean> task : tasks) {
            try {
                if (task.get()) return true;
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }
}
