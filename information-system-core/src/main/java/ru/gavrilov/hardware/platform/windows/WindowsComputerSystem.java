package ru.gavrilov.hardware.platform.windows;

import ru.gavrilov.hardware.common.AbstractComputerSystem;
import ru.gavrilov.util.windows.WmiUtil;

import java.util.List;
import java.util.Map;


final class WindowsComputerSystem extends AbstractComputerSystem {

    private static final long serialVersionUID = 1L;

    WindowsComputerSystem() {
        init();
    }

    private void init() {

        final Map<String, List<String>> win32ComputerSystem = WmiUtil.selectStringsFrom(null, "Win32_ComputerSystem",
                "Manufacturer,Model", null);

        final List<String> manufacturers = win32ComputerSystem.get("Manufacturer");
        if (manufacturers != null && !manufacturers.isEmpty()) {
            setManufacturer(manufacturers.get(0));
        }

        final List<String> models = win32ComputerSystem.get("Model");
        if (models != null && !models.isEmpty()) {
            setModel(models.get(0));
        }

        setSerialNumber(getSystemSerialNumber());

        setFirmware(new WindowsFirmware());

        setBaseboard(new WindowsBaseboard());
    }

    private String getSystemSerialNumber() {
        // This should always work
        String serialNumber = WmiUtil.selectStringFrom(null, "Win32_BIOS", "SerialNumber", "where PrimaryBIOS=true");
        // If the above doesn't work, this might
        if ("".equals(serialNumber)) {
            serialNumber = WmiUtil.selectStringFrom(null, "Win32_Csproduct", "IdentifyingNumber", null);
        }
        if ("".equals(serialNumber)) {
            serialNumber = "unknown";
        }
        return serialNumber;
    }
}
