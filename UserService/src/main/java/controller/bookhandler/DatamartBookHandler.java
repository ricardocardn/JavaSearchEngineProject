package controller.bookhandler;

import model.Book;
import model.BookInfo;
import model.User;

import java.util.List;

public interface DatamartBookHandler {
    void addBookToUser(User user, Book book);
    List<BookInfo> getUserBooks(User user);
}
