package model;

public class BookInfo {
    private String name;
    private String author;
    private String date;
    private String language;
    private Boolean PUBLIC;

    public BookInfo(String name, String author, String date, String language, Boolean PUBLIC) {
        this.name = name;
        this.author = author;
        this.date = date;
        this.language = language;
        this.PUBLIC = PUBLIC;
    }
}
