package com.personal.backend.dispatch;

import com.personal.backend.context.StaticContext;
import com.personal.backend.enums.StatusSearch;
import com.personal.backend.storage.data.DataSet;
import com.personal.backend.storage.data.SearchObject;
import com.personal.backend.utils.SearchCodeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchDispatcherTest {

    @Test
    void testDispatch() {
        // Arrange
        SearchDispatcher searchDispatcher = new SearchDispatcher();
        String codeId = SearchCodeUtils.generateNewCode();
        SearchObject object = new SearchObject(codeId, "Keyword", StatusSearch.ACTIVE, new ArrayList<>());

        // Act
        searchDispatcher.dispatch(new SearchEngine(object));
        object.addUrl("https://crawler-test.com/");

        // Assert
        assertTrue(object.containsUrl("https://crawler-test.com/"));
        
    }

    @BeforeEach
    void setUp() {
        // Init context
        var limitResult = Optional.ofNullable(System.getenv("LIMIT_RESULTS")).orElse("100");
        var minCorePoolSize = Optional.ofNullable(System.getenv("MIN_CORE_POOL_SIZE")).orElse("10");
        var maxCorePoolSize = Optional.ofNullable(System.getenv("MAX_CORE_POOL_SIZE")).orElse("50");
        var chunkSize = Optional.ofNullable(System.getenv("CHUNK_SIZE")).orElse("20");

        StaticContext.setAttribute("baseUrl", "https://crawler-test.com/");
        StaticContext.setAttribute("limitResults", Integer.valueOf(limitResult));
        StaticContext.setAttribute("minCorePoolSize", Integer.valueOf(minCorePoolSize));
        StaticContext.setAttribute("maxCorePoolSize", Integer.valueOf(maxCorePoolSize));
        StaticContext.setAttribute("chunkSize", Integer.valueOf(chunkSize));
        StaticContext.setAttribute("dataSet", new DataSet<String, SearchObject>());
    }

}
