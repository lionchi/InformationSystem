package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.Display;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.EdidUtil;

import java.util.ArrayList;

public class DisplayTask extends Task<String> {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(DisplayTask.class);
    private static final PK pk = PK.INSTANCE;

    @Override
    protected String call() throws Exception {
        LOG.info("Checking Displays...");
        long start = System.currentTimeMillis();
        Display[] displays = hardwareAbstractionLayer.getDisplays();
        StringBuilder stringBuilder = new StringBuilder("Дисплеи:");
        ArrayList<ru.gavrilov.entrys.Display> listDisplays = new ArrayList<>();
        int i = 1;
        for (Display display : displays) {
            stringBuilder.append("Дисплей №" + i + ":");
            stringBuilder.append(display.toString());
            i++;
            listDisplays.add(new ru.gavrilov.entrys.Display(EdidUtil.getManufId(), EdidUtil.getName(), EdidUtil.getDiagonal(), EdidUtil.getDisplayId()));
        }
        pk.setDisplays(listDisplays);
        long end = System.currentTimeMillis();
        System.out.println("Время сбора информации о мониторах подключенных к персональном компьютеру  в милисекундах равно " + (end-start));
        return stringBuilder.toString();
    }
}
