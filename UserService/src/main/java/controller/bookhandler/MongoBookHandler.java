package controller.bookhandler;

import com.mongodb.client.model.Filters;
import controller.connections.MongoConnection;
import controller.http.connections.ConnectionHandler;
import controller.http.connections.HTTPConnectionHandler;
import model.Book;
import model.BookInfo;
import model.User;
import org.bson.Document;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MongoBookHandler implements DatamartBookHandler {
    private final MongoConnection datamartConnection;
    private final ConnectionHandler httpConnectionHandler;
    private final String SERVER_API_URL;

    public MongoBookHandler(MongoConnection datamartConnection) throws JMSException {
        this.datamartConnection = datamartConnection;
        this.httpConnectionHandler = new HTTPConnectionHandler();
        this.SERVER_API_URL = System.getenv("SERVER_API_URL");
    }

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
                        (String) existingDocument.get("author"),
                        (String) existingDocument.get("date"),
                        (String) existingDocument.get("language"),
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
        httpConnectionHandler.makeUrlRequest("post", book);

        Document newDocument = new Document()
                .append("name", book.name())
                .append("author", book.name())
                .append("date", book.name())
                .append("language", book.name())
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MongoBookHandler) obj;
        return Objects.equals(this.datamartConnection, that.datamartConnection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datamartConnection);
    }

    @Override
    public String toString() {
        return "MongoBookHandler[" +
                "datamartConnection=" + datamartConnection + ']';
    }

}
