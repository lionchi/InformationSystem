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

public class MainController implements Controller {
    private Stage stage;

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
        return null;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
