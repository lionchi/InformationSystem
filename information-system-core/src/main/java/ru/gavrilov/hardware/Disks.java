package ru.gavrilov.hardware;

import java.io.Serializable;

public interface Disks extends Serializable {

    HWDiskStore[] getDisks();
}
