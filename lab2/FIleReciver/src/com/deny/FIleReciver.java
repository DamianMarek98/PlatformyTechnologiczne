package com.deny;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;
import static java.lang.System.in;

public class FIleReciver {
    private static int port = 1;

    public static void reciveFile(Socket client) throws IOException{
        String name;
        Long size;

        DataInputStream dis = new DataInputStream(client.getInputStream());
        name = dis.readUTF();
        size = dis.readLong();


        File file = new File(".", name);
        out.println("Downloading "+ name + "size: " + size +" bytes");
        //downloading starts here

        FileOutputStream fos = new FileOutputStream(file);
        try{
            int fullRead = 0, read=0;
            byte[] buffer = new byte[512];

            while(fullRead<size){
                read = dis.read(buffer);

                if(read != -1){
                    fullRead += read;
                } else {
                  throw new IOException("Couldn't read whole file!");
                }
                fos.write(buffer, 0, read);
            }
            out.println("Downloading file: "+ name+" finished!");
            fos.close();
        }catch (IOException e){
            out.println("Error");
            file.delete();
        }
    }

    public static void main(String[] args) throws IOException{
        port=1;

        ExecutorService executor = Executors.newFixedThreadPool(16);

        try (ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                final Socket socket = serverSocket.accept();
                executor.submit(()->{
                    try {
                        FIleReciver.reciveFile(socket);
                    }catch (IOException e){
                        out.println("Problem while downloading "+ e.getMessage());
                    }
                });
            }
        }catch (IOException e){
            out.println("Problem while accepting new clients.");
            System.exit(1);
        }
    }


}
