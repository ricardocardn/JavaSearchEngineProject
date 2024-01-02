package org.crawler.fillers;

import org.crawler.model.Book;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentFiller {
    public String fillDocument(Book book) {
        String opening = constructMetadataOpening(book);
        String ending = constructMetadataEnding(book);
        return opening + book.content() + ending;
    }

    private String constructMetadataOpening(Book book) {
        String filePath = "./src/main/resources/metadata/opening.txt";

        try {
            String fileContent = readFileAsString(filePath);
            return String.format(fileContent, book.name(), book.author(), book.date(), book.language());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String constructMetadataEnding(Book book) {
        String filePath = "./src/main/resources/metadata/ending.txt";

        try {
            String fileContent = readFileAsString(filePath);
            return fileContent;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String readFileAsString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
