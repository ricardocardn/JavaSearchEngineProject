package model;

public class Book {
    private String name;
    private Boolean PUBLIC;
    private String content;

    public Book(String name, Boolean status, String content) {
        this.name = name;
        this.PUBLIC = status;
        this.content = content;
    }

    public String name() {
        return name;
    }

    public Boolean status() {
        return PUBLIC;
    }

    public String content() {
        return content;
    }
}
