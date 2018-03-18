package ru.gavrilov.hardware;

import java.io.Serializable;


public interface CentralProcessor extends Serializable {

    /**
     * Указатель счетчиков тиков процессора в {@link #getSystemCpuLoadTicks ()} и
     * {@link #getProcessorCpuLoadTicks ()} массивы.
     */
    enum TickType {
        /**
         * Использование ЦП, возникающее при выполнении на уровне пользователя
         */
        USER(0),
        /**
         * Использование ЦП, возникающее при выполнении на уровне пользователя с
         * Хороший приоритет.
         */
        NICE(1),
        /**
         * Использование ЦП, возникающее при выполнении на системном уровне
         * (ядро). Также включает время процессора, которое гипервизор предназначен для
         * другие гости в системе (кража).
         */
        SYSTEM(2),
        /**
         * Время, в течение которого CPU или CPU были бездействующими, и у системы не было
         * выдающийся запрос ввода-вывода диска.
         */
        IDLE(3),
        /**
         Время, в течение которого CPU или CPU были бездействующими, в течение которых система имела
         * выдающийся запрос ввода-вывода диска.
         */
        IOWAIT(4),
        /**
         * Время, которое процессор использовал для обслуживания аппаратных IRQ
         */
        IRQ(5),
        /**
         * Время, которое процессор использовал для обслуживания аппаратных IRQ
         */
        SOFTIRQ(6);

        private int index;

        TickType(int value) {
            this.index = value;
        }

        /**
         * @return The integer index of this ENUM in the processor tick arrays,
         *         which matches the output of Linux /proc/cpuinfo
         */
        public int getIndex() {
            return index;
        }
    }

    String getVendor();

    void setVendor(String vendor);

    String getName();

    void setName(String name);

    /**
     * Частота поставщика (в Гц), например. для процессора Intel (R) Core (TM) 2 Duo
     * CPU T7300 @ 2.00GHz частота поставщика 2000000000.
     *
     * @return Частота процессора или -1, если неизвестно.
     */
    long getVendorFreq();

    void setVendorFreq(long freq);

    String getProcessorID();

    void setProcessorID(String processorID);

    /**
     * Identifier, eg. x86 Family 6 Model 15 Stepping 10.
     *
     * @return Processor identifier.
     */
    String getIdentifier();

    void setIdentifier(String identifier);

    boolean isCpu64bit();

    void setCpu64(boolean cpu64);

    String getStepping();

    void setStepping(String stepping);

    String getModel();

    void setModel(String model);

    String getFamily();

    void setFamily(String family);

    /**
     * Returns the "recent cpu usage" for the whole system by counting ticks
     * from {@link #getSystemCpuLoadTicks()} between successive calls of this
     * method, with a minimum interval slightly less than 1 second. If less than
     * one second has elapsed since the last call of this method, it will return
     * a calculation based on the tick counts and times of the previous two
     * calls. If at least a second has elapsed, it will return the average CPU
     * load for the interval and update the "last called" times. This method is
     * intended to be used for periodic polling at intervals of 1 second or
     * longer.
     *
     * @return CPU load between 0 and 1 (100%)
     */
    double getSystemCpuLoadBetweenTicks();

    /**
     * Get System-wide CPU Load tick counters. Returns an array with seven
     * elements representing either clock ticks or milliseconds (platform
     * dependent) spent in User (0), Nice (1), System (2), Idle (3), IOwait (4),
     * Hardware interrupts (IRQ) (5), Software interrupts/DPC (SoftIRQ) (6), or Steal (7)
     * states. Use {@link TickType#getIndex()} to retrieve the appropriate
     * index. By measuring the difference between ticks across a time interval,
     * CPU load over that interval may be calculated.
     *
     * Nice and IOWait information is not available on Windows, and IOwait and
     * IRQ information is not available on macOS, so these ticks will always be
     * zero.
     *
     * To calculate overall Idle time using this method, include both Idle and
     * IOWait ticks. Similarly, IRQ, SoftIRQ, and Steal ticks should be added to the
     * System value to get the total. System ticks also include time executing
     * other virtual hosts (steal).
     *
     * @return An array of 7 long values representing time spent in User, Nice,
     *         System, Idle, IOwait, IRQ, SoftIRQ, and Steal states.
     */
    long[] getSystemCpuLoadTicks();

