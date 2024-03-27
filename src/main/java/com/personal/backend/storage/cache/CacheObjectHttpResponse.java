package com.personal.backend.storage.cache;

import java.net.http.HttpResponse;

/***
 * CacheObjectHttpResponse is a class that implements CacheObject interface and is used to store the HttpResponse objects in the cache.
 * It stores the HttpResponse object and the expiry time of the object.
 */
public class CacheObjectHttpResponse implements CacheObject<HttpResponse<?>>{

    private HttpResponse<?> value;
    private Long expiryTime;

    public HttpResponse<?> getValue() {
        return value;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public CacheObject<HttpResponse<?>> setValue(HttpResponse<?> value) {
        this.value = value;
        return this;
    }

    public CacheObject<HttpResponse<?>> setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
        return this;
    }

    public CacheObjectHttpResponse() {
    }

    public CacheObjectHttpResponse(HttpResponse<?> value, Long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

}
