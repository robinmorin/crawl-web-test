package com.personal.backend.controller;

import com.personal.backend.context.StaticContext;
import com.personal.backend.dispatch.SearchDispatcher;
import com.personal.backend.dispatch.SearchEngine;
import com.personal.backend.enums.StatusSearch;
import com.personal.backend.exception.ApiError;
import com.personal.backend.storage.data.DataSet;
import com.personal.backend.storage.data.SearchObject;
import com.personal.backend.utils.SearchCodeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Objects;

import static spark.Spark.get;
import static spark.Spark.post;

/***
 * Controller to expose the http methods to the client
 */
public class CrawlController {

    private final DataSet<String, SearchObject> dataSet = StaticContext.getAttribute("dataSet");
    private final SearchDispatcher searchDispatcher = new SearchDispatcher();
    private final Gson gson = new Gson();

    /***
     * Exposes the http methods to the client
     */
    public void exposeHttpMethods() {
        post("/crawl", (req, res) -> {
            res.type("application/json");
            if(req.body().isEmpty()){
                res.status(400);
                return gson.toJson(new ApiError(400, "request body is required"));
            }
            JsonObject bodyObject = gson.fromJson(req.body(), JsonElement.class).getAsJsonObject();
            String keyword = bodyObject.get("keyword").getAsString();
            if (!validKeyword(keyword)) {
                res.status(400);
                return gson.toJson(new ApiError(400, "field 'keyword' is required (from 4 up to 32 chars)"));
            }
            var searchObject = new SearchObject(SearchCodeUtils.generateNewCode(),keyword, StatusSearch.ACTIVE, new ArrayList<>());
            var searchEngine = new SearchEngine(searchObject);
            searchDispatcher.dispatch(searchEngine);
            return String.format("{\"id\": \"%s\"}", searchObject.getId());
        });

        get("/crawl/:id", (req, res) -> {
                res.type("application/json");
                return dataSet.getItem(req.params("id"))
                              .map(searchObject -> {
                                        res.status(200);
                                        return gson.toJson(searchObject);
                                        })
                              .orElseGet(() -> {
                                        res.status(404);
                                        return gson.toJson(new ApiError(404, "search not found"));
                              });
        });

    }

    /**
     * Validates keyword with rules of test
     * @param keyword
     * @return true if keyword is valid
     */
    private boolean validKeyword(String keyword) {
        return !(Objects.isNull(keyword) || keyword.isBlank() || keyword.length() < 4 || keyword.length() > 32);
    }
}
