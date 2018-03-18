package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.UsbDevice;

import java.util.Arrays;

public abstract class AbstractUsbDevice implements UsbDevice {

    private static final long serialVersionUID = 2L;

    protected String name;

    protected String vendor;

    protected String vendorId;

    protected String productId;

    protected String serialNumber;

    protected UsbDevice[] connectedDevices;

    public AbstractUsbDevice(String name, String vendor, String vendorId, String productId, String serialNumber,
            UsbDevice[] connectedDevices) {
        this.name = name;
        this.vendor = vendor;
        this.vendorId = vendorId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.connectedDevices = Arrays.copyOf(connectedDevices, connectedDevices.length);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVendor() {
        return this.vendor;
    }

    @Override
    public String getVendorId() {
        return this.vendorId;
    }

    @Override
    public String getProductId() {
        return this.productId;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public UsbDevice[] getConnectedDevices() {
        return Arrays.copyOf(this.connectedDevices, this.connectedDevices.length);
    }

    @Override
    public int compareTo(UsbDevice usb) {
        return getName().compareTo(usb.getName());
    }

    @Override
    public String toString() {
        return indentUsb(this, 1);
    }


    private static String indentUsb(UsbDevice usbDevice, int indent) {
        String indentFmt = indent > 2 ? String.format("%%%ds|-- ", indent - 4) : String.format("%%%ds", indent);
        StringBuilder sb = new StringBuilder(String.format(indentFmt, ""));
        sb.append(usbDevice.getName());
        if (usbDevice.getVendor().length() > 0) {
            sb.append(" (").append(usbDevice.getVendor()).append(')');
        }
        if (usbDevice.getSerialNumber().length() > 0) {
            sb.append(" [s/n: ").append(usbDevice.getSerialNumber()).append(']');
        }
        for (UsbDevice connected : usbDevice.getConnectedDevices()) {
            sb.append('\n').append(indentUsb(connected, indent + 4));
        }
        return sb.toString();
    }

}
