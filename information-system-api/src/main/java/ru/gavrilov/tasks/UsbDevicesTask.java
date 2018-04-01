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
        LOG.info("Checking USB controllers..");
        TreeItem rootItem = new TreeItem("Контроллеры USB");

        rootItem.getChildren().addAll(getContent());

        return rootItem;
    }


    private List<TreeItem> getContent() {
        List<TreeItem> contents = new ArrayList<>();
        UsbDevice[] usbDevices = hardwareAbstractionLayer.getUsbDevices(true);

        for (UsbDevice usbDevice : usbDevices) {
            List<String> strings = Arrays.asList(usbDevice.toString().split("\n"));
            TreeItem treeItem = new TreeItem(strings.get(0));
            setTreeItemChildren(treeItem, strings.stream().map(UsbDevicesTask::replace).collect(Collectors.toList()), 2);
            contents.add(treeItem);
        }

        return contents;
    }

    private void setTreeItemChildren(TreeItem treeItem, List<String> strings, int countSpace) {
        if (countSpace > 10) {
            return;
        }

        List<TreeItem> contentChildrens = new ArrayList<>();
        List<String> newStrings = strings.stream().filter(s -> countSpace(s) == countSpace).collect(Collectors.toList());

        for (int i = 0; i < newStrings.size(); i++) {
            TreeItem childrenItem = new TreeItem(newStrings.get(i));
            int newCountSpace = countSpace + 4;
            setTreeItemChildren(childrenItem, strings, newCountSpace);
/*            if (!isExist(i,strings)){
                contentChildrens.remove(childrenItem);
            }*/
            contentChildrens.add(childrenItem);
        }
        treeItem.getChildren().addAll(contentChildrens);
    }


    private static String replace(String str) {
        return str.replace("|--", "");
    }

    private static String replaceSpace(String str) {
        return str.replaceAll("^\\s*", "");
    }

    private int countSpace(String str) {
        return str.length() - str.replaceAll("^\\s*", "").length();
    }

    private boolean isExist(int currentPosition, List<String> strings) {
        String currentStr = strings.get(currentPosition);
        String previousStr = currentPosition + 1 < strings.size() - 1 ? strings.get(currentPosition + 1) : "";

        if (previousStr.equals("")) {
            return false;
        }

        int currentCountSpace = this.countSpace(currentStr);
        int previousCountSpace = this.countSpace(previousStr);

        return previousCountSpace < currentCountSpace;
    }
}
