# CRAWLER WEB (PoC)

Project for the Web Crawler Proof of Concept (PoC).

## Description
The project to be develop using only classes the Java 14 provides. Has been included Spark for Controller Non Blocking Reactive Endpoints.

### Stacks:
- Java 14
- Sparl Core
- Gson
- Slf4j
- JUnit 5
- Mockito Core

### Features:
For development the this project, the following features will be implemented, such as:
- [x] DataSet Class for saving data in memory
- [x] Context Class for managing configuration and attributes of the application
- [x] Dispatcher Class for managing the threads of search
- [x] SimpleCache Class for managing the cache control
- [x] HttpConnectionHandler Class for managing the HTTP connections including Cache Control
- [x] HtmlDocUtils Class Utility for managing the HTML documents
- [x] Generator Class for generating random Search Id

### Environment Variables:
- BASE_URL: Base URL for Crawl. (required)
- LIMIT_RESULTS: Limit of results list of search. (optional) Default: 100
- MIN_CORE_POOL_SIZE: Minimum value of Core Pool Thread. (optional) Default: 10
- MAX_CORE_POOL_SIZE: Maximum value of Core Pool Thread. (optional) Default: 50
- CHUNK_SIZE: Chunk block size for individual search. (optional) Default: 20

The thread pool receive um thread for each search. The thread pool is managed by the Dispatcher class.
Into each thread, the search is divided into chunks. The chunk size is managed by the CHUNK_SIZE environment variable.

### How to run:

For running the project, you can use the following command in the root of the project:
```shell
docker build . -t personal/crawlweb
docker run -e BASE_URL=https://crawler-test.com/ -p 4567:4567 --rm personal/crawlweb
```   


### Controller:
Given the following endpoints:
- [POST /crawl](http://localhost:4567/crawl) - POST for send keyword to search
  <br>
  <br>
    - Request:
     ```json
     {
       "keyword": "keyword"
     }
     ```
    <br>   

    - Response:
     ```json
     {
       "id": "yh4S5zwe"  
     }
     ```
  Id returned is ramdom code for search. Check [SearchCodeUtils](src/main/java/com/personal/backend/utils/SearchCodeUtils.java)

<br>   

- [GET /crawl/:searchId](http://localhost:4567/crawl/:searchId) - GET for get the search result
  <br>
  <br>
    - Response:
      <br>
        ```json
        {
          "id": "keyword",
          "status": "active",
          "urls": [
              "https://crawler-test.com/",
              "https://crawler-test.com/urls/directory_index/index.html",
              "https://crawler-test.com/urls/duplication_types/index.html",
              "https://crawler-test.com//urls/double_slash/disallowed_start"
            ]
        }
        ```
      The status can be active or done. The urls are the result list of the search.


