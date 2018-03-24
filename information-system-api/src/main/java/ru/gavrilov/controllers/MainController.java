package ru.gavrilov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.TabEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainController implements Controller {
    private Stage stage;
    private List<TabEnum> tabEnumList = Arrays.asList(TabEnum.values());

    @FXML
    private TabPane tabPane;
    @FXML
    private TextField computerSystemText;
    @FXML
    private Button computerSystemButton;

    private ObservableList<Tab> tabs = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        tabs =tabPane.getTabs();
        tabPane.setOnMouseClicked(event -> getActiveTab());
    }

    private Tab getActiveTab(){
        Tab activeTab = tabPane.getSelectionModel().getSelectedItem();
        Optional<TabEnum> tabEnum = tabEnumList.stream()
                .filter(tabEnum1 -> tabEnum1.getNameTab().equals(activeTab.getText()))
                .findFirst();
        if(tabEnum.isPresent()){
            switch(tabEnum.get()){
                case CUMPUTER_SYSTEM:
                    //TODO
                    break;
                case FILE_SYSTEM:
                    //TODO
                    break;
                case CPU:
                    //TODO
                    break;
                case MEMORY:
                    //TODO
                    break;
                case HARD_DISKS:
                    //TODO
                    break;
                case USB_DEVICES:
                    //TODO
                    break;
                case NETWORK:
                    //TODO
                    break;
                case SENSORS:
                    //TODO
                    break;
                case DISPLAY:
                    //TODO
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
