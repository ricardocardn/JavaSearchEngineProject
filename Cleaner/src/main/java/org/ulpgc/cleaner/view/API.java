package org.ulpgc.cleaner.view;

import com.google.gson.Gson;
import org.ulpgc.cleaner.controller.reader.Reader;

import static spark.Spark.get;

public class API {
    private static Reader reader = new Reader();

    public static void getMetadata() {
        get("/datalake/metadata/:idBook", (req, res) -> {
            String idBook = req.params("idBook") + ".metadata";
            String metadata = reader.getMetadata(idBook);
            return (new Gson()).toJson(metadata);
        });
    }

    public static void getContent() {
        get("/datalake/content/:idBook", (req, res) -> {
            String idBook = req.params("idBook") + ".txt";
            String content = reader.getContent(idBook);
            return (new Gson()).toJson(content);
        });
    }

    public static void getRawBook () {
        get("/datalake/books/:idBook", (req, res) -> {
            String idBook = req.params("idBook") + ".txt";
            String rawBook = reader.getRawBook(idBook);
            return (new Gson()).toJson(rawBook);
        });
    }
}
