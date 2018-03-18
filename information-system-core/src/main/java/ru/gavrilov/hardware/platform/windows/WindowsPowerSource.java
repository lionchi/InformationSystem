package ru.gavrilov.hardware.platform.windows;

import com.sun.jna.NativeLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.hardware.PowerSource;
import ru.gavrilov.hardware.common.AbstractPowerSource;
import ru.gavrilov.jna.platform.windows.PowrProf;
import ru.gavrilov.util.FormatUtil;

public class WindowsPowerSource extends AbstractPowerSource {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(WindowsPowerSource.class);

    public WindowsPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
        super(newName, newRemainingCapacity, newTimeRemaining);
        LOG.debug("Initialized WindowsPowerSource");
    }

    public static PowerSource[] getPowerSources() {
        // Windows provides a single unnamed battery
        String name = "System Battery";
        WindowsPowerSource[] psArray = new WindowsPowerSource[1];
        // Get structure
        PowrProf.SystemBatteryState batteryState = new PowrProf.SystemBatteryState();
        if (0 != PowrProf.INSTANCE.CallNtPowerInformation(PowrProf.SYSTEM_BATTERY_STATE, null, new NativeLong(0),
                batteryState, new NativeLong(batteryState.size())) || batteryState.batteryPresent == 0) {
            psArray[0] = new WindowsPowerSource("Unknown", 0d, -1d);
        } else {
            int estimatedTime = -2; // -1 = unknown, -2 = unlimited
            if (batteryState.acOnLine == 0 && batteryState.charging == 0 && batteryState.discharging > 0) {
                estimatedTime = batteryState.estimatedTime;
            }
            long maxCapacity = FormatUtil.getUnsignedInt(batteryState.maxCapacity);
            long remainingCapacity = FormatUtil.getUnsignedInt(batteryState.remainingCapacity);

            psArray[0] = new WindowsPowerSource(name, (double) remainingCapacity / maxCapacity, estimatedTime);
        }

        return psArray;
    }
}
