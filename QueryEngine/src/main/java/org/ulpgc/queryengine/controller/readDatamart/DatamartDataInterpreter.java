package org.ulpgc.queryengine.controller.readDatamart;

import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatamartDataInterpreter {
    private static String datamartPath;

    public DatamartDataInterpreter(String datamartPath){
        this.datamartPath = datamartPath;
    }

    public static List<WordDocuments> getDocumentsWord(String param){
        List<WordDocuments> documents = new ArrayList<>();

        String[] words= param.split("\\+");

        for (String word : words){
            List<String> idDocuments = get_documents(word);
            WordDocuments wordDocuments = new WordDocuments(word, idDocuments);
            documents.add(wordDocuments);

        }
        return documents;
    }

    public static List<String> get_documents(String word){
        word = word.toLowerCase();
        String filePath = datamartPath + "/" + word;
        List<String> lines;

        try {
            Path path = Paths.get(filePath);
            lines = Files.readAllLines(path);

        } catch (IOException e) {
            e.printStackTrace();
            lines = null;
        }
        return lines;
    }

    public static List<RecommendBook> getRecommendBook(String phrase) {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(phrase);
        Map<String, Integer> idCountMap = new HashMap<>();
        Map<String, String> idTitleMap = new HashMap<>();

        for (WordDocuments wordDocuments : wordDocumentsList) {
            for (String id : wordDocuments.documentsId()) {
                idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);
                String title = getTitleForId(id);
                idTitleMap.put(id, title);
            }
        }

        int maxCount = 0;
        List<RecommendBook> mostRecommendedBooks = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : idCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostRecommendedBooks.clear();
                mostRecommendedBooks.add(new RecommendBook(idTitleMap.get(entry.getKey()), entry.getKey()));
            } else if (entry.getValue() == maxCount) {
                mostRecommendedBooks.add(new RecommendBook(idTitleMap.get(entry.getKey()), entry.getKey()));
            }
        }

        return mostRecommendedBooks;
    }

    private static String getTitleForId(String id) {
        try {
            MetadataBook metadataBook = DatalakeReaderOneDrive.readMetadata(id);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }


}
