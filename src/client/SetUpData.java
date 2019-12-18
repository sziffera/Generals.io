package client;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SetUpData extends Task<Void> {


    private Main main;
    private Integer[][] fieldType;
    private Integer[][] points;
    private Integer[][] players;
    private Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private GeneralsPane generalsPane;
    private final Button resultLabel;
    private SendData sendData;
    private Boolean connectionAccept;
    private Integer rows, columns, id, x, y;


    SetUpData(Main main, Button button) {


        this.resultLabel = button;
        try {
            socket = new Socket("localhost", Server.PORT_NUMBER);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.main = main;
    }

    @Override
    protected Void call() throws Exception {


        connectionAccept = (Boolean) objectInputStream.readObject();
        if (connectionAccept == false) {


        } else {

            id = (Integer) objectInputStream.readObject();
            x = (Integer) objectInputStream.readObject();
            y = (Integer) objectInputStream.readObject();
            rows = (Integer) objectInputStream.readObject();
            columns = (Integer) objectInputStream.readObject();
            players = (Integer[][]) objectInputStream.readObject();
            points = (Integer[][]) objectInputStream.readObject();
            fieldType = (Integer[][]) objectInputStream.readObject();

            System.out.println("SetDataOk");

        }

        return null;
    }

    @Override
    protected void succeeded() {



        sendData = new SendData(objectOutputStream);

        //ha betelt a szabad helyek szama akkor ujra lehet probalkozni
        if (connectionAccept == false) {
            resultLabel.setText("You can't connect, too many players. Click to retry");
            resultLabel.setOnAction(event ->
            {
                Thread thread = new Thread(new SetUpData(main, resultLabel));
                thread.setDaemon(true);
                thread.start();
            });

        } else {

            //ha nem akkor felepiti a jatekot
            main.setSendData(sendData);
            generalsPane = new GeneralsPane(main, rows, columns, points, players, fieldType, x, y);
            main.setGeneralsPane(generalsPane);
            main.setId(id);
            System.out.println(id + " ID");
            Receiver receiver = new Receiver(main, generalsPane, objectInputStream);
            main.getMediaPlayer().stop();
            main.setReceiver(receiver);
            receiver.start();
            main.changePane(generalsPane);

        }
    }
}
