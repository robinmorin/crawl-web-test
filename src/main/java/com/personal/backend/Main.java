package com.personal.backend;

import com.personal.backend.context.StaticContext;
import com.personal.backend.controller.CrawlController;
import com.personal.backend.storage.data.DataSet;
import com.personal.backend.storage.data.SearchObject;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        initContext();

        CrawlController crawlController = new CrawlController();
        crawlController.exposeHttpMethods();
    }


    /***
     * Method to initialize the context of the application.
     */
    private static void initContext() {

        Optional.ofNullable(System.getenv("BASE_URL"))
                .ifPresentOrElse(baseUrl -> StaticContext.setAttribute("baseUrl", baseUrl),
                                 () -> { throw new IllegalArgumentException("BASE_URL environment variable is required.");});

        var limitResult = Optional.ofNullable(System.getenv("LIMIT_RESULTS")).orElse("100");
        var minCorePoolSize = Optional.ofNullable(System.getenv("MIN_CORE_POOL_SIZE")).orElse("10");
        var maxCorePoolSize = Optional.ofNullable(System.getenv("MAX_CORE_POOL_SIZE")).orElse("50");
        var chunkSize = Optional.ofNullable(System.getenv("CHUNK_SIZE")).orElse("20");

        StaticContext.setAttribute("limitResults", Integer.valueOf(limitResult));
        StaticContext.setAttribute("minCorePoolSize", Integer.valueOf(minCorePoolSize));
        StaticContext.setAttribute("maxCorePoolSize", Integer.valueOf(maxCorePoolSize));
        StaticContext.setAttribute("chunkSize", Integer.valueOf(chunkSize));
        StaticContext.setAttribute("dataSet", new DataSet<String, SearchObject>());

    }

}
