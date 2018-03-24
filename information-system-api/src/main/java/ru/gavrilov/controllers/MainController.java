package ru.gavrilov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.TabEnum;
import ru.gavrilov.common.TaskService;
import ru.gavrilov.tasks.ComputerSystemTask;
import ru.gavrilov.tasks.CpuTask;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainController implements Controller {
    private Stage stage;

    @FXML
    private TabPane tabPane;
    @FXML
    private TextArea computerSystemText;
    @FXML
    private Button computerSystemButton;
    @FXML
    private TextArea cpuText;
    @FXML
    private Button cpuButton;

    private ObservableList<Tab> tabs = FXCollections.observableArrayList();
    private List<TabEnum> tabEnumList = Arrays.asList(TabEnum.values());

    @FXML
    private void initialize(){
        this.tabs =tabPane.getTabs();
        tabPane.setOnMouseClicked(event -> getActiveTab());
        this.computerSystemButton.setOnAction(event -> {
            TaskService<ComputerSystemTask,TextArea> taskService = new TaskService<>(new ComputerSystemTask(),this.computerSystemText);
            taskService.taskExecuter();
        });
    }

    private Tab getActiveTab(){
        Tab activeTab = this.tabPane.getSelectionModel().getSelectedItem();
        Optional<TabEnum> tabEnum = this.tabEnumList.stream()
                .filter(tabEnum1 -> tabEnum1.getNameTab().equals(activeTab.getText()))
                .findFirst();
        if(tabEnum.isPresent()){
            switch(tabEnum.get()){
                case CUMPUTER_SYSTEM:
                    if (this.computerSystemButton.getOnAction() == null) {
                        this.computerSystemButton.setOnAction(event -> {
                            TaskService<ComputerSystemTask,TextArea> taskService = new TaskService<>(new ComputerSystemTask(),this.computerSystemText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case FILE_SYSTEM:
                    //TODO
                    break;
                case CPU:
                    if (this.cpuButton.getOnAction() == null) {
                        this.cpuButton.setOnAction(event -> {
                            TaskService<CpuTask,TextArea> taskService = new TaskService<>(new CpuTask(),this.cpuText);
                            taskService.taskExecuter();
                        });
                    }
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
        return activeTab;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
