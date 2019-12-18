package server;

import java.io.IOException;
import java.net.Socket;

public class Listener extends Thread {

    private int clientNumber = 0;
    @Override
    public void run() {

        System.out.println("listener started");

        try {

            while (clientNumber < 2) {
                Socket clientSocket = Server.serverSocket.accept();
                clientNumber++;
                new Client(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //manager osztaly inditasa
        Server.manager = new Manager();
        Server.gameStart = true;
        System.out.println("listener vege, a jatek elindult");

    }
}
