package com.personal.backend.storage.cache;

/***
 * Interface to represent the cache object.
 * It used to store the value and the expiry time of the object into CacheStorage
 * @param <V>
 */
public interface CacheObject<V> {
    V getValue();
    Long getExpiryTime();
    CacheObject<V> setValue(V value);
    CacheObject<V> setExpiryTime(Long expiryTime);

}