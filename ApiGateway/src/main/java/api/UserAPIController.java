package api;

import controller.ConnectionHandler;
import controller.connections.HTTPConnectionHandler;

import static spark.Spark.get;
import static spark.Spark.port;

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
        userName();
        postBook();
    }

    private void userLogin() {
        get("user/login", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/login", req, res));
    }

    private void userName() {
        get("user/name", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/name", req, res));
    }

    private void postBook() {
        get("user/post", (req, res) ->
                connectionHandler.makeUrlRequest(USER_SERVICE_API + "user/post", req, res));
    }
}
