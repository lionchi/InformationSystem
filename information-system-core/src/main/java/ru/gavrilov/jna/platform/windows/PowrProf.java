package ru.gavrilov.jna.platform.windows;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.List;

public interface PowrProf extends Library {
    PowrProf INSTANCE = Native.loadLibrary("PowrProf", PowrProf.class);

    int SYSTEM_BATTERY_STATE = 5;

    class SystemBatteryState extends Structure {
        public byte acOnLine; // boolean

        public byte batteryPresent; // boolean

        public byte charging; // boolean

        public byte discharging; // boolean

        public byte[] spare1 = new byte[4]; // unused

        public int maxCapacity; // unsigned 32 bit

        public int remainingCapacity; // unsigned 32 bit

        public int rate; // signed 32 bit

        public int estimatedTime; // signed 32 bit

        public int defaultAlert1; // unsigned 32 bit

        public int defaultAlert2; // unsigned 32 bit

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] { "acOnLine", "batteryPresent", "charging", "discharging", "spare1",
                    "maxCapacity", "remainingCapacity", "rate", "estimatedTime", "defaultAlert1", "defaultAlert2" });
        }
    }

    int CallNtPowerInformation(int informationLevel, Pointer lpInputBuffer, NativeLong nInputBufferSize,
                               Structure lpOutputBuffer, NativeLong nOutputBufferSize);
}
