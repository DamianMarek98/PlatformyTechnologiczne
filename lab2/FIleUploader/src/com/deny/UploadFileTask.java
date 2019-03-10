package com.deny;

import javafx.concurrent.Task;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadFileTask extends Task<Void> {
    private File toUpload;

    public UploadFileTask(File file){
        toUpload=file;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Initiating...");
        updateProgress(0,toUpload.length());

        try (ServerSocket serverSocket = new ServerSocket(1500)){
            while (true){
                final Socket socket = serverSocket.accept();
                return null;
            }

        }

    }
}
