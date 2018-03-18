package ru.gavrilov.hardware.platform.windows;

import com.sun.jna.platform.win32.Kernel32;
import ru.gavrilov.HWPartition;
import ru.gavrilov.hardware.Disks;
import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.util.MapUtil;
import ru.gavrilov.util.ParseUtil;
import ru.gavrilov.util.windows.WmiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowsDisks implements Disks {

    private static final long serialVersionUID = 1L;

    private static Map<String, Long> readMap = new HashMap<>();
    private static Map<String, Long> readByteMap = new HashMap<>();
    private static Map<String, Long> writeMap = new HashMap<>();
    private static Map<String, Long> writeByteMap = new HashMap<>();
    private static Map<String, Long> xferTimeMap = new HashMap<>();
    private static Map<String, Long> timeStampMap = new HashMap<>();
    private static Map<String, List<String>> driveToPartitionMap = new HashMap<>();
    private static Map<String, String> partitionToLogicalDriveMap = new HashMap<>();
    private static Map<String, HWPartition> partitionMap = new HashMap<>();

    private static final WmiUtil.ValueType[] DRIVE_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING,
            WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32 };

    private static final WmiUtil.ValueType[] READ_WRITE_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.STRING,
            WmiUtil.ValueType.UINT32, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING };

    private static final WmiUtil.ValueType[] PARTITION_TYPES = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING,
            WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.UINT32 };

    private static final Pattern DEVICE_ID = Pattern.compile(".*\\.DeviceID=\"(.*)\"");

    private static final int BUFSIZE = 255;

    @Override
    public HWDiskStore[] getDisks() {
        List<HWDiskStore> result;
        result = new ArrayList<>();
        populateReadWriteMaps();
        populatePartitionMaps();

        Map<String, List<Object>> vals = WmiUtil.selectObjectsFrom(null, "Win32_DiskDrive",
                "Name,Manufacturer,Model,SerialNumber,Size,Index", null, DRIVE_TYPES);
        for (int i = 0; i < vals.get("Name").size(); i++) {
            HWDiskStore ds = new HWDiskStore();
            ds.setName((String) vals.get("Name").get(i));
            ds.setModel(String.format("%s %s", vals.get("Model").get(i), vals.get("Manufacturer").get(i)).trim());
            // Most vendors store serial # as a hex string; convert
            ds.setSerial(ParseUtil.hexStringToString((String) vals.get("SerialNumber").get(i)));
            String index = vals.get("Index").get(i).toString();
            ds.setReads(MapUtil.getOrDefault(readMap, index, 0L));
            ds.setReadBytes(MapUtil.getOrDefault(readByteMap, index, 0L));
            ds.setWrites(MapUtil.getOrDefault(writeMap, index, 0L));
            ds.setWriteBytes(MapUtil.getOrDefault(writeByteMap, index, 0L));
            ds.setTransferTime(MapUtil.getOrDefault(xferTimeMap, index, 0L));
            ds.setTimeStamp(MapUtil.getOrDefault(timeStampMap, index, 0L));
            // If successful this line is the desired value
            ds.setSize(ParseUtil.parseLongOrDefault((String) vals.get("Size").get(i), 0L));
            // Get partitions
            List<HWPartition> partitions = new ArrayList<>();
            List<String> partList = driveToPartitionMap.get(ds.getName());
            if (partList != null && !partList.isEmpty()) {
                for (String part : partList) {
                    partitions.add(partitionMap.get(part));
                }
            }
            ds.setPartitions(partitions.toArray(new HWPartition[partitions.size()]));
            // Add to list
            result.add(ds);
        }
        return result.toArray(new HWDiskStore[result.size()]);
    }

    private void populateReadWriteMaps() {
        readMap.clear();
        readByteMap.clear();
        writeMap.clear();
        writeByteMap.clear();
        xferTimeMap.clear();
        timeStampMap.clear();
        // Although the field names say "PerSec" this is the Raw Data from which
        // the associated fields are populated in the Formatted Data class, so
        // in fact this is the data we want
        Map<String, List<Object>> vals = WmiUtil.selectObjectsFrom(null, "Win32_PerfRawData_PerfDisk_PhysicalDisk",
                "Name,DiskReadsPerSec,DiskReadBytesPerSec,DiskWritesPerSec,DiskWriteBytesPerSec,PercentDiskTime,Timestamp_Sys100NS",
                null, READ_WRITE_TYPES);
        for (int i = 0; i < vals.get("Name").size(); i++) {
            String name = ParseUtil.whitespaces.split((String) vals.get("Name").get(i))[0];
            readMap.put(name, (long) vals.get("DiskReadsPerSec").get(i));
            readByteMap.put(name, ParseUtil.parseLongOrDefault((String) vals.get("DiskReadBytesPerSec").get(i), 0L));
            writeMap.put(name, (long) vals.get("DiskWritesPerSec").get(i));
            writeByteMap.put(name, ParseUtil.parseLongOrDefault((String) vals.get("DiskWriteBytesPerSec").get(i), 0L));
            // Units are 100-ns, divide to get ms
            xferTimeMap.put(name,
                    ParseUtil.parseLongOrDefault((String) vals.get("PercentDiskTime").get(i), 0L) / 10000L);
            timeStampMap.put(name,
                    ParseUtil.parseLongOrDefault((String) vals.get("Timestamp_Sys100NS").get(i), 0L) / 10000L);
        }
    }

    private void populatePartitionMaps() {
        driveToPartitionMap.clear();
        partitionToLogicalDriveMap.clear();
        partitionMap.clear();
        // For Regexp matching DeviceIDs
        Matcher mAnt;
        Matcher mDep;

        // Map drives to partitions
        Map<String, List<String>> partitionQueryMap = WmiUtil.selectStringsFrom(null, "Win32_DiskDriveToDiskPartition",
                "Antecedent,Dependent", null);
        for (int i = 0; i < partitionQueryMap.get("Antecedent").size(); i++) {
            mAnt = DEVICE_ID.matcher(partitionQueryMap.get("Antecedent").get(i));
            mDep = DEVICE_ID.matcher(partitionQueryMap.get("Dependent").get(i));
            if (mAnt.matches() && mDep.matches()) {
                MapUtil.createNewListIfAbsent(driveToPartitionMap, mAnt.group(1).replaceAll("\\\\\\\\", "\\\\"))
                        .add(mDep.group(1));
            }
        }

        // Map partitions to logical disks
        partitionQueryMap = WmiUtil.selectStringsFrom(null, "Win32_LogicalDiskToPartition", "Antecedent,Dependent",
                null);
        for (int i = 0; i < partitionQueryMap.get("Antecedent").size(); i++) {
            mAnt = DEVICE_ID.matcher(partitionQueryMap.get("Antecedent").get(i));
            mDep = DEVICE_ID.matcher(partitionQueryMap.get("Dependent").get(i));
            if (mAnt.matches() && mDep.matches()) {
                partitionToLogicalDriveMap.put(mAnt.group(1), mDep.group(1) + "\\");
            }
        }

        // Next, get all partitions and create objects
        final Map<String, List<Object>> hwPartitionQueryMap = WmiUtil.selectObjectsFrom(null, "Win32_DiskPartition",
                "Name,Type,Description,DeviceID,Size,DiskIndex,Index", null, PARTITION_TYPES);
        for (int i = 0; i < hwPartitionQueryMap.get("Name").size(); i++) {
            String deviceID = (String) hwPartitionQueryMap.get("DeviceID").get(i);
            String logicalDrive = MapUtil.getOrDefault(partitionToLogicalDriveMap, deviceID, "");
            String uuid = "";
            if (!logicalDrive.isEmpty()) {
                // Get matching volume for UUID
                char[] volumeChr = new char[BUFSIZE];
                Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint(logicalDrive, volumeChr, BUFSIZE);
                uuid = ParseUtil.parseUuidOrDefault(new String(volumeChr).trim(), "");
            }
            partitionMap.put(deviceID,
                    new HWPartition((String) hwPartitionQueryMap.get("Name").get(i),
                            (String) hwPartitionQueryMap.get("Type").get(i),
                            (String) hwPartitionQueryMap.get("Description").get(i), uuid,
                            ParseUtil.parseLongOrDefault((String) hwPartitionQueryMap.get("Size").get(i), 0L),
                            ((Long) hwPartitionQueryMap.get("DiskIndex").get(i)).intValue(),
                            ((Long) hwPartitionQueryMap.get("Index").get(i)).intValue(), logicalDrive));
        }
    }
}