    /**
     * Returns the "recent cpu usage" for the whole system from
     * {@link com.sun.management.OperatingSystemMXBean#getSystemCpuLoad()} if a
     * user is running the Oracle JVM. This value is a double in the [0.0,1.0]
     * interval. A value of 0.0 means that all CPUs were idle during the recent
     * period of time observed, while a value of 1.0 means that all CPUs were
     * actively running 100% of the time during the recent period being
     * observed. All values between 0.0 and 1.0 are possible depending of the
     * activities going on in the system. If the system recent cpu usage is not
     * available, the method returns a negative value. Calling this method
     * immediately upon instantiating the {@link CentralProcessor} may give
     * unreliable results. If a user is not running the Oracle JVM, this method
     * will default to the behavior and return value of
     * {@link #getSystemCpuLoadBetweenTicks()}.
     *
     * @return the "recent cpu usage" for the whole system; a negative value if
     *         not available.
     */
    @SuppressWarnings("restriction")
    double getSystemCpuLoad();

    /**
     * Returns the system load average for the last minute. This is equivalent
     * to calling {@link CentralProcessor#getSystemLoadAverage(int)} with an
     * argument of 1 and returning the first value, and is retained for
     * compatibility.
     *
     * @return the system load average; or a negative value if not available.
     */
    double getSystemLoadAverage();

    /**
     * Returns the system load average for the number of elements specified, up
     * to 3, representing 1, 5, and 15 minutes. The system load average is the
     * sum of the number of runnable entities queued to the available processors
     * and the number of runnable entities running on the available processors
     * averaged over a period of time. The way in which the load average is
     * calculated is operating system specific but is typically a damped
     * time-dependent average. If the load average is not available, a negative
     * value is returned. This method is designed to provide a hint about the
     * system load and may be queried frequently. The load average may be
     * unavailable on some platforms (e.g., Windows) where it is expensive to
     * implement this method.
     *
     * @param nelem
     *            Number of elements to return.
     * @return an array of the system load averages for 1, 5, and 15 minutes
     *         with the size of the array specified by nelem; or negative values
     *         if not available.
     */
    double[] getSystemLoadAverage(int nelem);

    /**
     * Returns the "recent cpu usage" for all logical processors by counting
     * ticks for the processors from {@link #getProcessorCpuLoadTicks()} between
     * successive calls of this method, with a minimum interval slightly less
     * than 1 second. If less than one second has elapsed since the last call of
     * this method, it will return a calculation based on the tick counts and
     * times of the previous two calls. If at least a second has elapsed, it
     * will return the average CPU load for the interval and update the
     * "last called" times. This method is intended to be used for periodic
     * polling (iterating over all processors) at intervals of 1 second or
     * longer.
     *
     * @return array of CPU load between 0 and 1 (100%) for each logical
     *         processor
     */
    double[] getProcessorCpuLoadBetweenTicks();

    /**
     * Get Processor CPU Load tick counters. Returns a two dimensional array,
     * with {@link #getLogicalProcessorCount()} arrays, each containing seven
     * elements representing either clock ticks or milliseconds (platform
     * dependent) spent in User (0), Nice (1), System (2), Idle (3), IOwait (4),
     * Hardware interrupts (IRQ) (5), Software interrupts/DPC (SoftIRQ) (6), or Steal (7)
     * states. Use {@link TickType#getIndex()} to retrieve the appropriate
     * index. By measuring the difference between ticks across a time interval,
     * CPU load over that interval may be calculated.
     *
     * Nice and IOwait per processor information is not available on Windows,
     * and IOwait and IRQ information is not available on macOS, so these ticks
     * will always be zero.
     *
     * To calculate overall Idle time using this method, include both Idle and
     * IOWait ticks. Similarly, IRQ, SoftIRQ and Steal ticks should be added to the
     * System value to get the total. System ticks also include time executing
     * other virtual hosts (steal).
     *
     * @return A 2D array of logicalProcessorCount x 7 long values representing
     *         time spent in User, Nice, System, Idle, IOwait, IRQ, SoftIRQ, and Steal
     *         states.
     */
    long[][] getProcessorCpuLoadTicks();

    /**
     * Получить время безотказной работы системы (время с момента загрузки).
     *
     * @return Количество секунд с момента загрузки.
     */
    long getSystemUptime();

    /**
     * Получите серийный номер System / CPU, если он доступен.
     *
     * В Linux и FreeBSD для этого требуются либо корневые разрешения, либо установка
     * (устаревшая) библиотека HAL (команда lshal). Linux также пытается
     * читать файлы серийного номера dmi / id в sysfs, которые являются корнями только для чтения,
     * по умолчанию, но могут иметь разрешения, измененные пользователем.
     *
     * @ вернуть системный / процессорный серийный номер, если он доступен, в противном случае возвращается
     * "Неизвестный"
     *
     * @deprecated использовать {@link ComputerSystem # getSerialNumber ()} вместо этого.
     */
    @Deprecated
    String getSystemSerialNumber();

    int getLogicalProcessorCount();

    int getPhysicalProcessorCount();
}
