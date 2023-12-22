package api;

import controller.loggers.MongoUserLogger;
import controller.loggers.UserLogger;
import controller.sessions.SessionHandler;
import controller.sessions.cookie.SessionHazelcastHandler;
import model.User;

import static spark.Spark.*;

public class APIController {
    private final UserLogger logger;
    private final SessionHandler sessionHandler;

    public APIController() {
        logger = new MongoUserLogger();
        sessionHandler = new SessionHazelcastHandler();
    }

    public void run() {
        port(8080);
        getSignUp();
        getLogin();
        getUserName();
    }

    private void getSignUp() {
        get("user/sign-up", (req, res) -> {
            String session = logger.logUser(req.queryParams("username"), req.queryParams("password"));
            if (session != null) {
                res.cookie("Session", session);
                return "User logged successfully";
            } else {
                res.status(400);
                return "User already exists";
            }
        });
    }

    private void getLogin() {
        get("user/login", (req, res) -> {
            String session = logger.logUser(req.queryParams("username"), req.queryParams("password"));
            if (session != null) {
                res.cookie("Session", session);
                return "User logged successfully";
            } else {
                res.status(400);
                return "User already exists";
            }
        });
    }

    private void getUserName() {
        get("user/name", (req, res) -> {
            String session = req.cookie("Session");
            try {
                User user = sessionHandler.hasValidSession(session);
                return "Your user name is: " + user.username();

            } catch (Exception e) {
                res.status(400);
                return "User logged-out";
            }

        });
    }
}
