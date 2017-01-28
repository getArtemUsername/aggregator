package ru.one.more.workers;

import co.unruly.matchers.OptionalMatchers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aboba on 28.01.17.
 */
public class FeedDownloaderTest {

    @Test
    public void downloadFeedsTest() throws UnirestException, IOException {
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpResponse<String> response = Mockito.mock(HttpResponse.class);
        String respXml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("in/rbc-rss.xml"));
        when(response.getBody()).thenReturn(respXml);
        when(response.getStatus()).thenReturn(200);
        when(request.asString()).thenReturn(response);
        FeedsDownloader feedsDownloader = new FeedsDownloader(request);
        Optional<String> actRespXml = feedsDownloader.downloadFeeds();
        assertThat(actRespXml, OptionalMatchers.contains(is(respXml)));
    }

    @Test
    public void downloadFeedsTestFail() throws UnirestException {
        HttpRequest request = Mockito.mock(HttpRequest.class);
        HttpResponse<String> response = Mockito.mock(HttpResponse.class);
        when(response.getBody()).thenReturn("test");
        when(response.getStatus()).thenReturn(404);
        when(request.asString()).thenReturn(response);
        FeedsDownloader feedsDownloader = new FeedsDownloader(request);
        Optional<String> actRespXml = feedsDownloader.downloadFeeds();
        assertThat(actRespXml, OptionalMatchers.empty());
    }
}
