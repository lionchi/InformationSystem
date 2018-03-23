package ru.gavrilov;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.gavrilov.common.Guard;
import ru.gavrilov.common.GuiForm;
import ru.gavrilov.controllers.MainController;

import java.net.URL;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        configPrimary(primaryStage);

        GuiForm<AnchorPane, MainController> loader = new GuiForm("main_form.fxml");
        AnchorPane root = loader.getParent();
        MainController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void closeApp() {
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
