package ru.one.more.workers;

import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.one.more.util.StrUtils;
import ru.one.more.app.entities.SourceRule;
import ru.one.more.parsers.XmlFeedsParser;
import ru.one.more.parsers.rule.ParserResult;
import ru.one.more.parsers.rule.ParserRule;

import java.util.Optional;
import java.util.concurrent.*;

/**
 * Created by aboba on 28.01.17.
 */
public class FeedGrabber {

    Logger logger = LoggerFactory.getLogger(FeedGrabber.class);

    ExecutorService grabbersPool = Executors.newWorkStealingPool(2);
    /*
     * возвращает true - если данные загрузились в бд и можно их доставать для отображения,
     * false - в случае ошибки
     */

    public Future<Boolean> grabFeeds(String url, ParserRule rule) {
        return grabbersPool.submit(() -> {
            FeedsDownloader downloader = new FeedsDownloader(Unirest.get(url));
            Optional<String> feedsStringOpt = downloader.downloadFeeds();
            if (rule.getSourceRule().getDataType() == SourceRule.DataType.XML) {
                Optional<ParserResult> result = new XmlFeedsParser().withRule(rule)
                        .flatMap(p -> feedsStringOpt.flatMap(p::parse));
                if (!result.isPresent() || Thread.currentThread().isInterrupted()) {
                    logger.warn("Task for url = "+ url + " was cancelled or parser get error(watch logs)");
                    return false;
                }
                ParserResult parserResult = result.get();
                try {
                    DataAccessHelper.getInstance().saveResult(parserResult, rule);
                } catch (DataAccessHelper.SaveResultException e) {
                    logger.error(e.getMessage());
                    logger.error(StrUtils.getStackTraceText(e));
                    return false;
                }
                return true;

            } else if (rule.getSourceRule().getDataType() == SourceRule.DataType.JSON) {
                //ToDo
            }
            return false;
        });
    }
}
