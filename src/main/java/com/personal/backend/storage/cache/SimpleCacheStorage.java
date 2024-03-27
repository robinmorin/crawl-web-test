package com.personal.backend.storage.cache;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/***
 * Abstract class to represent the cache storage.
 * It is used to store the cache objects in the storage.
 * @param <K> Object will be used as the key of item in the storage.
 * @param <V> Object will be used as the value of item in the storage.
 */
public abstract class SimpleCacheStorage<K, V> {
    private final ConcurrentHashMap<K, CacheObject<V>> mapCacheStorage = new ConcurrentHashMap<>();

    protected CacheObject<V> put(K key, CacheObject<V> value) {
        return mapCacheStorage.putIfAbsent(key, value);
    }

    protected CacheObject<V> get(K key) {
        CacheObject<V> cacheObject = mapCacheStorage.get(key);
        if (Objects.nonNull(cacheObject) && Objects.nonNull(cacheObject.getExpiryTime())
                && cacheObject.getExpiryTime().compareTo(System.currentTimeMillis()) <= 0) {
            remove(key);
            return null;
        }
        return cacheObject;
    }

    protected void remove(K key) {
        mapCacheStorage.remove(key);
    }

    protected void clear() {
        mapCacheStorage.clear();
    }

    protected boolean contains(K key) {
        return mapCacheStorage.contains(key);
    }

    protected int size() {
        return mapCacheStorage.size();
    }

    protected boolean isEmpty() {
        return mapCacheStorage.isEmpty();
    }

}
