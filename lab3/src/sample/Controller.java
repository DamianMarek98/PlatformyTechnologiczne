package sample;


import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller implements Initializable {
    @FXML
    Button threadsButton;
    @FXML
    Button selectFilesButton;
    @FXML
    Button selectLocationButton;
    @FXML
    Label statusLabel;
    @FXML
    TableView imagesView;
    @FXML
    TableColumn<ImageProcessingJob, String> imageNameColumn;
    @FXML
    TableColumn<ImageProcessingJob, Double> progressColumn;
    @FXML
    TableColumn<ImageProcessingJob, String> statusColumn;

    File dir = null;
    ObservableList<ImageProcessingJob> jobs;

    int threadingModel = 0;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFile().getName()));
        statusColumn.setCellValueFactory(p -> p.getValue().messageProperty());
        progressColumn.setCellFactory(ProgressBarTableCell.<ImageProcessingJob>forTableColumn());
        progressColumn.setCellValueFactory(p -> p.getValue().progressProperty().asObject());

        jobs = FXCollections.observableList(new ArrayList<>());
        imagesView.setItems(jobs);
    }

    @FXML
    private void toggleThreads(ActionEvent event) {
        threadingModel = 0;
        ChoiceBox threadingModelBox = new ChoiceBox(
                FXCollections.observableArrayList(
                        "Single thread", "Common threads pool", "2 threads", "4 threads", "8 threads"
                )
        );
        threadingModelBox.setMinWidth(250);
        VBox box = new VBox();
        box.getChildren().add(threadingModelBox);
        box.setMaxHeight(250);
        box.setPrefWidth(250);
        box.setPadding(new Insets(5, 5, 5, 5));
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Threating Model");
        alert.setHeaderText("Choose one of the following :");
        alert.getDialogPane().setContent(box);
        alert.showAndWait();

        threadingModel = threadingModelBox.getSelectionModel().getSelectedIndex();
        reloadExecutor();
    }

    @FXML
    private void selectFiles(ActionEvent event) {
        Window stage = ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG images", "*.jpg"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles == null || selectedFiles.isEmpty()) return;

        jobs.clear();
        selectedFiles.stream().forEach(file -> {
            jobs.add(new ImageProcessingJob(file));
        });
    }

    @FXML
    private void selectDirectory(ActionEvent event) {
        if (jobs.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Firstly choose some files!");
            alert.showAndWait();

            return;
        }
        Window stage = ((Node) event.getSource()).getScene().getWindow();
        dir = new DirectoryChooser().showDialog(stage);
        if (dir == null) {
            return;
        } else {
            new Thread(this::processing).start();
        }

    }

    private void processing() {
        long beginTime = System.currentTimeMillis();
        Platform.runLater(() -> {
            disableButtons(true);
            statusLabel.setText("Processing");
        });

        jobs.stream().forEach(job -> {
            job.targetDirectory = dir;
            executor.submit(job);
        });
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            long convertingTime = System.currentTimeMillis() - beginTime;
            Platform.runLater(() -> {
                statusLabel.setText("Finished, duration: " + convertingTime + " ms");
                disableButtons(false);
                reloadExecutor();
            });

        } catch (InterruptedException e) {

        }

    }

    private void reloadExecutor() {
        switch (threadingModel) {
            case 0:
                threadsButton.setText("Threads: 1");
                executor = Executors.newSingleThreadExecutor();
                break;
            case 1:
                threadsButton.setText("Common pool");
                executor = new ForkJoinPool();
                break;
            case 2:
                threadsButton.setText("Threads: 2");
                executor = Executors.newFixedThreadPool(2);
                break;
            case 3:
                threadsButton.setText("Threads: 4");
                executor = Executors.newFixedThreadPool(4);
                break;
            case 4:
                threadsButton.setText("Threads: 8");
                executor = Executors.newFixedThreadPool(8);
                break;
        }
    }

    private void disableButtons(boolean ok) {
        threadsButton.setDisable(ok);
        selectFilesButton.setDisable(ok);
        selectLocationButton.setDisable(ok);
    }
}
