package api;

import com.google.gson.Gson;
import controller.connections.MongoConnection;
import controller.bookhandler.DatamartBookHandler;
import controller.bookhandler.MongoBookHandler;
import controller.loggers.MongoUserLogger;
import controller.loggers.UserLogger;
import controller.registers.MongoUserRegister;
import controller.registers.UserRegister;
import controller.sessions.SessionHandler;
import controller.sessions.cookie.SessionHazelcastHandler;
import model.Book;
import model.User;

import javax.jms.JMSException;

import static spark.Spark.*;

public class APIController {
    private final UserLogger logger;
    private final UserRegister register;
    private final DatamartBookHandler bookHandler;
    private final SessionHandler sessionHandler;

    public APIController() throws JMSException {
        MongoConnection mongoConnection = new MongoConnection();
        logger = new MongoUserLogger(mongoConnection);
        register = new MongoUserRegister(mongoConnection);
        bookHandler = new MongoBookHandler(mongoConnection);
        sessionHandler = new SessionHazelcastHandler();
    }

    public void run() {
        port(8080);
        login();
        signUp();
        getUserName();
        postDocument();
        getDocuments();
        logout();
    }

    private void login() {
        get("user/login", (req, res) -> {
            try {
                String session = logger.logUser(req.queryParams("username"), req.queryParams("password"));
                res.cookie("Session", session);
                return "User logged successfully";
            } catch (Exception e) {
                return e.getMessage();
            }
        });
    }

    private void signUp() {
        get("user/sign-up", (req, res) -> {
            try {
                String session = register.register(req.queryParams("username"), req.queryParams("password"));
                res.cookie("Session", session);
                return "User logged successfully";
            } catch (Exception e) {
                return e.getMessage();
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
                String author = user.username();
                String language = req.queryParams("language");
                String date = req.queryParams("date");
                Boolean status = Boolean.valueOf(req.queryParams("status"));
                String content = req.body();

                Book book = new Book(name, author, date, language, content, status);
                bookHandler.addBookToUser(user, book);
                return String.format("Added book %s (public: %s) from user %s",
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
