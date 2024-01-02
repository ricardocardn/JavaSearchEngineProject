package org.crawler.fileHandlers;
import org.jsoup.nodes.Document;

import java.io.IOException;

public interface FileHandler {
    abstract void saveDocument(Document bookDocument, int bookID) throws IOException;
    abstract void saveDocument(String content, String name) throws IOException;
}
