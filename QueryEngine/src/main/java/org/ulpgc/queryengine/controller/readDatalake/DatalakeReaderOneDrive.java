package org.ulpgc.queryengine.controller.readDatalake;

import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.Book;




import java.nio.file.Path;

public class DatalakeReaderOneDrive{

    private static String metadataPath;
    private static String contentPath;
    private static String rawPath;


    public DatalakeReaderOneDrive(String metadataPath, String contentPath, String rawPath) {
        this.metadataPath = metadataPath;
        this.contentPath = contentPath;
        this.rawPath = rawPath;
    }


    public static MetadataBook readMetadata(String idBook) throws Exception {
        Path filePath = Path.of(metadataPath + "/" + idBook);

        String content = Reader.readFileContent(filePath);
        return MetadataExtractor.extractMetadata(content);
    }

    //Read content book
    public static String readContent(String idBook) throws Exception {
        Path filePath = Path.of(contentPath + "/" + idBook);
        String content = Reader.readFileContent(filePath);
        return content;
    }

    public static Book readRawBook(String idBook) throws Exception {
        Path filePath = Path.of(rawPath + "/" + idBook);
        String content = Reader.readFileContent(filePath);
        return new Book(idBook, content);
    }



}
