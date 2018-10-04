package ru.gavrilov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gavrilov.common.*;
import ru.gavrilov.entrys.ProcessEntry;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.software.OSFileStore;
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
    public TreeView<OSFileStore> treeViewFileSystem;
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
    public Button videoCardButton;
    public TextArea videoCardText;

    public AnchorPane paneComputer;
    public AnchorPane paneFileSystem;
    public AnchorPane paneCpu;
    public AnchorPane paneMemory;
    public AnchorPane paneHardDisks;
    public AnchorPane paneUsb;
    public AnchorPane paneNetwork;
    public AnchorPane paneSensors;
    public AnchorPane paneDisplay;
    public AnchorPane paneVideoCard;


    private ObservableList<ProcessEntry> tableModels = FXCollections.observableArrayList();
    private List<TabEnum> tabEnumList = Arrays.asList(TabEnum.values());

    @FXML
    private void initialize() {
        tabPane.setOnMouseClicked(event -> getActiveTab());
        TreeViewService.setOnMouseClickForTreeView(treeViewDisks);
        TreeViewService.setOnMouseClickForTreeView(treeViewFileSystem);
        this.computerSystemButton.setOnAction(event -> {
            TaskService<ComputerSystemTask, AnchorPane> taskService = new TaskService<>(new ComputerSystemTask(), this.paneComputer);
            taskService.taskExecuter(this.computerSystemText);
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
                            TaskService<ComputerSystemTask, AnchorPane> taskService = new TaskService<>(new ComputerSystemTask(), this.paneComputer);
                            taskService.taskExecuter(this.computerSystemText);
                        });
                    }
                    break;
                case FILE_SYSTEM:
                    if (this.fileSystemButton.getOnAction() == null) {
                        this.fileSystemButton.setOnAction(event -> {
                            TaskService<FileSystemTask, AnchorPane> taskService = new TaskService<>(new FileSystemTask(), this.paneFileSystem);
                            AnchorPane root = (AnchorPane) activeTab.getContent();
                            taskService.taskExecuterTreeView(this.treeViewFileSystem);
                        });
                    }
                    break;
                case CPU:
                    if (this.cpuButton.getOnAction() == null) {
                        this.cpuButton.setOnAction(event -> {
                            TaskService<CpuTask, AnchorPane> taskService = new TaskService<>(new CpuTask(), this.paneCpu);
                            taskService.taskExecuter(this.cpuText);
                        });
                    }
                    break;
                case PROCESS:
                    if (this.memoryButton.getOnAction() == null) {
                        this.memoryButton.setOnAction(event -> {
                            processesTable.getItems().clear();
                            TaskService<MemoryTask, AnchorPane> taskServiceForMemory = new TaskService<>(new MemoryTask(), this.paneMemory);
                            taskServiceForMemory.taskExecuter(this.processesText, this.threadsText, this.memoryText, this.swapText);
                            taskServiceForMemory.getTask().addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,event1->{
                                TaskService<ProcessTask, AnchorPane> taskServiceProcess = new TaskService<>(new ProcessTask(), this.paneMemory);
                                taskServiceProcess.taskExecuterProcess(processesTable, pid, cpu, memory, vsz, name, rss, tableModels);
                            });
                        });
                    }
                    break;
                case HDD:
                    if (this.hardDisksButton.getOnAction() == null) {
                        this.hardDisksButton.setOnAction(event -> {
                            TaskService<HardDisksTask, AnchorPane> taskService = new TaskService<>(new HardDisksTask(), this.paneHardDisks);
                            taskService.taskExecuterTreeView(this.treeViewDisks);
                        });
                    }
                    break;
                case USB_CONTROLLERS:
                    if (this.usbDevicesButton.getOnAction() == null) {
                        this.usbDevicesButton.setOnAction(event -> {
                            TaskService<UsbDevicesTask, AnchorPane> taskService = new TaskService<>(new UsbDevicesTask(), this.paneUsb);
                            taskService.taskExecuterTreeView(this.treeView);
                        });
                    }
                    break;
                case NETWORK:
                    if (this.networkButton.getOnAction() == null) {
                        this.networkButton.setOnAction(event -> {
                            TaskService<NetworkTask, AnchorPane> taskService = new TaskService<>(new NetworkTask(), this.paneNetwork);
                            taskService.taskExecuter(this.networkText);
                        });
                    }
                    break;
                case STATUS_PK:
                    if (this.sensorsAndPSButton.getOnAction() == null) {
                        this.sensorsAndPSButton.setOnAction(event -> {
                            TaskService<SensorsAndPsTask, AnchorPane> taskService = new TaskService<>(new SensorsAndPsTask(), this.paneSensors);
                            taskService.taskExecuter(this.sensorsAndPSText);
                        });
                    }
                    break;
                case VIDEO_CARD:
                    if (this.videoCardButton.getOnAction() == null) {
                        this.videoCardButton.setOnAction(event -> {
                            TaskService<VideoCardTask, AnchorPane> taskService = new TaskService<>(new VideoCardTask(), this.paneVideoCard);
                            taskService.taskExecuter(this.videoCardText);
                        });
                    }
                    break;
                case DISPLAY:
                    if (this.displayButton.getOnAction() == null) {
                        this.displayButton.setOnAction(event -> {
                            TaskService<DisplayTask, AnchorPane> taskService = new TaskService<>(new DisplayTask(), this.paneDisplay);
                            taskService.taskExecuter(this.displayText);
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
