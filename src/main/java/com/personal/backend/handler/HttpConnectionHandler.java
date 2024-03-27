package com.personal.backend.handler;

import com.personal.backend.exception.HttpConnectionHandlerException;
import com.personal.backend.storage.annotations.Cacheable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * HttpConnectionHandler is a class that is used to handle the http connections.
 * It is a singleton class that has a HttpClient instance, it gives a one upgrade for HttpClient with Cache Control inside.
 */
public final class HttpConnectionHandler {


    private final HttpClient httpClient;
    private static HttpConnectionHandler instance;
    private final HttpSimpleCacheControl httpCacheControl;

    private static final Logger log = Logger.getLogger(HttpConnectionHandler.class.getName());

    private HttpConnectionHandler() {
        throw new UnsupportedOperationException("This class should not be instantiated.");
    }

    private HttpConnectionHandler(HttpClientConfig httpClientConfig) {
        var builder = HttpClient.newBuilder()
                .version(httpClientConfig.getVersion())
                .connectTimeout(httpClientConfig.getConnectTimeout())
                .followRedirects(httpClientConfig.isFollowRedirects() ? HttpClient.Redirect.NORMAL : HttpClient.Redirect.NEVER);
        if(httpClientConfig.getExecutor() != null) builder.executor(httpClientConfig.getExecutor());
        this.httpClient = builder.build();
        httpCacheControl = new HttpSimpleCacheControl();
    }

    public static HttpConnectionHandler handle() {
        instance = new HttpConnectionHandler(defaultHttpClientConfig());
        return instance;
    }

    public static HttpConnectionHandler handle(HttpClientConfig config) {
        instance = new HttpConnectionHandler(config);
        return instance;
    }

    /***
     * Send a HttpRequest and return a HttpResponse.
     * The method that it call from, could be annotated with Cacheable annotation for apply cache control.
     * If the method is annotated with Cacheable, the response will be cached with the duration specified in the annotation.
     * You can put the duration for the cache in the annotation Cacheable in the method that call this method.
     * The duration should be in String format and should be parseable by Duration.parse method.
     * In the annotation @Cacheable has examples of how to put the duration.
     * @param request
     * @return HttpResponse
     */
    public HttpResponse<?> send(HttpRequest request)  {
        try{
            String cacheableAndExpiryDuration = cacheableAndExpiryDuration();
            AtomicReference<HttpResponse<?>> response = new AtomicReference<>();
            AtomicBoolean inCache = new AtomicBoolean(false);
            if(Objects.nonNull(cacheableAndExpiryDuration)) {
                httpCacheControl.getResponse(request.uri(), request.method())
                        .ifPresentOrElse(httpResponseCacheObject -> {
                                            inCache.set(true);
                                            response.set(httpResponseCacheObject.getValue());
                                        },
                            () -> {
                                    try {
                                        var newResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                                        response.set(newResponse);
                                    } catch (IOException | InterruptedException e) {
                                        throw new HttpConnectionHandlerException(String.format("Error calling web page %s",request.uri().toString()), e);
                                    }
                            });
            }
            if(cacheableAndExpiryDuration != null && !inCache.get()) {
                Long expiryDuration = null;
                if(!cacheableAndExpiryDuration.isBlank()) expiryDuration = Duration.parse(cacheableAndExpiryDuration).toMillis() + System.currentTimeMillis();
                return httpCacheControl.putResponse(response.get(), expiryDuration);
            }
            return response.get();
        } catch (Exception e) {
            throw new HttpConnectionHandlerException(String.format("Error sending request: %s ", e.getMessage()), e);
        }
    }

