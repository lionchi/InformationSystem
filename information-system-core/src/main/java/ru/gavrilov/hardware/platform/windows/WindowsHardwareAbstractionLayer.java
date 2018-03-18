package ru.gavrilov.hardware.platform.windows;


import ru.gavrilov.hardware.*;
import ru.gavrilov.hardware.common.AbstractHardwareAbstractionLayer;

public class WindowsHardwareAbstractionLayer extends AbstractHardwareAbstractionLayer {

    private static final long serialVersionUID = 1L;

    @Override
    public ComputerSystem getComputerSystem() {
        if (this.computerSystem == null) {
            this.computerSystem = new WindowsComputerSystem();
        }
        return this.computerSystem;
    }

    @Override
    public GlobalMemory getMemory() {
        if (this.memory == null) {
            this.memory = new WindowsGlobalMemory();
        }
        return this.memory;
    }


    @Override
    public CentralProcessor getProcessor() {
        if (this.processor == null) {
            this.processor = new WindowsCentralProcessor();
        }
        return this.processor;
    }

    @Override
    public PowerSource[] getPowerSources() {
        return WindowsPowerSource.getPowerSources();
    }

    @Override
    public HWDiskStore[] getDiskStores() {
        return new WindowsDisks().getDisks();
    }

    @Override
    public Display[] getDisplays() {
        return WindowsDisplay.getDisplays();
    }

    @Override
    public Sensors getSensors() {
        if (this.sensors == null) {
            this.sensors = new WindowsSensors();
        }
        return this.sensors;
    }

    @Override
    public NetworkIF[] getNetworkIFs() {
        return new WindowsNetworks().getNetworks();
    }

    @Override
    public UsbDevice[] getUsbDevices(boolean tree) {
        return WindowsUsbDevice.getUsbDevices(tree);
    }
}
