package ru.one.more.app.services;

import org.apache.tapestry5.ioc.ServiceBinder;
import ru.one.more.app.services.impl.FeedsDownloadServiceImpl;
import ru.one.more.workers.DataAccessHelper;
import ru.one.more.app.services.impl.ReadFeedsServiceImpl;
import ru.one.more.app.services.impl.RefreshServiceImpl;

/**
 * Created by aboba on 25.01.17.
 */
public class AggregatorModule {
        public static void bind(ServiceBinder binder) {
            binder.bind(RefreshService.class, RefreshServiceImpl.class);
            binder.bind(ReadFeedsService.class, ReadFeedsServiceImpl.class);
            binder.bind(FeedsDownloadService.class, FeedsDownloadServiceImpl.class);
        }
}
