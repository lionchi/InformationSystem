package ru.gavrilov.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.gavrilov.common.Controller;
import ru.gavrilov.common.TabEnum;
import ru.gavrilov.common.TaskService;
import ru.gavrilov.common.TreeViewService;
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
                        this.computerSystemButton.setOnAction(event -> runTaskServiceOfCs());
                    }
                    if (this.computerSystemText.getText().isEmpty()) {
                        runTaskServiceOfCs();
                    }
                    break;
                case CPU:
                    if (this.cpuButton.getOnAction() == null) {
                        this.cpuButton.setOnAction(event -> runTaskServiceOfCPU());
                    }
                    if (this.cpuText.getText().isEmpty()) {
                        runTaskServiceOfCPU();
                    }
                    break;
                case HDD:
                    if (this.hardDisksButton.getOnAction() == null) {
                        this.hardDisksButton.setOnAction(event -> runTaskServiceOfHd());
                    }
                    if (this.treeViewDisks.getRoot() == null) {
                        runTaskServiceOfHd();
                    }
                    break;
                case NETWORK:
                    if (this.networkButton.getOnAction() == null) {
                        this.networkButton.setOnAction(event -> runTaskServiceOfNetwork());
                    }
                    if (this.networkText.getText().isEmpty()) {
                        runTaskServiceOfNetwork();
                    }
                    break;
                case VIDEO_CARD:
                    if (this.videoCardButton.getOnAction() == null) {
                        this.videoCardButton.setOnAction(event -> runTaskServiceOfVc());
                    }
                    if (this.videoCardText.getText().isEmpty()) {
                        runTaskServiceOfVc();
                    }
                    break;
                case DISPLAY:
                    if (this.displayButton.getOnAction() == null) {
                        this.displayButton.setOnAction(event -> runTaskServiceOfDisplay());
                    }
                    if (this.displayText.getText().isEmpty()) {
                        runTaskServiceOfDisplay();
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

    private void runTaskServiceOfCs() {
        TaskService<ComputerSystemTask, AnchorPane> taskService = new TaskService<>(new ComputerSystemTask(), this.paneComputer);
        taskService.taskExecuter(this.computerSystemText);
    }

    private void runTaskServiceOfCPU() {
        TaskService<CpuTask, AnchorPane> taskService = new TaskService<>(new CpuTask(), this.paneCpu);
        taskService.taskExecuter(this.cpuText);
    }

    private void runTaskServiceOfHd() {
        TaskService<HardDisksTask, AnchorPane> taskServiceOfHd = new TaskService<>(new HardDisksTask(), this.paneHardDisks);
        taskServiceOfHd.taskExecuterTreeView(this.treeViewDisks);
    }

    private void runTaskServiceOfNetwork() {
        TaskService<NetworkTask, AnchorPane> taskServiceOfNetwork = new TaskService<>(new NetworkTask(), this.paneNetwork);
        taskServiceOfNetwork.taskExecuter(this.networkText);
    }

    private void runTaskServiceOfVc() {
        TaskService<VideoCardTask, AnchorPane> taskServiceOfVc = new TaskService<>(new VideoCardTask(), this.paneVideoCard);
        taskServiceOfVc.taskExecuter(this.videoCardText);
    }

    private void runTaskServiceOfDisplay() {
        TaskService<DisplayTask, AnchorPane> taskServiceOfDisplay = new TaskService<>(new DisplayTask(), this.paneDisplay);
        taskServiceOfDisplay.taskExecuter(this.displayText);
    }

}
