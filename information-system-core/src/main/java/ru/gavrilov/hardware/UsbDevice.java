package ru.gavrilov.hardware;

import java.io.Serializable;

public interface UsbDevice extends Serializable, Comparable<UsbDevice> {
    String getName();

    String getVendor();

    String getVendorId();

    String getProductId();

    String getSerialNumber();

    UsbDevice[] getConnectedDevices();
}
