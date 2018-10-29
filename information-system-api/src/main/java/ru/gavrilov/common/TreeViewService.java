package ru.gavrilov.common;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.gavrilov.controllers.InformationController;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.software.OSFileStore;

public final class TreeViewService {

    public static <T, C> void setOnMouseClickForTreeView(TreeView<T> treeView) {
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                try {
                    C cl = (C) treeView.getSelectionModel().getSelectedItem().getValue();
                    GuiForm<AnchorPane, InformationController> form = new GuiForm<>(getFileName(cl));
                    Stage stage = new Stage(StageStyle.TRANSPARENT);
                    AnchorPane parent = form.getParent();
                    InformationController controller = form.getController();
                    applayMethodSet(cl, controller).applay(cl);
                    controller.setStage(stage);
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.initModality(Modality.WINDOW_MODAL);
                    Window window = treeView.getScene().getWindow();
                    stage.initOwner(window);
                    stage.centerOnScreen();
                    stage.showAndWait();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.WARNING,
                            "Двойной клик срабатывает только при нажатии на элемент дерева").showAndWait();
                }
            }
        });
    }

    private static <C> String getFileName(C clazz) {
        if (clazz instanceof HWDiskStore) {
            return "hardDisks_form.fxml";
        } /*else if (clazz instanceof OSFileStore) {
            return "fileStore_form.fxml";
        }*/
        return "";
    }

    private static <C> SiFunction<C> applayMethodSet(C cl, InformationController informationController) {
        SiFunction<C> function = null;
        if (cl instanceof HWDiskStore) {
            function = obj -> informationController.setHardDisksController(obj);
        } /*else if (cl instanceof OSFileStore) {
            function = obj -> informationController.setFileStore(obj);
        }*/

        return function;
    }
}
