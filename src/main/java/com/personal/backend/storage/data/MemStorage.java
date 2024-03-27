package com.personal.backend.storage.data;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MemStorage is a class that implements the storage interface and is used to store objects in memory.
 * This class manages the objects in memory and provides the methods to save, update and get the objects.
 * @param <K>
 * @param <V>
 */
public abstract class MemStorage<K,V> {

    private final ConcurrentHashMap<K,V> mapContainer = new ConcurrentHashMap<>();

    public V save(K id, V object) {
        return mapContainer.putIfAbsent(id, object);
    }

    public V update(K id, V newObject) {
        return mapContainer.put(id, newObject);
    }

    public Optional<V> getObject(K id) {
        return Optional.ofNullable(mapContainer.get(id));
    }

}
