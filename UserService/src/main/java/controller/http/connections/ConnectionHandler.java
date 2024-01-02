package controller.http.connections;

import model.Book;
import spark.Request;
import spark.Response;

public interface ConnectionHandler {
    String makeUrlRequest(String path, Book book);
}
