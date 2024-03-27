package com.personal.backend.dispatch;

import com.personal.backend.context.StaticContext;
import com.personal.backend.enums.StatusSearch;
import com.personal.backend.storage.data.DataSet;
import com.personal.backend.storage.data.SearchObject;
import com.personal.backend.utils.HtmlDocUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

/***
 * SearchEngine class that implements Runnable to be executed in a thread pool
 */
public class SearchEngine implements Runnable {

    private static final String BASE_URL = StaticContext.getAttribute("baseUrl");
    private static final Integer LIMIT_RESULTS = StaticContext.getAttributeAsInt("limitResults");
    private static final Logger log = LoggerFactory.getLogger(SearchEngine.class);
    private final SearchObject searchObject;
    private final Queue<String> urlsToVisit = new LinkedBlockingQueue<>();
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());

    /***
     * It's a good practice to avoid creating an instance of this class without a SearchObject.
     */
    private SearchEngine() {
        throw new UnsupportedOperationException("Cannot create a SearchEngine without a codeSearch.");
    }

    public SearchEngine(SearchObject searchObject) {
        this.searchObject = searchObject;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(String.format("SearchEngine[%s]", searchObject.getId()));
        var start = LocalDateTime.now();
        log.info("Starting search for keyword: \"{}\" in Thread: {} at {}", searchObject.getKeyword(), Thread.currentThread().getName(), start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        DataSet<String, SearchObject> dataSet = StaticContext.getAttribute("dataSet");
        dataSet.saveItem().accept(searchObject);
        urlsToVisit.add(BASE_URL);
        while (!urlsToVisit.isEmpty() && searchObject.getUrls().size() < LIMIT_RESULTS && searchObject.getStatus().equals(StatusSearch.ACTIVE)) {
            getChunk().stream().parallel().forEach(toVisit -> {
                if(searchObject.getStatus().equals(StatusSearch.ACTIVE)){
                    search(toVisit, searchObject);
                }
            });
        }
        searchObject.setStatus(StatusSearch.DONE);
        var timeLapsed = ChronoUnit.SECONDS.between(start, LocalDateTime.now());
        log.info("Finish search for keyword: \"{}\" in Thread: {} - Visited Urls: {} - Timelapsed: {}(secs)",
                  searchObject.getKeyword(), Thread.currentThread().getName(), visitedUrls.size(), timeLapsed);
    }

    private List<String> getChunk() {
        List<String> chunk = new ArrayList<>();
        for (int i = 0; i < StaticContext.getAttributeAsInt("chunkSize") && !urlsToVisit.isEmpty(); i++){
            chunk.add(urlsToVisit.poll());
        }
        return chunk;
    }

    private void search(String url, SearchObject searchObject) {
        if(searchObject.getStatus().equals(StatusSearch.DONE) || visitedUrls.contains(url)) return;

        var docHtml = HtmlDocUtils.getHtmlDocument(url);
        visitedUrls.add(url);
        if(docHtml.isBlank()) return;

        Pattern pattern = Pattern.compile(searchObject.getKeyword(), Pattern.CASE_INSENSITIVE);
        var hasKeyword = pattern.matcher(docHtml).find();

        if(hasKeyword && searchObject.getUrls().size() < LIMIT_RESULTS) {
            searchObject.addUrl(url);
        }
        if(LIMIT_RESULTS.equals(searchObject.getUrls().size())) {
            searchObject.setStatus(StatusSearch.DONE);
            return;
        }
        var urls = HtmlDocUtils.extractAnchorTags(docHtml, BASE_URL);
        if(urls.isEmpty()) return;
        urls.forEach(newUrl -> {
            if(!urlsToVisit.contains(newUrl) && !visitedUrls.contains(newUrl)) {
                urlsToVisit.add(newUrl);
            }
        });
    }

}