    /**
     * By reflection, if the method that call this method is annotated with Cacheable, it will return the duration of the cache.
     * @return String value with the duration of the cache. If not put value in duration,
     *         it will return blank for denote that it is cacheable but is infinite time to expires.
     *         In case that the method is not annotated with Cacheable, it will return null.
     */
    private String cacheableAndExpiryDuration() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        var stackElement = stackTraceElements[3];
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class<?> clazz = classLoader.loadClass(stackElement.getClassName());
            Method method = Arrays.stream(clazz.getMethods()).filter(md -> md.getName().equals(stackElement.getMethodName())).findFirst().orElseThrow();
            Cacheable annotation = method.getAnnotation(Cacheable.class);
            if(annotation != null) {
                return annotation.duration();
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            log.info(String.format("Error getting class by name: %s",e.getMessage()));
            throw new HttpConnectionHandlerException("Error analizing annotation Cacheable in class : "+ e.getMessage(), e);
        } catch (Exception e) {
            throw new HttpConnectionHandlerException("Others error analizing methods and annotations in class:"+e.getMessage(), e);
        }
    }

    /**
     * Return the instance of the HttpClientConfig with default values.
     * @return HttpClientConfig
     */
    public static HttpClientConfig defaultHttpClientConfig() {
        return new HttpClientConfigImpl.HttpClientConfigBuilderImpl().build();
    }

    /**
     * Return a builder for HttpClientConfig.
     * @return
     */
    public static HttpClientConfig.Builder createtHttpClientConfig() {
        return new HttpClientConfigImpl.HttpClientConfigBuilderImpl();
    }

    /***
     * HttpClientConfigImpl is a class that implements HttpClientConfig.
     * Represents the basic configuration of the HttpClient.
     */
    private static class HttpClientConfigImpl implements HttpClientConfig {
        private final Duration connectTimeout;
        private final Duration readTimeout;
        private final HttpClient.Version version;
        private final Executor executor;
        private final boolean followRedirects;

        public HttpClientConfigImpl(Duration connectTimeout, Duration readTimeout, HttpClient.Version version, Executor executor, boolean followRedirects) {
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            this.version = version;
            this.executor = executor;
            this.followRedirects = followRedirects;
        }

        public Duration getConnectTimeout() {
            return connectTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public HttpClient.Version getVersion() {
            return version;
        }

        public Executor getExecutor() {
            return executor;
        }

        public boolean isFollowRedirects() {
            return followRedirects;
        }

        /**
         * Builder for HttpClientConfigImpl.
         */
        private static class HttpClientConfigBuilderImpl implements HttpClientConfig.Builder {
            private Duration connectTimeout = Duration.ofSeconds(30);
            private Duration readTimeout = Duration.ofSeconds(30);
            private HttpClient.Version version = HttpClient.Version.HTTP_2;
            private Executor executor = null;
            private boolean followRedirects;

            public HttpClientConfig.Builder version(HttpClient.Version version) {
                this.version = version;
                return this;
            }

            public HttpClientConfig.Builder executor(Executor executor) {
                this.executor = executor;
                return this;
            }

            public HttpClientConfig.Builder readTimeout(Duration seconds) {
                this.readTimeout = seconds;
                return this;
            }

            public HttpClientConfig.Builder followRedirects(boolean followRedirects) {
                this.followRedirects = followRedirects;
                return this;
            }

            public HttpClientConfig.Builder connectTimeout(Duration seconds) {
                this.connectTimeout = seconds;
                return this;
            }

            public HttpClientConfig build() {
                return new HttpClientConfigImpl(connectTimeout, readTimeout, version, executor, followRedirects);
            }
        }
    }

    /**
     * HttpClientConfig is an interface that represents the contract for basic configuration of the HttpClient.
     */
    public interface HttpClientConfig {

        Duration getConnectTimeout();
        Duration getReadTimeout();
        HttpClient.Version getVersion();
        Executor getExecutor();
        boolean isFollowRedirects();

        /**
         * Represent the contract for Builder of HttpClientConfig.
         */

        interface Builder {
            HttpClientConfig.Builder connectTimeout(Duration timeout);
            HttpClientConfig.Builder readTimeout(Duration timeout);
            HttpClientConfig.Builder version(HttpClient.Version version);
            HttpClientConfig.Builder executor(Executor executor);
            HttpClientConfig.Builder followRedirects(boolean followRedirects);
            HttpClientConfig build();
        }
    }

}
