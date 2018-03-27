package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.hardware.UsbDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsbDevicesTask extends Task<TreeItem> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(UsbDevicesTask.class);

    @Override
    protected TreeItem call() throws Exception {
        LOG.info("Checking USB Devices...");
        TreeItem rootItem = new TreeItem("USB Devices");

        rootItem.getChildren().addAll(getContent());

        return rootItem;
    }


    private List<TreeItem> getContent() {
        List<TreeItem> contents = new ArrayList<>();
        for(UsbDevice usbDevice : hardwareAbstractionLayer.getUsbDevices(true)){
            List<String> strings = Arrays.asList(usbDevice.toString().split("\n"));
            TreeItem treeItem = new TreeItem(strings.get(0));
            setTreeItemChildren(treeItem,strings.stream().map(UsbDevicesTask::replace).collect(Collectors.toList()), 1);
            contents.add(treeItem);
        }

        return contents;
    }

    private void setTreeItemChildren(TreeItem treeItem, List<String> strings, int start) {
        if(treeItem==null || start > strings.size()){
            return;
        }
        List<TreeItem> contentChildrens = new ArrayList<>();
        for (int i = start; i<strings.size(); i++) {
            TreeItem childrenItem = new TreeItem(strings.get(i));
            start = start + 1;
            setTreeItemChildren(childrenItem, strings,start);
            contentChildrens.add(childrenItem);
        }
        treeItem.getChildren().addAll(contentChildrens);
    }

    private static String replace(String str){
        return str.replace("|--", "").replaceAll("\\s*","");
    }
}
