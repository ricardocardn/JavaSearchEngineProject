package controller.sessions;

import model.User;

public interface SessionHandler {
    String getSessionToken(User user);
    User hasValidSession(String session);
}
