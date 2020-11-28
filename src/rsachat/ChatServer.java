package rsachat;

import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class ChatServer implements Runnable {

    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
private ChatWindowServer w = null;
private String smg = " ";
private ChatWindow w2;
private String normal="";
    public ChatServer(int port) {
        try {
            
            w = new ChatWindowServer(this);
            w.setVisible(true);
            w.ShowMsg(smg);
            System.out.println("Configuracion " + port + ", Espera  ...");
            server = new ServerSocket(port);
            System.out.println("Servidor iniciado: " + server);
            start();
             
        
   
        } catch (IOException ioe) {
            System.out.println("Conexiobn fallida al puerto " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (thread != null) {
            
            try {
                System.out.println("Esperando al cliente ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println("Error en el servidor " + ioe);
                stop();
            }
        }
    }

    public void start() {
        
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
            
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public synchronized void handle(String msg, String unico) {
        RSA rsa = new RSA(10);
    
         normal = new String(msg.getBytes());    
          //console.readLine().getBytes()
                
        
        //console.readLine().getBytes() //normalna wiadomosc
        BigInteger[] TextoCifrado = rsa.cifrar(normal); //zakodowana wiadomosc
        String TextoCifrado2 = "";
        for(int i=0; i<TextoCifrado.length; i++) {
     TextoCifrado2 += TextoCifrado[i].toString(16).toUpperCase();
        }
        smg = "Texto cifrado:  " + msg;
        w.ShowMsg(smg);
        w.Obtener(unico);
        System.out.println("SMGGGGGG: "+smg);
        
        for (int i = 0; i < clientCount; i++) {
            clients[i].send(TextoCifrado.toString());
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("El cliente acepto " + socket);
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                System.out.println("Error de apertura en el hilo: " + ioe);
            }
        } else {
            System.out.println("Cliente rechazado maximo " + clients.length + " Logrado");
        }
    }

    public static void main(String args[]) {
        ChatServer server = new ChatServer(2);
    }
}
