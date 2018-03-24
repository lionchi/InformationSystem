package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.Baseboard;
import ru.gavrilov.hardware.ComputerSystem;
import ru.gavrilov.hardware.Firmware;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.FormatUtil;

public class ComputerSystemTask extends Task<String> {

    protected static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    protected static final Logger LOG = LoggerFactory.getLogger(ComputerSystemTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking computer system...");

        final ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("manufacturer: " + computerSystem.getManufacturer() + "\n");
        stringBuilder.append("model: " + computerSystem.getModel() + "\n");
        stringBuilder.append("serialnumber: " + computerSystem.getSerialNumber() + "\n");

        final Firmware firmware = computerSystem.getFirmware();

        stringBuilder.append("firmware:" + "\n");
        stringBuilder.append("  manufacturer: " + firmware.getManufacturer() + "\n");
        stringBuilder.append("  name: " + firmware.getName() + "\n");
        stringBuilder.append("  description: " + firmware.getDescription() + "\n");
        stringBuilder.append("  version: " + firmware.getVersion() + "\n");
        stringBuilder.append("  release date: " + (firmware.getReleaseDate() == null ? "unknown"
                : firmware.getReleaseDate() == null ? "unknown" : FormatUtil.formatDate(firmware.getReleaseDate())) + "\n");

        final Baseboard baseboard = computerSystem.getBaseboard();

        stringBuilder.append("baseboard:" + "\n");
        stringBuilder.append("  manufacturer: " + baseboard.getManufacturer() + "\n");
        stringBuilder.append("  model: " + baseboard.getModel() + "\n");
        stringBuilder.append("  version: " + baseboard.getVersion() + "\n");
        stringBuilder.append("  serialnumber: " + baseboard.getSerialNumber() + "\n");

        return stringBuilder.toString();
    }
}
