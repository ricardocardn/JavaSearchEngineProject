package org.ulpgc.cleaner;

import org.ulpgc.cleaner.controller.Controller;
import org.ulpgc.cleaner.view.APIController;

public class Main {
    public static void main(String[] args) {
        Controller.run(args[0]);
        APIController apiController = new APIController();
        apiController.run();
    }
}
