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
    public TreeView<HWDiskStore> treeViewDisks;
    public Button hardDisksButton;
    public TextArea networkText;
    public Button networkButton;
    public TextArea displayText;
    public Button displayButton;
    public Button videoCardButton;
    public TextArea videoCardText;

    public AnchorPane paneComputer;
    public AnchorPane paneCpu;
    public AnchorPane paneHardDisks;
    public AnchorPane paneNetwork;
    public AnchorPane paneDisplay;
    public AnchorPane paneVideoCard;


    private List<TabEnum> tabEnumList = Arrays.asList(TabEnum.values());

    @FXML
    private void initialize() {
        tabPane.setOnMouseClicked(event -> getActiveTab());
        TreeViewService.setOnMouseClickForTreeView(treeViewDisks);
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
                case CPU:
                    if (this.cpuButton.getOnAction() == null) {
                        this.cpuButton.setOnAction(event -> {
                            TaskService<CpuTask, AnchorPane> taskService = new TaskService<>(new CpuTask(), this.paneCpu);
                            taskService.taskExecuter(this.cpuText);
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
                case NETWORK:
                    if (this.networkButton.getOnAction() == null) {
                        this.networkButton.setOnAction(event -> {
                            TaskService<NetworkTask, AnchorPane> taskService = new TaskService<>(new NetworkTask(), this.paneNetwork);
                            taskService.taskExecuter(this.networkText);
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
