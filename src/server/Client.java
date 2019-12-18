package server; /**
 * Klienseket kezelo szalak
 */

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


class Client extends Thread {

    static List<Client> clients = new LinkedList<>();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;
    private int id;
    private Integer[] array;

    Client(Socket socket) throws IOException {

        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());


        //ha nincs mar szabad hely akkor visszautasitja a kapcsolatot
        if (clients.size() >= Server.MAX_NUMBER_OF_PLAYERS) {
            objectOutputStream.writeObject(false);
            System.out.println("TOO MANY PLAYERS");

            //kulonben keres egy szabad id-t
        } else {

            boolean used;

            for (int i = 1; i <= Server.MAX_NUMBER_OF_PLAYERS; i++) {
                used = false;

                for (Client c : clients) {
                    if (c.getClientId() == i) {
                        used = true;
                        break;
                    }
                }
                if (!used) {
                    this.id = i;
                    break;
                }
            }

            clients.add(this);

            //ha mar van ket jatekos akkor az ujaknak kuldi a labirintust
            if (Server.gameStart) {
                server.Server.manager.sendDataToNewClient(id);
            }
        }
    }

    @Override
    public void run() {

        Object o;
        String eventType;
        try {
            while (!interrupted()) {


                o = objectInputStream.readObject();

                //lecsatlakozasi kerelem, leveszi a tablarol a jatekost, bontja a kapcsolatot, leallitja a szalat
                if (o.getClass() == Boolean.class) {

                    server.Server.manager.closeConnection(id);

                } else {

                    eventType = (String) o;

                    if (eventType.equalsIgnoreCase("key")) {

                        array = (Integer[]) objectInputStream.readObject();
                        server.Server.manager.validateStep(id, array[0], array[1], array[2], array[3]);
                    } else {

                        array = (Integer[]) objectInputStream.readObject();
                        server.Server.manager.validateClick(id, array[0], array[1]);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Client " + id + " thread ended");
    }

    synchronized ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    synchronized ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    synchronized Socket getSocket() {
        return socket;
    }

    synchronized int getClientId() {
        return id;
    }
}
