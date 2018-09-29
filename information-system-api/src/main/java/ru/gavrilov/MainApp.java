package ru.gavrilov;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.common.Guard;
import ru.gavrilov.common.GuiForm;
import ru.gavrilov.controllers.MainController;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.tasks.SearchUsbDeviceTask;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Executors;

public class MainApp extends Application {

    private FileManager fileManager = new FileManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        configPrimary(primaryStage);

        SearchUsbDeviceTask searchUsbDeviceTask = new SearchUsbDeviceTask();
        Executors.newCachedThreadPool().submit(searchUsbDeviceTask);
        searchUsbDeviceTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            FileManager resultFileManager = searchUsbDeviceTask.getValue();
            fileManager.setNameFolder(resultFileManager.getNameFolder());
            fileManager.setMountPoint(resultFileManager.getMountPoint());
            fileManager.setFolder(resultFileManager.getFolder());
            fileManager.setFile(resultFileManager.getFile());

            GuiForm<AnchorPane, MainController> loader = new GuiForm("main_form.fxml");
            AnchorPane root = loader.getParent();
            MainController controller = loader.getController();
            controller.setStage(primaryStage);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.show();
        });
    }

    private void closeApp() {
        fileManager.write();
        //fileManager.read();
        Platform.exit();
        System.exit(0);
    }

    private void configPrimary(Stage primaryStage) {
        URL url = MainApp.class.getClassLoader().getResource("icons/market.png");
        Guard.notNull(url, "Icon file not found!");
        primaryStage.getIcons().addAll(new Image(url.toString()));
        primaryStage.setTitle("Информация о системе");
        primaryStage.setOnCloseRequest(event -> closeApp());
    }
}
