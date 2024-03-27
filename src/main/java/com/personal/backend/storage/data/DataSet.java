package com.personal.backend.storage.data;

import com.personal.backend.storage.annotations.IdObject;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * DataSet is a class that represents the data set.
 * This class is used to store the data in memory.
 * This class manages the objects in memory and provides the methods to save and get the objects.
 * This class would be used for the storage any kind of objects in the memory.
 * The class for type object that will be stored in the memory must have a field marked with @IdObject annotation.
 */
public class DataSet<K,V> {

        private final Storage<K,V> data;

        public DataSet() {
            this.data = new StorageLocal<>();
        }

        public Optional<V> getItem(K id) {
            return data.getObject(id);
        }

        public Consumer<V> saveItem() {
            return data::save;
        }

        public interface Storage<K,V> {
            V save(V object);
            V update(K id, V newObject);
            Optional<V> getObject(K id);
        }

        private static class StorageLocal<K,V> extends MemStorage<K,V> implements Storage<K,V> {
            public StorageLocal() {
                super();
            }

            public V save(V object) {
                return super.save(getIdFieldValue(object), object);
            }

            private K getIdFieldValue(V object) {
                Class<?> clazz = object.getClass();
                return Arrays.stream(clazz.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(IdObject.class))
                        .findFirst()
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                return (K) field.get(object);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .orElseThrow(() -> new IllegalArgumentException(String.format("The object %s doesn't have field mark as @IdObject", clazz.getName())));
            }
        }

}
