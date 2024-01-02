package org.crawler.api;

import org.crawler.fileHandlers.FileHandler;
import org.crawler.fileHandlers.LocalFileHandler;
import org.crawler.fillers.DocumentFiller;
import org.crawler.model.Book;

import static spark.Spark.*;

public class CrawlerAPI {
    private final FileHandler fileHandler;
    private final DocumentFiller documentFiller;

    public CrawlerAPI() throws Exception {
        fileHandler = new LocalFileHandler();
        documentFiller = new DocumentFiller();
    }

    public void run() {
        port(8081);
        postDocument();
    }

    private void postDocument() {
        post("/post", (req, res) -> {
            String name = req.queryParams("name");
            String author = req.queryParams("author");
            String language = req.queryParams("language");
            String date = req.queryParams("date");
            String content = req.body();

            content = documentFiller.fillDocument(new Book(name, author, language, date, content));
            fileHandler.saveDocument(content, name);

            return "Added and enqueued book " + name;
        });
    }
}
