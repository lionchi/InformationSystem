package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.hardware.UsbDevice;

public class UsbDevicesTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(UsbDevicesTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking USB Devices...");
        UsbDevice[] usbDevices = hardwareAbstractionLayer.getUsbDevices(true);
        StringBuilder stringBuilder = new StringBuilder("USB Devices:");

        for (UsbDevice usbDevice : usbDevices) {
            stringBuilder.append(usbDevice.toString());
        }

        return stringBuilder.toString();
    }
}
