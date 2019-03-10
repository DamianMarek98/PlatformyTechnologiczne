package com.deny;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUploadController implements Initializable {
    @FXML private BorderPane mainPane;
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button uploadButton;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("ok");
        File file=new FileChooser().showOpenDialog(null);
        Task<Void> task=new UploadFileTask(file);
        statusLabel.textProperty().bind(task.messageProperty());

        progressBar.progressProperty().bind(task.progressProperty());
        executor.submit(task);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
