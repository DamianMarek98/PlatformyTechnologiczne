package com.deny;

import java.io.IOException;
import java.net.Socket;

import static java.lang.System.out;
import static java.lang.System.in;

public class FIleReciver {
    private static int port = 1000;

    public static void reciveFile(Socket client) throws IOException{
        String name;
        Long size;

        byte[] buffer = new byte[4096];
        int readSize;

        while ((readSize = in.read(buffer)) != -1) {
            out.write(buffer, 0, readSize);
        }
    }
}
