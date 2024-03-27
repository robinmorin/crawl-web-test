package com.personal.backend.storage.data;

import com.personal.backend.enums.StatusSearch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataSetTest {

    @Test
    void testGetItem() {
        // Arrange
        DataSet<Object, Object> dataSet = new DataSet<>();

        // Act and Assert
        assertFalse(dataSet.getItem("Id").isPresent());
    }

    @Test
    void testSaveItem() {
        // Arrange
        DataSet<String, SearchObject> dataSet = new DataSet<>();
        SearchObject searchObject = new SearchObject("Id", "Keyword", StatusSearch.ACTIVE, new ArrayList<>());
        // Act
        dataSet.saveItem().accept(searchObject);
        // Assert
        assertEquals(searchObject, dataSet.getItem("Id").get());
    }


    @Test
    void testNewDataSet() {
        // Arrange
        DataSet<String, String> data = new DataSet<>();
        // Assert
        assertTrue(data.getItem("Id").isEmpty());

    }
}
