package ru.gavrilov;

import java.io.Serializable;

/**
 * Область на жестком диске или другом вторичном хранилище
 */
public class HWPartition implements Serializable, Comparable<HWPartition> {

    private static final long serialVersionUID = 1L;

    private String identification;
    private String name;
    private String type;
    private String uuid;
    private long size;
    private int major;
    private int minor;
    private String mountPoint;


    public HWPartition(String identification, String name, String type, String uuid, long size, int major, int minor,
                       String mountPoint) {
        setIdentification(identification);
        setName(name);
        setType(type);
        setUuid(uuid);
        setSize(size);
        setMajor(major);
        setMinor(minor);
        setMountPoint(mountPoint);
    }

    public HWPartition() {
        this("", "", "", "", 0L, 0, 0, "");
    }

    public String getIdentification() {
        return this.identification;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getUuid() {
        return this.uuid;
    }

    public long getSize() {
        return this.size;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getMountPoint() {
        return this.mountPoint;
    }

    public void setIdentification(String identification) {
        this.identification = identification == null ? "" : identification;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? "" : uuid;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }


    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint == null ? "" : mountPoint;
    }


    @Override
    public int compareTo(HWPartition part) {
        // Naturally sort by device ID
        return getIdentification().compareTo(part.getIdentification());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.identification == null ? 0 : this.identification.hashCode());
        result = prime * result + this.major;
        result = prime * result + this.minor;
        result = prime * result + (this.mountPoint == null ? 0 : this.mountPoint.hashCode());
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + (int) (this.size ^ this.size >>> 32);
        result = prime * result + (this.type == null ? 0 : this.type.hashCode());
        result = prime * result + (this.uuid == null ? 0 : this.uuid.hashCode());
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
        if (!(obj instanceof HWPartition)) {
            return false;
        }
        HWPartition other = (HWPartition) obj;
        if (this.identification == null) {
            if (other.identification != null) {
                return false;
            }
        } else if (!this.identification.equals(other.identification)) {
            return false;
        }
        if (this.major != other.major) {
            return false;
        }
        if (this.minor != other.minor) {
            return false;
        }
        if (this.mountPoint == null) {
            if (other.mountPoint != null) {
                return false;
            }
        } else if (!this.mountPoint.equals(other.mountPoint)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!this.type.equals(other.type)) {
            return false;
        }
        if (this.uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!this.uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

}
