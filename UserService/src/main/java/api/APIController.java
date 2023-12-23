package api;

import controller.MongoConnection;
import controller.bookhandler.DatamartBookHandler;
import controller.bookhandler.MongoBookHandler;
import controller.loaders.MongoUserLoader;
import controller.loaders.UserLoader;
import controller.loggers.MongoUserLogger;
import controller.loggers.UserLogger;
import controller.sessions.SessionHandler;
import controller.sessions.cookie.SessionHazelcastHandler;
import model.Book;
import model.User;

import static spark.Spark.*;

public class APIController {
    private final UserLogger logger;
    private final UserLoader loader;
    private final DatamartBookHandler bookHandler;
    private final SessionHandler sessionHandler;

    public APIController() {
        MongoConnection mongoConnection = new MongoConnection();
        logger = new MongoUserLogger(mongoConnection);
        loader = new MongoUserLoader(mongoConnection);
        bookHandler = new MongoBookHandler(mongoConnection);
        sessionHandler = new SessionHazelcastHandler();
    }

    public void run() {
        port(8080);
        getLogin();
        getUserName();
        postDocument();
    }

    private void getLogin() {
        get("user/login", (req, res) -> {
            String session = logger.logUser(req.queryParams("username"), req.queryParams("password"));
            if (session != null) {
                res.cookie("Session", session);
                return "User logged successfully";
            } else {
                res.status(400);
                return "The given credentials are incorrect";
            }
        });
    }

    private void getUserName() {
        get("user/name", (req, res) -> {
            String session = req.cookie("Session");
            try {
                User user = sessionHandler.hasValidSession(session);
                if (user == null)
                    return "User logged out";

                return "Your user name is: " + user.username();

            } catch (Exception e) {
                res.status(400);
                return e.getMessage();
            }

        });
    }

    private void postDocument() {
        get("user/post", (req, res) -> {
            String session = req.cookie("Session");
            try {
                User user = sessionHandler.hasValidSession(session);
                if (user == null)
                    return "User logged out";

                String name = req.queryParams("name");
                Boolean status = Boolean.valueOf(req.queryParams("status"));
                String content = req.queryParams("content");

                Book book = new Book(name, status, content);
                bookHandler.addBookToUser(user, book);
                return String.format("Added book %s (public: %s) to user %s",
                        book.name(),
                        book.status(),
                        user.username());

            } catch (Exception e) {
                res.status(400);
                return e.getMessage();
            }

        });
    }
}
