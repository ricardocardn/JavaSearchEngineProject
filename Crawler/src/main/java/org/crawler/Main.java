package org.crawler;

import org.crawler.api.CrawlerAPI;
import org.crawler.controllers.CrawlerController;

public class Main {
    public static void main(String[] args) throws Exception {
        CrawlerController controller = new CrawlerController();
        CrawlerAPI crawlerAPI = new CrawlerAPI();

        controller.run();
        crawlerAPI.run();
    }
}