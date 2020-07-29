package main;

import logger.Logger;

public class ServerLauncher {

    public ServerLauncher(){

    }



    public static void main(String[] args) {
        Logger logger = new Logger(new ServerLauncher());
        logger.log("Starting Launch Process...");
        var server = new ServerMain();
        server.init();
    }
}
