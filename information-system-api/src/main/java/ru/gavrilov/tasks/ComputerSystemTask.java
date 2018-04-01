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

    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(ComputerSystemTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking computer system...");

        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Производитель: " + computerSystem.getManufacturer() + "\n");
        stringBuilder.append("Модель: " + computerSystem.getModel() + "\n");
        stringBuilder.append("Серийный номер: " + computerSystem.getSerialNumber() + "\n");

        Firmware firmware = computerSystem.getFirmware();

        stringBuilder.append("Прошивка:" + "\n");
        stringBuilder.append("  производитель: " + firmware.getManufacturer() + "\n");
        stringBuilder.append("  имя: " + firmware.getName() + "\n");
        stringBuilder.append("  описание: " + firmware.getDescription() + "\n");
        stringBuilder.append("  версия: " + firmware.getVersion() + "\n");
        stringBuilder.append("  дата выпуска: " + (firmware.getReleaseDate() == null ? "неизвестна"
                : firmware.getReleaseDate() == null ? "неизвестна" : FormatUtil.formatDate(firmware.getReleaseDate())) + "\n");

        Baseboard baseboard = computerSystem.getBaseboard();

        stringBuilder.append("Материнская плата:" + "\n");
        stringBuilder.append("  производитель: " + baseboard.getManufacturer() + "\n");
        stringBuilder.append("  модель: " + baseboard.getModel() + "\n");
        stringBuilder.append("  версия: " + baseboard.getVersion() + "\n");
        stringBuilder.append("  серийный номер: " + baseboard.getSerialNumber() + "\n");

        return stringBuilder.toString();
    }
}
