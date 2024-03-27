package com.personal.backend.handler;

import com.personal.backend.storage.cache.CacheObject;
import com.personal.backend.storage.cache.CacheObjectHttpResponse;
import com.personal.backend.storage.cache.SimpleCacheStorage;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * HttpCacheControl is a class that extends CacheStorage and is used to management of the cache of the http responses.
 * All logic of cache for HttpResponses is implemented in this class, and the CacheStorage abstract class has the logic of the cache storage.
 */
public class HttpSimpleCacheControl extends SimpleCacheStorage<String, HttpResponse<?>> {

    public HttpSimpleCacheControl() {
        super();
    }
    public Optional<CacheObject<HttpResponse<?>>> getResponse(URI uri, String httpMethod) {
        return Optional.ofNullable(get(httpMethod.concat(uri.toString())));
    }

    public HttpResponse<?> putResponse(HttpResponse<?> httpResponse, Long expiryTime) {
        var key = httpResponse.request().method().concat(httpResponse.request().uri().toString());
        put(key, new CacheObjectHttpResponse().setValue(httpResponse).setExpiryTime(expiryTime));
        return httpResponse;
    }
}
