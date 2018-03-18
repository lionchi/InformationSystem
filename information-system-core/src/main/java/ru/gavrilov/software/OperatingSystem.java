package ru.gavrilov.software;

import ru.gavrilov.software.os.FileSystem;

import java.io.Serializable;

public interface OperatingSystem extends Serializable {
    enum ProcessSort {
        CPU, MEMORY, OLDEST, NEWEST, PID, PARENTPID, NAME
    }

    String getFamily();

    String getManufacturer();

    OperatingSystemVersion getVersion();

    FileSystem getFileSystem();

    OSProcess[] getProcesses(int limit, ProcessSort sort);

    OSProcess getProcess(int pid);

    int getProcessId();

    int getProcessCount();

    int getThreadCount();

    NetworkParams getNetworkParams();
}
