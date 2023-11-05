package org.ulpgc.queryengine.view;

import com.google.gson.Gson;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReader;
import org.ulpgc.queryengine.controller.readDatamart.InvertedIndexReaderWord;
import org.ulpgc.queryengine.model.*;

import java.util.List;

import static spark.Spark.*;
import static spark.Spark.port;

public class API {

    private static DatamartReader datamartReader;

    public static void runAPI(){
        datamartReader = new InvertedIndexReaderWord();
        port(8080);
        getWordDocuments();
        getPhrase();
        getRecommendBook();
        getFrequencyWord();
        getMetadata();
        getRawBook();
        getContent();
    }

    private static void getWordDocuments() {
        get("/datamart/:word", (req, res) -> {
            String word = req.params("word");
            List<WordDocuments> documents = datamartReader.getDocumentsWord(word);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getPhrase() {
        get("/datamart-search/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<WordDocuments> documents = datamartReader.getDocumentsWord(phrase);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getRecommendBook(){
        get("/datamart-recommend/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<RecommendBook> book = datamartReader.getRecommendBook(phrase);
            return (new Gson()).toJson(book);
        });
    }

    private static void getFrequencyWord(){
        get("/datamart-frequency/:word", (req, res) -> {
            String word = req.params("word");
            WordFrequency frequency = datamartReader.getFrequency(word);
            return (new Gson()).toJson(frequency);
        });
    }

    private static void getMetadata(){
        get("/datalake/metadata/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".metadata";
            MetadataBook metadata = DatalakeReaderOneDrive.readMetadata(idbook);
            return (new Gson()).toJson(metadata);
        });
    }

    private static void getRawBook(){
        get("/datalake/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".txt";
            Book book = DatalakeReaderOneDrive.readRawBook(idbook);
            return (new Gson()).toJson(book);
        });
    }

    private static void getContent(){
        get("/datalake/content/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".content";
            String content = DatalakeReaderOneDrive.readContent(idbook);
            return (new Gson()).toJson(content);
        });
    }

}
