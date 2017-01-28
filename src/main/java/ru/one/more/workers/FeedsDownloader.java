package ru.one.more.workers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;
import ru.one.more.util.StrUtils;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by aboba on 28.01.17.
 */
public class FeedsDownloader {
    Logger logger = getLogger(FeedsDownloader.class);

    final HttpRequest request;
    public FeedsDownloader(HttpRequest request) {
        this.request = request;
    }

    public Optional<String> downloadFeeds() {
        HttpResponse<String> response;
        try {
            response = request.asString();
        } catch (UnirestException e) {
            logger.error("Unirest exception: " + e.getMessage());
            logger.error("Stack:\n" + StrUtils.getStackTraceText(e));

            return Optional.empty();
        }
        if (response.getStatus() != 200) {
            logger.error("response code = " + response.getStatus() + "; " +
                    response.getStatusText());
            return Optional.empty();
        }
        return Optional.of(response.getBody());
    }
}
