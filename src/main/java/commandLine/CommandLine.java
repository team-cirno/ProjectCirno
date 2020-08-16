package commandLine;

import main.ServerMain;

import java.util.Scanner;

public class CommandLine extends Thread{

    Boolean alive;

    public CommandLine(){
        alive = true;
    }

    public void run() {
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
        String cmd;
        String welcomMsg =  "==========================================\r\n"+
                            String.format("Server: %s\r\n", ServerMain.serverName)+
                            String.format("Version: %s\r\n", ServerMain.version)+
                            "==========================================";

        System.out.printf(welcomMsg);
        while(alive) {
            cmd = sc.nextLine();

            String[] command = cmd.split(" ");

            //Command::help String[] command1 = command;

        }
    }

}
