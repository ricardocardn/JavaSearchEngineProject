package org.crawler.schedulerTasks;

import org.crawler.downloadHandlers.GutenbergDownloadHandler;
import org.crawler.fileHandlers.LocalFileHandler;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.TimerTask;

public class GutenbergTask extends TimerTask {
    @Override
    public void run() {
        try {
            handleAllBooks(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAllBooks(Integer amountOfBooks) throws Exception {
        int lastFileID = new LocalFileHandler().getLastFileIdInLastDirectory() + 1;
        for (int i = lastFileID; i < amountOfBooks + lastFileID; i++) {
            System.out.println("Downloading book with ID: " + i);
            try {
                handleSingleBook(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSingleBook(int bookID){
        GutenbergDownloadHandler downloader = new GutenbergDownloadHandler();
        Document downloadedDocument;
        {
            try {
                downloadedDocument = downloader.handleDownload(bookID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        LocalFileHandler fileHandler = null;

        try {
            fileHandler = new LocalFileHandler();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            fileHandler.saveDocument(downloadedDocument, bookID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
