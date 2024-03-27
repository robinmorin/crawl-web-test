package com.personal.backend.storage.data;

import com.personal.backend.enums.StatusSearch;
import com.personal.backend.storage.annotations.IdObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SearchObject is a class that represents the search object.
 * This class is used to store the search object in memory.
 * This object is updated by reference while the search engine is running, and allows consulting the status and the urls found any time.
 */
public class SearchObject {

    @IdObject
    private final String id;

    private transient final String keyword;
    private StatusSearch status;
    private final List<String> urls;

    public SearchObject(String id, String keyword, StatusSearch status, List<String> urls) {
        this.id = id;
        this.keyword = keyword;
        this.status = status;
        this.urls = Collections.synchronizedList(Objects.requireNonNull(urls));
    }

    public String getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setStatus(StatusSearch status) {
        this.status = status;
    }

    public StatusSearch getStatus() {
        return status;
    }

    public List<String> getUrls() {
        return urls;
    }

    public boolean containsUrl(String url) {
        return urls.contains(url);
    }

    public boolean addUrl(String url) {
        return !containsUrl(url) && urls.add(url);
    }

}
