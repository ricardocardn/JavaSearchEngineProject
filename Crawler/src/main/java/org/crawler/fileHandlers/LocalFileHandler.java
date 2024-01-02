package org.crawler.fileHandlers;

import org.crawler.message.broker.EventPublisher;
import org.crawler.message.broker.Publisher;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class LocalFileHandler implements FileHandler{
    private static final String DOCUMENT_REPOSITORY_PATH = "./DocumentsRepository/RawBooks/";
    private final Publisher filePublisher;

    public LocalFileHandler() throws Exception {
        this.filePublisher = new EventPublisher("61616", "datalakeEvents");
    }

    @Override
    public void saveDocument(Document bookDocument, int bookID) throws IOException {
        createFile(bookDocument.text(), String.valueOf(bookID));
        publishFileAddition(String.valueOf(bookID));
    }

    @Override
    public void saveDocument(String content, String name) throws IOException {
        createFile(content, name);
        publishFileAddition(name);
    }

    private void publishFileAddition(String bookID) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        filePublisher.publish( bookID + ".txt");
    }

    public int getLastFileIdInLastDirectory() {
        File directory = new File(DOCUMENT_REPOSITORY_PATH);
        File[] filesInLastDirectory = directory.listFiles();
        if (filesInLastDirectory != null && filesInLastDirectory.length > 0) {
            Arrays.sort(filesInLastDirectory, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
            return Integer.parseInt(filesInLastDirectory[0].getName().replace(".txt", ""));
        } else {
            return 0;
        }

    }

    private void createFile(String bookDocument, String bookID) throws IOException{
        String filePath = DOCUMENT_REPOSITORY_PATH + bookID + ".txt";
        FileWriter writer = new FileWriter(filePath);
        writer.write(bookDocument);
        writer.close();
    }

    private void createFolder(){
        File folder = new File(getFolderPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private String getFolderPath(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        return DOCUMENT_REPOSITORY_PATH + currentDate + "/";
    }


}
