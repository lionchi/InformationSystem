package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.SystemInfo;
import ru.gavrilov.entrys.CPU;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.CentralProcessor;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.util.FormatUtil;
import ru.gavrilov.util.Util;

import java.util.Arrays;

public class CpuTask extends Task<String>{

    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static final Logger LOG = LoggerFactory.getLogger(CpuTask.class);
    private static final PK pk = PK.INSTANCE;

    @Override
    protected String call() throws Exception {
        LOG.info("Checking cpu...");
        long start = System.currentTimeMillis();
        CentralProcessor processor = hardwareAbstractionLayer.getProcessor();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(processor + "\n");
        stringBuilder.append(processor.getPhysicalProcessorCount() + " физических CPU(s)" + "\n");
        stringBuilder.append(processor.getLogicalProcessorCount() + " логических CPU(s)" + "\n");

        stringBuilder.append("Идентификатор: " + processor.getIdentifier() + "\n");
        stringBuilder.append("Процессор ID: " + processor.getProcessorID() + "\n");

        pk.setCpu(new CPU(processor.getName(), processor.getLogicalProcessorCount(), processor.getPhysicalProcessorCount(),
                processor.getIdentifier(), processor.getProcessorID()));

        stringBuilder.append("Время с момента загрузки: " + FormatUtil.formatElapsedSecs(processor.getSystemUptime()) + "\n");

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        stringBuilder.append("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks) + "\n");
        // Wait
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        stringBuilder.append("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks) + "\n");

        this.printTotalCpu(stringBuilder,prevTicks, ticks);

        stringBuilder.append(String.format("CPU загрузка: %.1f%%%n", processor.getSystemCpuLoadBetweenTicks() * 100));
        //stringBuilder.append(String.format("CPU загрузка: %.1f%% (OS MXBean)%n", processor.getSystemCpuLoad() * 100));

/*        double[] loadAverage = processor.getSystemLoadAverage(3);
        stringBuilder.append(String.format("CPU среднее значение загрузки:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
                + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
                + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2]))) + "\n");*/
        // на ядро CPU
        StringBuilder procCpu = new StringBuilder("CPU загрузка процессора на ядро:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks();
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        stringBuilder.append(procCpu.toString() + "\n");
        long end = System.currentTimeMillis();
        System.out.println("Время сбора информации о центральном процессоре в милисекундах равно " + (end-start));
        return  stringBuilder.toString();
    }

    private void printTotalCpu(StringBuilder stringBuilder,long[] prevTicks,long[] ticks){
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];

        long totalCpu = user + nice + sys + idle + iowait + irq + softirq;

        stringBuilder.append(String.format(
                "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%%%n",
                100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
                100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu));
    }
}
