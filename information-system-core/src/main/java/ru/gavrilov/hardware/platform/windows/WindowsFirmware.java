package ru.gavrilov.hardware.platform.windows;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneOffset;
import ru.gavrilov.hardware.common.AbstractFirmware;
import ru.gavrilov.util.windows.WmiUtil;

import java.util.List;
import java.util.Map;


final class WindowsFirmware extends AbstractFirmware {

    private static final long serialVersionUID = 1L;

    private static final WmiUtil.ValueType[] BIOS_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING,
            WmiUtil.ValueType.STRING, WmiUtil.ValueType.DATETIME };

    WindowsFirmware() {
        init();
    }

    private void init() {

        final Map<String, List<Object>> win32BIOS = WmiUtil.selectObjectsFrom(null, "Win32_BIOS",
                "Manufacturer,Name,Description,Version,ReleaseDate", "where PrimaryBIOS=true", BIOS_TYPES);

        final List<Object> manufacturers = win32BIOS.get("Manufacturer");
        if (manufacturers != null && manufacturers.size() == 1) {
            setManufacturer((String) manufacturers.get(0));
        }

        final List<Object> names = win32BIOS.get("Name");
        if (names != null && names.size() == 1) {
            setName((String) names.get(0));
        }

        final List<Object> descriptions = win32BIOS.get("Description");
        if (descriptions != null && descriptions.size() == 1) {
            setDescription((String) descriptions.get(0));
        }

        final List<Object> version = win32BIOS.get("Version");
        if (version != null && version.size() == 1) {
            setVersion((String) version.get(0));
        }

        final List<Object> releaseDate = win32BIOS.get("ReleaseDate");
        if (releaseDate != null && releaseDate.size() == 1) {
            setReleaseDate(Instant.ofEpochMilli((Long) releaseDate.get(0)).atZone(ZoneOffset.UTC).toLocalDate());
        }
    }
}
