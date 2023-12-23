package controller;

import spark.Request;
import spark.Response;

public interface ConnectionHandler {
    String makeUrlRequest(String apiUrl, Request request, Response response, String method);
}
