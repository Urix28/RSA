package rsachat;

import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import javax.swing.JOptionPane;

public class ChatClient implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    int primo = Integer.parseInt(JOptionPane.showInputDialog("¿Tamaño del número primo UwU?"));
    int tamPrimo = 10;
    private RSA rsa = new RSA(primo);
    private ChatWindow w = null;

    public ChatClient(String serverName, int serverPort) {
        w = new ChatWindow(this);
        w.setVisible(true);
        
        w.setTitle("Cliente");
        System.out.println("Haz una llamada por favor espera ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Conectado: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host desconocido: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Error inesperado: " + ioe.getMessage());
        }
    }

    ChatClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void run() {
        String normal = null;
       // BigInteger zakod = null;
        String read = null;
        while (thread != null) {
            try {
                read = console.readLine();
                normal = new String(read.getBytes());
                System.out.println("Normal: "+ normal);//console.readLine().getBytes()
                BigInteger[] TextoCifrado = rsa.cifrar(normal);
                streamOut.writeUTF(new String(TextoCifrado.toString()));
                
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Enviando error: " + ioe.getMessage());
                stop();
            }
        }
    }

    public void BntRun(String msg) {

        try {

            streamOut.writeUTF(msg);
            System.out.println("MSG: "+msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Enviando error: " + ioe.getMessage());
            stop();
        }

    }

    public void handle(String msg) {
        ChatServer cs = new ChatServer(2);
        ChatWindowServer css = new ChatWindowServer(cs);
        System.out.println("MSG2: "+msg);
        BigInteger[] TextoCifrado = null;
        String normal = null;
        String decrypt = null;
        normal = msg;
        css.Obtener(normal);
        System.out.println("Normal2: "+ normal);
        //Co robi z wiadomoscia
        TextoCifrado = rsa.cifrar(normal);
        String TextoCifrado2 = "";
        for(int i=0; i<TextoCifrado.length; i++) {
     TextoCifrado2 += TextoCifrado[i].toString(16).toUpperCase();
 
 }
        cs.handle(TextoCifrado2, normal);
        decrypt = rsa.descifrar(TextoCifrado);
        w.ShowMsg("Texto encriptado:  " + TextoCifrado2);
        w.ShowMsg("Texto enviado:  " +decrypt);
        System.out.println("Texto encriptado:  " + TextoCifrado2);
        System.out.println("Texto enviado:  " + decrypt);
    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error critico...");
        }
        client.zamknij();
        client.stop();
    }

    public static void main(String args[]) {
        ChatClient client = null;

        client = new ChatClient("localhost", 2);
    }
}
