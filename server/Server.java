import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements Runnable{

    private ServerSocket serverSocket;
    private DataOutputStream output;
    private DataInputStream input;

    private byte[] buffer;

    private String currentAddress;

    private boolean endServerActivity;

    private final int BUFFERSIZE = 2048;
    private final int PORT = 2020;
    private final String sourcesPath = ".\\mobilesSources";

    public Server(){
        try {
            this.serverSocket = new ServerSocket(this.PORT);
            this.getLocalAddress();
            System.out.println("[!] Servidor escuchando en: " + this.currentAddress + ":" + this.PORT);
            new File(sourcesPath).mkdir();
            this.buffer = new byte[this.BUFFERSIZE];
            this.endServerActivity = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void startServer(){
        new Thread(this).start();
    }

    private void getLocalAddress() throws UnknownHostException{
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            this.currentAddress = socket.getLocalAddress().getHostAddress();
            socket.close();
        } catch (IOException e) {
            this.currentAddress = Inet4Address.getLocalHost().getHostAddress();
        } 
    }

    @Override
    public void run(){
        while(!this.endServerActivity){
            try{
                Socket clientSocket = this.serverSocket.accept();
                new Thread(){
                    public void run(){
                        try {
                            processClientRequest(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void processClientRequest(Socket clientSocket) throws IOException{
        System.out.println("[!] Procesando petición de un cliente");
        this.receiveAPK(clientSocket, this.receiveAPKHeader(clientSocket));
        System.out.println("[!] APK procesada con exito");
    }

    private void receiveAPK(Socket clientSocket, String header) throws IOException{
        String apkName = header.substring(0, header.lastIndexOf("-"));
        long fileSize = Long.parseLong(header.substring(header.lastIndexOf("-") + 1, header.length()));

        System.out.println("[Procesando la APK en: " + this.sourcesPath + "\\" + apkName);

        this.output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(this.sourcesPath + "\\" + apkName + ".apk"))));
        
        //La longitud del fichero por lo general sobrepasa al valor máximo de un entero
        int integerFileSizeValue = Integer.MAX_VALUE;
        boolean integerMaxValueExceeded = true;
        if(fileSize < Integer.MAX_VALUE){
            integerFileSizeValue = (int)(fileSize);
            integerMaxValueExceeded = false;
        }

        int bytesReaded = 0;
        while(integerFileSizeValue > 0 && (bytesReaded = this.input.read(this.buffer, 0, Math.min(this.BUFFERSIZE, integerFileSizeValue))) >= 0){
            this.output.write(this.buffer, 0, bytesReaded);
            this.output.flush();
            fileSize -= bytesReaded;

            if(integerMaxValueExceeded && fileSize < Integer.MAX_VALUE){
                integerFileSizeValue = Math.toIntExact(fileSize);
                integerMaxValueExceeded = false;
            }
            else if(!integerMaxValueExceeded){
                integerFileSizeValue -= bytesReaded;
            }
        }
    }

    private String receiveAPKHeader(Socket clientSocket) throws IOException{
        String header = "";
        this.input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        header = this.input.readUTF();

        return header;
    }

}
