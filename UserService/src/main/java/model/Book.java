package model;

public class Book {
    private String name;
    private String author;
    private String date;
    private String language;
    private String content;
    private Boolean PUBLIC;

    public Book(String name, String author, String date, String language, String content, Boolean PUBLIC) {
        this.name = name;
        this.author = author;
        this.date = date;
        this.language = language;
        this.content = content;
        this.PUBLIC = PUBLIC;
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

    public String author() {
        return author;
    }

    public String date() {
        return date;
    }

    public String language() {
        return language;
    }
}
