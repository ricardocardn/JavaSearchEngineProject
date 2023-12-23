package controller.bookhandler;

import model.Book;
import model.User;

public interface DatamartBookHandler {
    void addBookToUser(User user, Book book);
}
