package ru.gavrilov.software.common;

import ru.gavrilov.software.OSProcess;
import ru.gavrilov.software.OperatingSystem;
import ru.gavrilov.software.OperatingSystemVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractOperatingSystem implements OperatingSystem {

    private static final long serialVersionUID = 1L;

    protected String manufacturer;
    protected String family;
    protected OperatingSystemVersion version;

    /*
     * Comparators for use in processSort()
     */
    private static final Comparator<OSProcess> CPU_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Double.compare((p2.getKernelTime() + p2.getUserTime()) / (double) p2.getUpTime(),
                    (p1.getKernelTime() + p1.getUserTime()) / (double) p1.getUpTime());
        }
    };
    private static final Comparator<OSProcess> RSS_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p2.getResidentSetSize(), p1.getResidentSetSize());
        }
    };
    private static final Comparator<OSProcess> UPTIME_DESC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p2.getUpTime(), p1.getUpTime());
        }
    };
    private static final Comparator<OSProcess> UPTIME_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Long.compare(p1.getUpTime(), p2.getUpTime());
        }
    };
    private static final Comparator<OSProcess> PID_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Integer.compare(p1.getProcessID(), p2.getProcessID());
        }
    };
    private static final Comparator<OSProcess> PARENTPID_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return Integer.compare(p1.getParentProcessID(), p2.getParentProcessID());
        }
    };
    private static final Comparator<OSProcess> NAME_ASC_SORT = new Comparator<OSProcess>() {
        @Override
        public int compare(OSProcess p1, OSProcess p2) {
            return p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
        }
    };

    @Override
    public OperatingSystemVersion getVersion() {
        return this.version;
    }

    @Override
    public String getFamily() {
        return this.family;
    }

    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    /**
     * Сортирует массив процессов, используя указанную сортировку, возвращая
     * массив с верхним лимитом, если положительный.
     *
     * @param processes
     * Массив для сортировки
     * @param limit
     * Число результатов, возвращаемых положительно; если возвращается нуль
     * все результаты
     * @param sort
     * Сортировка для использования или null
     * @return Массив ограничения размера (если положительный) или всех процессов, отсортированных
     *         как указано
     */
    protected List<OSProcess> processSort(List<OSProcess> processes, int limit, ProcessSort sort) {
        if (sort != null) {
            switch (sort) {
            case CPU:
                Collections.sort(processes, CPU_DESC_SORT);
                break;
            case MEMORY:
                Collections.sort(processes, RSS_DESC_SORT);
                break;
            case OLDEST:
                Collections.sort(processes, UPTIME_DESC_SORT);
                break;
            case NEWEST:
                Collections.sort(processes, UPTIME_ASC_SORT);
                break;
            case PID:
                Collections.sort(processes, PID_ASC_SORT);
                break;
            case PARENTPID:
                Collections.sort(processes, PARENTPID_ASC_SORT);
                break;
            case NAME:
                Collections.sort(processes, NAME_ASC_SORT);
                break;
            default:
                throw new IllegalArgumentException("Unimplemented enum type: " + sort.toString());
            }
        }
        // Return max of limit or process size
        // Nonpositive limit means return all
        int maxProcs = processes.size();
        if (limit > 0 && maxProcs > limit) {
            maxProcs = limit;
        }
        List<OSProcess> procs = new ArrayList<>();
        for (int i = 0; i < maxProcs; i++) {
            procs.add(processes.get(i));
        }
        return procs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getManufacturer()).append(' ').append(getFamily()).append(' ').append(getVersion().toString());
        return sb.toString();
    }
}
