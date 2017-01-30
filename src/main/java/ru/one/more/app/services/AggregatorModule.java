package ru.one.more.app.services;

import org.apache.tapestry5.ioc.ServiceBinder;
import ru.one.more.app.services.impl.FeedsDownloadServiceImpl;
import ru.one.more.app.services.impl.FeedsServiceImpl;
import ru.one.more.app.services.impl.RefreshServiceImpl;
import ru.one.more.app.services.impl.RulesServiceImpl;

/**
 * Created by aboba on 25.01.17.
 */
public class AggregatorModule {
        public static void bind(ServiceBinder binder) {
            binder.bind(RulesService.class, RulesServiceImpl.class);
            binder.bind(RefreshService.class, RefreshServiceImpl.class);
            binder.bind(FeedsService.class, FeedsServiceImpl.class);
            binder.bind(FeedsDownloadService.class, FeedsDownloadServiceImpl.class);
        }
}
