package main;

import java.io.IOException;
import java.net.ServerSocket;

public class test {
    public static void main(String[] args) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(2048);
            socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
