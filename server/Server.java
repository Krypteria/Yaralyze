import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private DataOutputStream output;
    private DataInputStream input;

    private byte[] buffer;

    private boolean endServerActivity;

    private final int BUFFERSIZE = 2048;
    private final int PORT = 2020;

    public Server(){
        try {
            this.serverSocket = new ServerSocket(this.PORT);
            this.buffer = new byte[this.BUFFERSIZE];
            this.endServerActivity = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public void run(){
        while(!this.endServerActivity){
            try{
                Socket clientSocket = this.serverSocket.accept();
                new Thread(){
                    public void run(){
                        processClientRequest(clientSocket);
                    }
                }.start();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void processClientRequest(Socket clientSocket){
        this.receiveAPK(clienSocket);
    }

    private void receiveAPK(Socket clienSocket){

    }

}
