package sample;

import javafx.concurrent.Task;

import javax.imageio.IIOException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class UploadFileTask extends Task<Void> {
    private File toUpload;

    public UploadFileTask(File file) {
        toUpload = file;
    }

    @Override
    protected Void call() throws Exception {
        updateMessage("Initiating...");
        updateProgress(0, 100);
        try {
            Socket server = new Socket("localhost", 1);
            if (!server.isConnected()) {
                updateMessage("Fialed to connect to the server.");
                updateProgress(100, 100);
                return null;
            }

            DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());
            Throwable var = null;

            try {
                long toUploadBytes = this.toUpload.length();
                dataOutputStream.writeUTF(this.toUpload.getName());
                dataOutputStream.writeLong(toUploadBytes);
                long readBytes = 0;
                this.updateMessage("Uploading...");
                this.updateProgress(0, toUploadBytes);
                FileInputStream fileInputStream = new FileInputStream(this.toUpload);
                byte[] buffer = new byte[512];

                while (readBytes != toUploadBytes) {
                    int read = fileInputStream.read(buffer);
                    dataOutputStream.write(buffer, 0, read);
                    readBytes += (long) read;
                    this.updateProgress(readBytes, toUploadBytes);
                }
            } catch (Throwable e) {
                var = e;
                throw e;
            } finally {
                if (dataOutputStream != null) {
                    if (var != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Throwable e) {
                            var.addSuppressed(e);
                        }
                    } else {
                        dataOutputStream.close();
                    }
                }
            }
        } catch (IIOException e) {
            this.updateMessage("Upload filed!" + e.getMessage());
        }
        this.updateMessage("File upload done!");
        this.updateProgress(0, 100);
        return null;
    }
}