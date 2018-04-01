package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.hardware.PowerSource;
import ru.gavrilov.hardware.Sensors;

import java.util.Arrays;

public class SensorsAndPsTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(SensorsAndPsTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Sensors...");
        Sensors sensors = hardwareAbstractionLayer.getSensors();
        StringBuilder stringBuilder = new StringBuilder("Датчики:");

        stringBuilder.append(String.format("CPU температура: %.1f°C%n", sensors.getCpuTemperature()));
        stringBuilder.append(String.format("Скорость вентилятора: " + Arrays.toString(sensors.getFanSpeeds())) + "\n");
        stringBuilder.append(String.format("CPU вольтаж: %.1fV%n", sensors.getCpuVoltage()));

        stringBuilder.append(getPowerSources());

        return stringBuilder.toString();
    }

    private String getPowerSources() {
        LOG.info("Checking Power sources...");
        PowerSource[] powerSources = hardwareAbstractionLayer.getPowerSources();
        StringBuilder powerSourcesBuilder = new StringBuilder("Мощность: ");
        if (powerSources.length == 0) {
            powerSourcesBuilder.append("Неизвестна");
        } else {
            double timeRemaining = powerSources[0].getTimeRemaining();
            if (timeRemaining < -1d) {
                powerSourcesBuilder.append("Зарядка");
            } else if (timeRemaining < 0d) {
                powerSourcesBuilder.append("Вычисление оставшегося времени");
            } else {
                powerSourcesBuilder.append(String.format("%d:%02d осталось", (int) (timeRemaining / 3600),
                        (int) (timeRemaining / 60) % 60));
            }
        }
        for (PowerSource pSource : powerSources) {
            powerSourcesBuilder.append(String.format("%n%s @ %.1f%%%n", pSource.getName(), pSource.getRemainingCapacity() * 100d));
        }
        return powerSourcesBuilder.toString();
    }
}
