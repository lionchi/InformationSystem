package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.*;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.software.OperatingSystemVersion;
import ru.gavrilov.util.FormatUtil;

public class ComputerSystemTask extends Task<String> {

    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final OperatingSystem operatingSystem = SystemInfo.INSTANCE.getOperatingSystem();
    private static final PK pk = PK.INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(ComputerSystemTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking computer system...");
        long start = System.currentTimeMillis();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
        OperatingSystemVersion operatingSystemVersion = operatingSystem.getVersion();
        GlobalMemory memory = hardwareAbstractionLayer.getMemory();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(operatingSystemVersion + "\n");
        pk.setVersionOs(operatingSystemVersion.toString());

        stringBuilder.append("Производитель: " + computerSystem.getManufacturer() + "\n");
        pk.setManufacture(computerSystem.getManufacturer());
        stringBuilder.append("Модель: " + computerSystem.getModel() + "\n");
        pk.setModelPk(computerSystem.getModel());
        stringBuilder.append("Серийный номер: " + computerSystem.getSerialNumber() + "\n");
        pk.setSerialNumberPk(computerSystem.getSerialNumber());
        stringBuilder.append("Общий объем RAM: " + FormatUtil.formatBytes(memory.getTotal()) + "\n");
        pk.setRam(FormatUtil.formatBytes(memory.getTotal()));

        Firmware firmware = computerSystem.getFirmware();

        stringBuilder.append("Bios:" + "\n");
        stringBuilder.append("  производитель: " + firmware.getManufacturer() + "\n");
        stringBuilder.append("  имя: " + firmware.getName() + "\n");
        stringBuilder.append("  описание: " + firmware.getDescription() + "\n");
        stringBuilder.append("  версия: " + firmware.getVersion() + "\n");
        pk.setVersionBios(firmware.getVersion());
        stringBuilder.append("  дата выпуска: " + (firmware.getReleaseDate() == null ? "неизвестна"
                : firmware.getReleaseDate() == null ? "неизвестна" : FormatUtil.formatDate(firmware.getReleaseDate())) + "\n");

        Baseboard baseboard = computerSystem.getBaseboard();

        stringBuilder.append("Материнская плата:" + "\n");
        stringBuilder.append("  производитель: " + baseboard.getManufacturer() + "\n");
        pk.setMotherboardManufacturer(baseboard.getManufacturer());
        stringBuilder.append("  модель: " + baseboard.getModel() + "\n");
        stringBuilder.append("  версия: " + baseboard.getVersion() + "\n");
        stringBuilder.append("  серийный номер: " + baseboard.getSerialNumber() + "\n");
        pk.setMotherboardSerialNumber(baseboard.getSerialNumber());
        long end = System.currentTimeMillis();
        System.out.println("Время сбора общей информации о персональном компьютере  в милисекундах равно " + (end-start));
        return stringBuilder.toString();
    }
}
