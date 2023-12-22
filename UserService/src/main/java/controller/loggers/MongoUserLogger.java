package controller.loggers;

import controller.sessions.cookie.SessionHazelcastHandler;
import controller.sessions.SessionHandler;
import model.User;

public class MongoUserLogger implements UserLogger{
    private final SessionHandler sessionHandler;

    public MongoUserLogger() {
        this.sessionHandler = new SessionHazelcastHandler();
    }

    @Override
    public String logUser(String username, String password) {
        User user = saveToDatamart(username, password);
        return sessionHandler.getSessionToken(user);
    }

    private User saveToDatamart(String username, String password) {
        return new User(username);
    }
}
