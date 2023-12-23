package controller.bookhandler;

import com.mongodb.client.model.Filters;
import controller.MongoConnection;
import model.Book;
import model.BookInfo;
import model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public record MongoBookHandler(MongoConnection datamartConnection) implements DatamartBookHandler {
    @Override
    public void addBookToUser(User user, Book book) {
        Document userDocument = datamartConnection.collection().find(Filters.eq("username", user.username())).first();

        if (userDocument != null) {
            List<Document> existingDocuments = getDocuments(userDocument);
            checkIfExists(book, existingDocuments);
            setNewBook(user, book, existingDocuments);
        }
    }

    @Override
    public List<BookInfo> getUserBooks(User user) {
        Document userDocument = datamartConnection.collection().find(Filters.eq("username", user.username())).first();

        List<BookInfo> userBooks = new ArrayList<>();
        if (userDocument != null) {
            List<Document> existingDocuments = getDocuments(userDocument);
            for (Document existingDocument : existingDocuments) {
                userBooks.add(new BookInfo(
                        (String) existingDocument.get("name"),
                        (Boolean) existingDocument.get("status")
                ));
            }
        }

        return userBooks;
    }

    private void checkIfExists(Book book, List<Document> existingDocuments) {
        for (Document existingDocument : existingDocuments) {
            if (existingDocument.get("name").equals(book.name()))
                throw new RuntimeException("Book already exists");
        }
    }

    private void setNewBook(User user, Book book, List<Document> existingDocuments) {
        Document newDocument = new Document()
                .append("name", book.name())
                .append("status", book.status())
                        .append("content", book.content());

        existingDocuments.add(newDocument);

        datamartConnection.collection().updateOne(
                Filters.eq("username", user.username()),
                new Document("$set", new Document("documents", existingDocuments))
        );
    }

    private List<Document> getDocuments(Document userDocument) {
        List<Document> existingDocuments = (List<Document>) userDocument.get("documents");
        System.out.println(existingDocuments);
        if (existingDocuments == null) {
            existingDocuments = new ArrayList<>();
        }
        return existingDocuments;
    }
}
