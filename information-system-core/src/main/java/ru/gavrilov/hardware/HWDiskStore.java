package ru.gavrilov.hardware;

import ru.gavrilov.HWPartition;

import java.io.Serializable;
import java.util.Arrays;

public class HWDiskStore implements Serializable, Comparable<HWDiskStore> {

    private static final long serialVersionUID = 1L;

    private String model;
    private String name;
    private String serial;
    private long size;
    private long reads;
    private long readBytes;
    private long writes;
    private long writeBytes;
    private long transferTime;
    private HWPartition[] partitions;
    private long timeStamp;

    public HWDiskStore() {
        this("", "", "", 0L, 0L, 0L, 0L, 0L, 0L, new HWPartition[0], 0L);
    }

    public HWDiskStore(String name, String model, String serial, long size, long reads, long readBytes, long writes,
                       long writeBytes, long transferTime, HWPartition[] partitions, long timeStamp) {
        setName(name);
        setModel(model);
        setSerial(serial);
        setSize(size);
        setReads(reads);
        setReadBytes(readBytes);
        setWrites(writes);
        setWriteBytes(writeBytes);
        setTransferTime(transferTime);
        setPartitions(partitions);
        setTimeStamp(timeStamp);
    }

    public String getName() {
        return this.name;
    }

    public String getModel() {
        return this.model;
    }


    public String getSerial() {
        return this.serial;
    }

    public long getSize() {
        return this.size;
    }

    public long getReads() {
        return this.reads;
    }

    public long getReadBytes() {
        return this.readBytes;
    }

    public long getWrites() {
        return this.writes;
    }

    public long getWriteBytes() {
        return this.writeBytes;
    }

    public long getTransferTime() {
        return this.transferTime;
    }

    public HWPartition[] getPartitions() {
        return this.partitions;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public void setModel(String model) {
        this.model = model == null ? "" : model;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? "" : serial;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setReads(long reads) {
        this.reads = reads;
    }

    public void setReadBytes(long readBytes) {
        this.readBytes = readBytes;
    }

    public void setWrites(long writes) {
        this.writes = writes;
    }

    public void setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
    }

    public void setTransferTime(long transferTime) {
        this.transferTime = transferTime;
    }

    public void setPartitions(HWPartition[] partitions) {
        this.partitions = partitions;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(HWDiskStore store) {
        // Naturally sort by device name
        return getName().compareTo(store.getName());
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.model == null ? 0 : this.model.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + Arrays.hashCode(this.partitions);
        result = prime * result + (this.serial == null ? 0 : this.serial.hashCode());
        result = prime * result + (int) (this.size ^ this.size >>> 32);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HWDiskStore)) {
            return false;
        }
        HWDiskStore other = (HWDiskStore) obj;
        if (this.model == null) {
            if (other.model != null) {
                return false;
            }
        } else if (!this.model.equals(other.model)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (!Arrays.equals(this.partitions, other.partitions)) {
            return false;
        }
        if (this.serial == null) {
            if (other.serial != null) {
                return false;
            }
        } else if (!this.serial.equals(other.serial)) {
            return false;
        }
        return this.size == other.size;
    }

}
