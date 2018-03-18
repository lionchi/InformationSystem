package ru.gavrilov.hardware;

import java.io.Serializable;

public interface HardwareAbstractionLayer extends Serializable {

    ComputerSystem getComputerSystem();

    CentralProcessor getProcessor();

    GlobalMemory getMemory();

    PowerSource[] getPowerSources();

    HWDiskStore[] getDiskStores();

    NetworkIF[] getNetworkIFs();

    Display[] getDisplays();

    Sensors getSensors();

    UsbDevice[] getUsbDevices(boolean tree);
}
