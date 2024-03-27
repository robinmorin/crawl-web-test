package com.personal.backend.storage.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CacheObjectHttpResponseTest {

    @Test
    void testGetValue() {
        // Arrange, Act and Assert
        assertNull((new CacheObjectHttpResponse()).getValue());
    }

    @Test
    void testGettersAndSettersDefaulConstructor() {
        // Arrange and Act
        CacheObjectHttpResponse actualCacheObjectHttpResponse = new CacheObjectHttpResponse();
        actualCacheObjectHttpResponse.setExpiryTime(1L);
        actualCacheObjectHttpResponse.setValue(null);
        Long actualExpiryTime = actualCacheObjectHttpResponse.getExpiryTime();

        // Assert
        assertNull(actualCacheObjectHttpResponse.getValue());
        assertEquals(1L, actualExpiryTime.longValue());
    }

    @Test
    void testGettersAndSettersAllParamConstructor() {
        // Arrange and Act
        CacheObjectHttpResponse actualCacheObjectHttpResponse = new CacheObjectHttpResponse(null, 1L);
        actualCacheObjectHttpResponse.setExpiryTime(1L);
        actualCacheObjectHttpResponse.setValue(null);
        Long actualExpiryTime = actualCacheObjectHttpResponse.getExpiryTime();

        // Assert
        assertNull(actualCacheObjectHttpResponse.getValue());
        assertEquals(1L, actualExpiryTime.longValue());
    }
}
