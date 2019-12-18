package client;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendData {

    private ObjectOutputStream objectOutputStream;

    public SendData(ObjectOutputStream o) {
        this.objectOutputStream = o;
    }

    synchronized void sendData(Object evenType, Object array) {

        try {

            objectOutputStream.writeObject(evenType);
            objectOutputStream.writeObject(array);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("data sent");
    }

    synchronized void sendEndRequest() {
        new Thread(() -> {
            try {

                objectOutputStream.writeObject(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

