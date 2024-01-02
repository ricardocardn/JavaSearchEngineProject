package org.crawler.model;

import java.util.Date;

public record Book(String name, String author, String language, String date,
                   String content) {
}
