package org.ulpgc.queryengine.controller;

import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.ReadCloud;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller() throws Exception {
        DatalakeReaderOneDrive datalakeReader = new DatalakeReaderOneDrive("C:\\Users\\Susana\\Desktop\\Universidad\\Tercero\\Primer\\BD\\JavaSearchEngine\\datalake\\ metadata",
                "datalake/content", "/datalake");
        ReadCloud.obtain_credentials();
        API.runAPI();
    }
}
