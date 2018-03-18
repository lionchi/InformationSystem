package ru.gavrilov;

import com.sun.jna.Platform;
import ru.gavrilov.hardware.HardwareAbstractionLayer;
import ru.gavrilov.hardware.platform.windows.WindowsHardwareAbstractionLayer;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.software.os.windows.WindowsOperatingSystem;

import java.io.Serializable;

public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private OperatingSystem os = null;

    private HardwareAbstractionLayer hardware = null;

    // The platform isn't going to change, and making this static enables easy
    // access from outside this class
    private static final PlatformEnum currentPlatformEnum;

    static {
        if (Platform.isWindows()) {
            currentPlatformEnum = PlatformEnum.WINDOWS;
        } else {
            currentPlatformEnum = PlatformEnum.UNKNOWN;
        }
    }


    public static PlatformEnum getCurrentPlatformEnum() {
        return currentPlatformEnum;
    }


    public OperatingSystem getOperatingSystem() {
        if (this.os == null) {
            switch (currentPlatformEnum) {
                case WINDOWS:
                    this.os = new WindowsOperatingSystem();
                    break;
                default:
                    throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
            }
        }
        return this.os;
    }

    public HardwareAbstractionLayer getHardware() {
        if (this.hardware == null) {
            switch (currentPlatformEnum) {

                case WINDOWS:
                    this.hardware = new WindowsHardwareAbstractionLayer();
                    break;
                default:
                    throw new UnsupportedOperationException("Operating system not supported: " + Platform.getOSType());
            }
        }
        return this.hardware;
    }
}
