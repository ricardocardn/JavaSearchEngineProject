package api;

import controller.connections.ConnectionHandler;
import controller.connections.HTTPConnectionHandler;

import static spark.Spark.*;

public class UserAPIController {
    private final String USER_SERVICE_API;
    private final ConnectionHandler connectionHandler;

    public UserAPIController() {
        this.USER_SERVICE_API = System.getenv("USER_SERVICE_API");
        connectionHandler = new HTTPConnectionHandler();
    }

    public void run() {
        port(80);
        handleUserAPI();
    }

    private void handleUserAPI() {
        userLogin();
        userSignUp();
        userName();
        postBook();
        getBooks();
        userLogout();
    }

    private void userLogin() {
        get("user/login", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/login", req, res, "GET"));
    }

    private void userSignUp() {
        get("user/sign-up", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/sign-up", req, res, "GET"));
    }

    private void userName() {
        get("user/name", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/name", req, res, "GET"));
    }

    private void postBook() {
        post("user/post", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/post", req, res, "POST"));
    }

    private void getBooks() {
        get("user/books", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/books", req, res, "GET"));
    }

    private void userLogout() {
        get("user/logout", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/logout", req, res, "GET"));
    }
}
