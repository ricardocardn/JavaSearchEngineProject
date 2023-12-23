package model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userName;
    private Map<String, String> documents;

    public User(String name) {
        this.userName = name;
        documents = new HashMap<>();
    }

    public String username() {
        return userName;
    }

    public Map<String, String> document(){
        return documents;
    }

    public void setDocuments(String document, String status) {
        this.documents.put(document, status);
    }
}
