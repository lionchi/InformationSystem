package ru.gavrilov.hardware.platform.windows;

import ru.gavrilov.hardware.common.AbstractBaseboard;
import ru.gavrilov.util.windows.WmiUtil;

import java.util.List;
import java.util.Map;

public class WindowsBaseboard extends AbstractBaseboard {

    private static final long serialVersionUID = 1L;

    WindowsBaseboard() {
        init();
    }

    private void init() {

        final Map<String, List<String>> win32BaseBoard = WmiUtil.selectStringsFrom(null, "Win32_BaseBoard",
                "Manufacturer,Model,Version,SerialNumber", null);

        final List<String> baseboardManufacturers = win32BaseBoard.get("Manufacturer");
        if (baseboardManufacturers != null && !baseboardManufacturers.isEmpty()) {
            setManufacturer(baseboardManufacturers.get(0));
        }

        final List<String> baseboardModels = win32BaseBoard.get("Model");
        if (baseboardModels != null && !baseboardModels.isEmpty()) {
            setModel(baseboardModels.get(0));
        }

        final List<String> baseboardVersions = win32BaseBoard.get("Version");
        if (baseboardVersions != null && !baseboardVersions.isEmpty()) {
            setVersion(baseboardVersions.get(0));
        }

        final List<String> baseboardSerials = win32BaseBoard.get("SerialNumber");
        if (baseboardSerials != null && !baseboardSerials.isEmpty()) {
            setSerialNumber(baseboardSerials.get(0));
        }
    }
}
