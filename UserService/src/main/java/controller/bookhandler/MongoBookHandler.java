package controller.bookhandler;

import com.mongodb.client.model.Filters;
import controller.MongoConnection;
import model.Book;
import model.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public record MongoBookHandler(MongoConnection datamartConnection) implements DatamartBookHandler {
    @Override
    public void addBookToUser(User user, Book book) {
        Document userDocument = datamartConnection.collection().find(Filters.eq("username", user.username())).first();
        System.out.println(userDocument.get("username"));

        if (userDocument != null) {
            List<Document> existingDocuments = getDocuments(userDocument);
            checkIfExists(book, existingDocuments);
            setNewBook(user, book, existingDocuments);
        }
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
                .append("status", book.status());

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
