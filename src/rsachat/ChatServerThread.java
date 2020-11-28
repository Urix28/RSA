package rsachat;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;

    public ChatServerThread(ChatServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }

    public void send(String msg) {
        try {
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(ID + " Enviar error: " + ioe.getMessage());
            stop();
        }
    }

    public int getID() {
        return ID;
    }

    public void run() {
        System.out.println("Hilo del servidor " + ID + "trabajos.");
        while (true) {
            try {
                server.handle(streamIn.readUTF(),streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(ID + " Error de lectura: " + ioe.getMessage());

                stop();
            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void zamknij() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}
