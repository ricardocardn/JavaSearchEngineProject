package model;

public class BookInfo {
    private String name;
    private Boolean PUBLIC;

    public BookInfo(String name, Boolean status) {
        this.name = name;
        this.PUBLIC = status;
    }

    public String name() {
        return name;
    }

    public Boolean status() {
        return PUBLIC;
    }
}
