package com.personal.backend.storage.data;

import com.personal.backend.enums.StatusSearch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchObjectTest {

    @Test
    void testContainsUrlEmpty() {
        // Arrange, Act and Assert
        assertFalse((new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>()))
                .containsUrl("https://crawler-test.com/"));
    }

    @Test
    void testContainsUrlFilled() {
        // Arrange
        SearchObject searchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());
        searchObject.addUrl("https://crawler-test.com/");

        // Act and Assert
        assertTrue(searchObject.containsUrl("https://crawler-test.com/"));
    }


    @Test
    void testAddUrl() {
        // Arrange
        SearchObject searchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());

        // Act
        boolean actualAddUrlResult = searchObject.addUrl("https://crawler-test.com/");

        // Assert
        List<String> urls = searchObject.getUrls();
        assertEquals(1, urls.size());
        assertEquals("https://crawler-test.com/", urls.get(0));
        assertTrue(actualAddUrlResult);
    }

    @Test
    void testNotAddUrlIfExits() {
        // Arrange
        SearchObject searchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());
        searchObject.addUrl("https://crawler-test.com/");

        // Act and Assert
        assertFalse(searchObject.addUrl("https://crawler-test.com/"));
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SearchObject searchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());

        // Act
        searchObject.setStatus(StatusSearch.ACTIVE);
        String actualId = searchObject.getId();
        String actualKeyword = searchObject.getKeyword();
        StatusSearch actualStatus = searchObject.getStatus();
        searchObject.getUrls();

        // Assert that nothing has changed
        assertEquals("Id", actualId);
        assertEquals("Keyword", actualKeyword);
        assertEquals(StatusSearch.ACTIVE, actualStatus);
    }


    @Test
    void testNewSearchObject() {
        // Arrange and Act
        SearchObject actualSearchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());

        // Assert
        assertEquals("Id", actualSearchObject.getId());
        assertEquals("Keyword", actualSearchObject.getKeyword());
        assertEquals(StatusSearch.ACTIVE, actualSearchObject.getStatus());
        assertTrue(actualSearchObject.getUrls().isEmpty());
    }


    @Test
    void testNewSearchObjectWithStatusDone() {
        // Arrange and Act
        SearchObject actualSearchObject = new SearchObject("Id", "Keyword", StatusSearch.DONE, new ArrayList<>());

        // Assert
        assertEquals("Id", actualSearchObject.getId());
        assertEquals("Keyword", actualSearchObject.getKeyword());
        assertEquals(StatusSearch.DONE, actualSearchObject.getStatus());
        assertTrue(actualSearchObject.getUrls().isEmpty());
    }

    @Test
    void testNewSearchObjectWithStatusActive() {
        // Arrange
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://crawler-test.com/");

        // Act
        SearchObject actualSearchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, urls);

        // Assert
        assertEquals("Id", actualSearchObject.getId());
        assertEquals("Keyword", actualSearchObject.getKeyword());
        assertEquals(1, actualSearchObject.getUrls().size());
        assertEquals(StatusSearch.ACTIVE, actualSearchObject.getStatus());
    }


    @Test
    void testNewSearchObjectWithListFilled() {
        // Arrange
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://crawler-test.com/");
        urls.add("https://crawler-test.com/index.html");

        // Act
        SearchObject actualSearchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, urls);

        // Assert
        assertEquals("Id", actualSearchObject.getId());
        assertEquals("Keyword", actualSearchObject.getKeyword());
        assertEquals(2, actualSearchObject.getUrls().size());
        assertEquals(StatusSearch.ACTIVE, actualSearchObject.getStatus());
    }
}
