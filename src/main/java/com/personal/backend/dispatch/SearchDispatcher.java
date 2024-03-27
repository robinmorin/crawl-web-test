package com.personal.backend.dispatch;

import com.personal.backend.context.StaticContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Dispatches search engines to be executed in a thread pool for search tasks.
 */
public class SearchDispatcher  {

    private final ExecutorService executorService;

    public SearchDispatcher() {
        var minCorePoolSize = StaticContext.getAttributeAsInt("minCorePoolSize");
        var maxCorePoolSize = StaticContext.getAttributeAsInt("maxCorePoolSize");
        this.executorService = new ThreadPoolExecutor(minCorePoolSize, maxCorePoolSize, 0L, TimeUnit.MILLISECONDS,
                                                      new LinkedBlockingQueue<>(), new SearchThreadFactory());
    }

    public void dispatch(SearchEngine searchEngine) {
        executorService.execute(searchEngine);
    }

    /**
     * ThreadFactory to take control of creation the threads for the thread pool.
     */
    private static class SearchThreadFactory implements ThreadFactory {

        private static int threadNumber;

        @Override
        public Thread newThread(Runnable r) {
            delay(100);
            Thread thread = new Thread(r);
            thread.setName("SearchThread-"+threadNumber++);
            thread.setUncaughtExceptionHandler(new SearchExceptionHandler());
            return thread;
        }

        /***
         * Delays the thread creation. It's a good practice to avoid thread creation storms.
         * @param milisegs
         */
        private void delay(int milisegs){
            try {
                Thread.sleep(milisegs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
