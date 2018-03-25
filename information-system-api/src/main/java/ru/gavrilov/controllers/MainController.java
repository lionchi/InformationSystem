package ru.gavrilov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.TabEnum;
import ru.gavrilov.common.TaskService;
import ru.gavrilov.tasks.*;

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
    @FXML
    private TextArea fileSystemText;
    @FXML
    private Button fileSystemButton;
    @FXML
    private TextArea memoryText;
    @FXML
    private Button memoryButton;
    @FXML
    private TextArea hardDisksText;
    @FXML
    private Button hardDisksButton;
    @FXML
    private TextArea networkText;
    @FXML
    private Button networkButton;
    @FXML
    private TextArea sensorsAndPSText;
    @FXML
    private Button sensorsAndPSButton;
    @FXML
    private TextArea displayText;
    @FXML
    private Button displayButton;
    @FXML
    private TextArea usbDevicesText;
    @FXML
    private Button usbDevicesButton;

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
                    if(this.fileSystemButton.getOnAction() == null){
                        this.fileSystemButton.setOnAction(event -> {
                           TaskService<FileSystemTask,TextArea> taskService  = new TaskService<>(new FileSystemTask(),this.fileSystemText);
                           taskService.taskExecuter();
                        });
                    }
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
                    if (this.memoryButton.getOnAction() == null) {
                        this.memoryButton.setOnAction(event -> {
                            TaskService<MemoryTask,TextArea> taskService = new TaskService<>(new MemoryTask(),this.memoryText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case HARD_DISKS:
                    if (this.hardDisksButton.getOnAction() == null) {
                        this.hardDisksButton.setOnAction(event -> {
                            TaskService<HardDisksTask,TextArea> taskService = new TaskService<>(new HardDisksTask(),this.hardDisksText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case USB_DEVICES:
                    if (this.usbDevicesButton.getOnAction() == null) {
                        this.usbDevicesButton.setOnAction(event -> {
                            TaskService<UsbDevicesTask,TextArea> taskService = new TaskService<>(new UsbDevicesTask(),this.usbDevicesText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case NETWORK:
                    if (this.networkButton.getOnAction() == null) {
                        this.networkButton.setOnAction(event -> {
                            TaskService<NetworkTask,TextArea> taskService = new TaskService<>(new NetworkTask(),this.networkText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case SENSORS_AND_PS:
                    if (this.sensorsAndPSButton.getOnAction() == null) {
                        this.sensorsAndPSButton.setOnAction(event -> {
                            TaskService<SensorsAndPsTask,TextArea> taskService = new TaskService<>(new SensorsAndPsTask(),this.sensorsAndPSText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case DISPLAY:
                    if (this.displayButton.getOnAction() == null) {
                        this.displayButton.setOnAction(event -> {
                            TaskService<DisplayTask,TextArea> taskService = new TaskService<>(new DisplayTask(),this.displayText);
                            taskService.taskExecuter();
                        });
                    }
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
