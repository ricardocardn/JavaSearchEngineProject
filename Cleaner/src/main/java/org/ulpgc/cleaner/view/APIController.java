package org.ulpgc.cleaner.view;

import static org.ulpgc.cleaner.view.API.*;
import static spark.Spark.port;

public class APIController {
    public void run() {
        port(8080);
        getMetadata();
        getContent();
        getRawBook();
    }
}
