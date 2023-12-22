package api;

import controller.ConnectionHandler;
import controller.connections.HTTPConnectionHandler;

import static spark.Spark.get;
import static spark.Spark.port;

public class APIController {
    public APIController() {}

    public void run() {
        new UserAPIController().run();
    }
}
