package ru.gavrilov;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import ru.gavrilov.common.FileManager;
import ru.gavrilov.common.Guard;
import ru.gavrilov.common.GuiForm;
import ru.gavrilov.controllers.ErrorStartAppFormController;
import ru.gavrilov.controllers.MainController;
import ru.gavrilov.controllers.SelectDeviceController;
import ru.gavrilov.controllers.SerialNumberFormController;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.tasks.SearchUsbDeviceTask;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Executors;

public class MainApp extends Application {

    private FileManager fileManager = new FileManager();
    private boolean goodStartApp = true;
    private Stage mainStage;
    private static final PK pk = PK.INSTANCE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Security.initPropertiesHelper(MainApp.class);
        if (Security.checkFirstStart()) {
            GuiForm<AnchorPane, SelectDeviceController> loader = new GuiForm<>("select_device_form.fxml");
            AnchorPane anchorPane = loader.getParent();
            SelectDeviceController selectDeviceController = loader.getController();
            selectDeviceController.setStage(primaryStage);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(anchorPane));
            primaryStage.centerOnScreen();
            primaryStage.show();
            new Alert(Alert.AlertType.WARNING, "После выбора устройства программа необходимо перезапустить").showAndWait();
        } else {
            configPrimary(primaryStage);
            SearchUsbDeviceTask searchUsbDeviceTask = new SearchUsbDeviceTask();
            Executors.newCachedThreadPool().submit(searchUsbDeviceTask);
            searchUsbDeviceTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                try {
                    FileManager resultFileManager = searchUsbDeviceTask.getValue();
                    fileManager.setNameFolder(resultFileManager.getNameFolder());
                    fileManager.setMountPoint(resultFileManager.getMountPoint());
                    fileManager.setFolder(resultFileManager.getFolder());
                    fileManager.setFile(resultFileManager.getFile());
                } catch (Exception e) {
                    GuiForm<AnchorPane, ErrorStartAppFormController> loader = new GuiForm<>("error_start_app_form.fxml");
                    AnchorPane root = loader.getParent();
                    ErrorStartAppFormController controller = loader.getController();
                    primaryStage.initStyle(StageStyle.UNDECORATED);
                    controller.setStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                    goodStartApp = false;
                }
                if (goodStartApp) {
                    GuiForm<AnchorPane, MainController> loader = new GuiForm<>("main_form.fxml");
                    AnchorPane root = loader.getParent();
                    MainController controller = loader.getController();
                    controller.setStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                }
            });
        }
    }

    private void closeApp(WindowEvent event) {
        if (pk.canSave()) {
            exit();
            return;
        }
        Alert alertApproval = new Alert(Alert.AlertType.WARNING, "Просканированы не все аппаратные ресурсы. Результирующий файл не будет создан. " +
                "Получить информацию необходимо на всех имеющихся вкладках.");
        alertApproval.setTitle("WARNING!");
        alertApproval.setHeaderText(null);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertApproval.getButtonTypes().addAll(buttonTypeCancel);
        Optional<ButtonType> result = alertApproval.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        } else {
            event.consume();
        }
    }

    private void exit() {
        GuiForm<AnchorPane, SerialNumberFormController> loader = new GuiForm<>("serial_number_form.fxml");
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        AnchorPane root = loader.getParent();
        SerialNumberFormController controller = loader.getController();
        controller.setStage(stage);
        controller.setFileManager(fileManager);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.centerOnScreen();
        stage.show();
    }

    private void configPrimary(Stage primaryStage) {
        URL url = MainApp.class.getClassLoader().getResource("icons/market.png");
        Guard.notNull(url, "Icon file not found!");
        primaryStage.getIcons().addAll(new Image(url.toString()));
        primaryStage.setTitle("Инвентаризация аппартаного обеспечения");
        primaryStage.setOnCloseRequest(this::closeApp);
        mainStage = primaryStage;
    }
}
