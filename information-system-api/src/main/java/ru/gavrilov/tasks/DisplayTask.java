package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.hardware.Display;
import ru.gavrilov.hardware.HardwareAbstractionLayer;

public class DisplayTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(DisplayTask.class);

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Displays...");
        Display[] displays = hardwareAbstractionLayer.getDisplays();
        StringBuilder stringBuilder = new StringBuilder("Displays:");
        int i = 1;
        for (Display display : displays) {
            stringBuilder.append(" Display " + i + ":");
            stringBuilder.append(display.toString());
            i++;
        }

        return stringBuilder.toString();
    }
}
