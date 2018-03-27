package ru.gavrilov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.GuiForm;
import ru.gavrilov.common.TabEnum;
import ru.gavrilov.common.TaskService;
import ru.gavrilov.entrys.ProcessEntry;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.tasks.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainController implements Controller {
    private Stage stage;

    public TabPane tabPane;
    public TextArea computerSystemText;
    public Button computerSystemButton;
    public TextArea cpuText;
    public Button cpuButton;
    public TreeView<String> treeViewFileSystem;
    public Button fileSystemButton;
    public TableView<ProcessEntry> processesTable;
    public TableColumn<ProcessEntry, String> pid;
    public TableColumn<ProcessEntry, String> cpu;
    public TableColumn<ProcessEntry, String> memory;
    public TableColumn<ProcessEntry, String> vsz;
    public TableColumn<ProcessEntry, String> rss;
    public TableColumn<ProcessEntry, String> name;
    public TextField memoryText;
    public TextField swapText;
    public TextField processesText;
    public TextField threadsText;
    public Button memoryButton;
    public TreeView<HWDiskStore> treeViewDisks;
    public Button hardDisksButton;
    public TextArea networkText;
    public Button networkButton;
    public TextArea sensorsAndPSText;
    public Button sensorsAndPSButton;
    public TextArea displayText;
    public Button displayButton;
    public TreeView<String> treeView;
    public Button usbDevicesButton;

    private ObservableList<Tab> tabs = FXCollections.observableArrayList();
    private ObservableList<ProcessEntry> tableModels = FXCollections.observableArrayList();
    private List<TabEnum> tabEnumList = Arrays.asList(TabEnum.values());

    @FXML
    private void initialize() {
        this.tabs = tabPane.getTabs();
        tabPane.setOnMouseClicked(event -> getActiveTab());
        this.treeViewDisks.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                try {
                    HWDiskStore diskStore = treeViewDisks.getSelectionModel().getSelectedItem().getValue();
                    GuiForm<AnchorPane, HardDisksController> form = new GuiForm<>("info_form.fxml");
                    Stage stage = new Stage(StageStyle.UTILITY);
                    AnchorPane parent = form.getParent();
                    HardDisksController controller = form.getController();
                    controller.setHardDisksController(diskStore);
                    controller.setStage(stage);
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                    stage.initModality(Modality.WINDOW_MODAL);
                    Window window =this.treeView.getScene().getWindow();
                    stage.initOwner(window);
                    stage.centerOnScreen();
                    stage.showAndWait();
                }
                catch (Exception e){
                    new Alert(Alert.AlertType.WARNING,"Двойной клик срабатывает только при нажатии на элемент дерева").showAndWait();
                }
            }
        });
        this.computerSystemButton.setOnAction(event -> {
            TaskService<ComputerSystemTask, TextArea> taskService = new TaskService<>(new ComputerSystemTask(), this.computerSystemText);
            taskService.taskExecuter();
        });
    }

    private Tab getActiveTab() {
        Tab activeTab = this.tabPane.getSelectionModel().getSelectedItem();
        Optional<TabEnum> tabEnum = this.tabEnumList.stream()
                .filter(tabEnum1 -> tabEnum1.getNameTab().equals(activeTab.getText()))
                .findFirst();
        if (tabEnum.isPresent()) {
            switch (tabEnum.get()) {
                case CUMPUTER_SYSTEM:
                    if (this.computerSystemButton.getOnAction() == null) {
                        this.computerSystemButton.setOnAction(event -> {
                            TaskService<ComputerSystemTask, TextArea> taskService = new TaskService<>(new ComputerSystemTask(), this.computerSystemText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case FILE_SYSTEM:
                    if (this.fileSystemButton.getOnAction() == null) {
                        this.fileSystemButton.setOnAction(event -> {
                            TaskService<FileSystemTask, TextArea> taskService = new TaskService<>(new FileSystemTask(), this.treeViewFileSystem);
                            taskService.taskExecuterTreeView();
                        });
                    }
                    break;
                case CPU:
                    if (this.cpuButton.getOnAction() == null) {
                        this.cpuButton.setOnAction(event -> {
                            TaskService<CpuTask, TextArea> taskService = new TaskService<>(new CpuTask(), this.cpuText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case MEMORY:
                    if (this.memoryButton.getOnAction() == null) {
                        this.memoryButton.setOnAction(event -> {
                            processesTable.getItems().clear();
                            TaskService<MemoryTask, TextField> taskServiceForMemory = new TaskService<>(new MemoryTask(), this.processesText);
                            taskServiceForMemory.taskExecuter(this.processesText, this.threadsText, this.memoryText, this.swapText);
                            TaskService<ProcessTask, TextField> taskServiceProcess = new TaskService<>(new ProcessTask(), this.processesText);
                            taskServiceProcess.taskExecuterProcess(processesTable, pid, cpu, memory, vsz, name, rss, tableModels);
                        });
                    }
                    break;
                case HARD_DISKS:
                    if (this.hardDisksButton.getOnAction() == null) {
                        this.hardDisksButton.setOnAction(event -> {
                            TaskService<HardDisksTask, TextArea> taskService = new TaskService<>(new HardDisksTask(), this.treeViewDisks);
                            taskService.taskExecuterTreeView();
                        });
                    }
                    break;
                case USB_DEVICES:
                    if (this.usbDevicesButton.getOnAction() == null) {
                        this.usbDevicesButton.setOnAction(event -> {
                            TaskService<UsbDevicesTask, TextArea> taskService = new TaskService<>(new UsbDevicesTask(), this.treeView);
                            taskService.taskExecuterTreeView();
                        });
                    }
                    break;
                case NETWORK:
                    if (this.networkButton.getOnAction() == null) {
                        this.networkButton.setOnAction(event -> {
                            TaskService<NetworkTask, TextArea> taskService = new TaskService<>(new NetworkTask(), this.networkText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case SENSORS_AND_PS:
                    if (this.sensorsAndPSButton.getOnAction() == null) {
                        this.sensorsAndPSButton.setOnAction(event -> {
                            TaskService<SensorsAndPsTask, TextArea> taskService = new TaskService<>(new SensorsAndPsTask(), this.sensorsAndPSText);
                            taskService.taskExecuter();
                        });
                    }
                    break;
                case DISPLAY:
                    if (this.displayButton.getOnAction() == null) {
                        this.displayButton.setOnAction(event -> {
                            TaskService<DisplayTask, TextArea> taskService = new TaskService<>(new DisplayTask(), this.displayText);
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
