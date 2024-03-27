package com.personal.backend.utils;

import com.personal.backend.context.StaticContext;
import com.personal.backend.exception.HtmlDocUtilsException;
import com.personal.backend.handler.HttpConnectionHandler;
import com.personal.backend.storage.annotations.Cacheable;
import org.eclipse.jetty.util.URIUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * HtmlDocUtils is a class utility that provides methods to extract anchor tags from a html document and get the html document from a url.
 */
public final class HtmlDocUtils {
    private static final Pattern LINK_PATTERN = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1.*?>(.*?)</a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final HttpConnectionHandler connectionHandler =
                                                HttpConnectionHandler.handle(HttpConnectionHandler.createtHttpClientConfig()
                                                                                                    .readTimeout(Duration.ofSeconds(30))
                                                                                                    .connectTimeout(Duration.ofSeconds(60))
                                                                                                    .version(HttpClient.Version.HTTP_2)
                                                                                                    .executor(Executors.newFixedThreadPool(StaticContext.getAttributeAsInt("maxCorePoolSize")))
                                                                                     .build());
    private static final Predicate<String> isValidHRefOrRelative = href -> !href.startsWith("http://") && !href.startsWith("https://") && href.endsWith(".html") &&
                                                                   !href.startsWith("ftp://") && !href.startsWith("data:") && !href.startsWith("javascript:") &&
                                                                   !href.contains(".png") && !href.contains(".jpg") && !href.contains(".jpeg") &&
                                                                   !href.contains("mailto:") && !href.contains("tel:") && !href.startsWith("#");

    private HtmlDocUtils() {
        throw new UnsupportedOperationException("This class should not be instantiated.");
    }

    public static Set<String> extractAnchorTags(final String htmlDoc, String baseUrl) {
        Set<String> urlsResult = new HashSet<>();

        Matcher matcher = LINK_PATTERN.matcher(htmlDoc);
        while (matcher.find()) {
            String textResult = matcher.group(3);
            if (textResult.contains("Prev") || textResult.contains("Next")) continue;

            String hrefResult = matcher.group(2);
            if (hrefResult != null && (hrefResult.startsWith(baseUrl) || isValidHRefOrRelative.test(hrefResult))) {
                if(!hrefResult.startsWith(baseUrl)) hrefResult = URIUtil.addEncodedPaths(baseUrl, hrefResult);
                urlsResult.add(hrefResult);
            }
        }
        return urlsResult;
    }

    @Cacheable(duration = "PT10M")
    public static String getHtmlDocument(final String url) {
        try {
             HttpResponse<String> httpResponse = (HttpResponse<String>) connectionHandler.send(HttpRequest.newBuilder().uri(URI.create(url)).build());
            return httpResponse.statusCode() < 400 ? httpResponse.body() : "";
        } catch (Exception e) {
            throw new HtmlDocUtilsException(String.format("Error ocurrs while getting Html text from url: %s",url),e);
        }
    }

}
