package org.crawler.message.broker;

public interface Publisher {
    void publish(String event);
}
