package server;


import java.io.IOException;
import java.net.Socket;

public class Listener2 extends Thread {

    private Manager manager;

    Listener2(Manager manager) {

        this.manager = manager;
    }

    @Override
    public void run() {

        System.out.println("listener started");

        try {

            while (!interrupted()) {

                Socket clientSocket = Server.serverSocket.accept();

                new Client(clientSocket).start();

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("listener2 vege");
    }
}
