package org.crawler;

import org.crawler.api.CrawlerAPI;
import org.crawler.controllers.CrawlerController;

public class Main {
    public static void main(String[] args) throws Exception {
        String currentPath = System.getProperty("user.dir");
        System.out.println("Current Path: " + currentPath);

        CrawlerController controller = new CrawlerController();
        CrawlerAPI crawlerAPI = new CrawlerAPI();

        controller.run();
        crawlerAPI.run();
    }
}