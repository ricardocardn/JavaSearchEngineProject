package api;

import com.google.gson.Gson;
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
        login();
        getUserName();
        postDocument();
        getDocuments();
        logout();
    }

    private void login() {
        get("user/login", (req, res) -> {
            String session = logger.logUser(req.queryParams("username"), req.queryParams("password"));
            if (session != null) {
                res.cookie("Session", session);
                return "User logged successfully";
            } else {
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
                return e.getMessage();
            }

        });
    }

    private void postDocument() {
        post("user/post", (req, res) -> {
            String session = req.cookie("Session");
            try {
                User user = sessionHandler.hasValidSession(session);
                if (user == null)
                    return "User logged out";

                String name = "u_" + user.username() + "_" + req.queryParams("name");
                Boolean status = Boolean.valueOf(req.queryParams("status"));
                String content = req.body();

                Book book = new Book(name, status, content);
                bookHandler.addBookToUser(user, book);
                return String.format("Added book %s (public: %s) to user %s",
                        book.name(),
                        book.status(),
                        user.username());

            } catch (Exception e) {
                return e.getMessage();
            }

        });
    }

    private void getDocuments() {
        get("user/books", (req, res) -> {
            String session = req.cookie("Session");
            try {
                User user = sessionHandler.hasValidSession(session);
                if (user == null)
                    return "User logged out";

                return new Gson().toJson(bookHandler.getUserBooks(user));

            } catch (Exception e) {
                return e.getMessage();
            }

        });
    }

    private void logout() {
        get("user/logout", (req, res) -> {
            res.cookie("Session","");
            return "Logged out correctly";
        });
    }
}
