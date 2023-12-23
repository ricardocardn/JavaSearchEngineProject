package controller.loaders;

import model.User;

public interface UserLoader {
    User getUser(String user, String Password);
}
