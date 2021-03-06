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
         * @return Целочисленный индекс этого ENUM в массивах тиков процессора,
         *, который соответствует выводам Linux / proc / cpuinfo
         * /
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
     * Возвращает «недавнее использование процессора» для всей системы путем подсчета тиков
     * из {@link #getSystemCpuLoadTicks ()} между последовательными вызовами этого метода
     * с минимальным интервалом чуть меньше 1 секунды. Если меньше одной минуты прошло
     * с момента последнего вызова этого метода, она вернет
     * расчет, основанный на подсчетах тиков и времени предыдущих двух
     * вызовов. Если прошло не менее секунды, он вернет среднию загрузка процессора
     * для интервала и обновит «последние вызванные». Этот метод
     * предназначен для периодического опроса с интервалом в 1 секунду или
     * дольше.
     *
     * @return  нагрузку процессора между 0 и 1 (100%)
     */
    double getSystemCpuLoadBetweenTicks();

    /**
     * Вернет системные счетчики загрузки CPU. Возвращает массив с семью
     * элементами, представляющие либо тики, либо миллисекунды (платформа
     * зависимый), проведенный в User (0), Nice (1), System (2), Idle (3), IOwait (4),
     * Аппаратные прерывания (IRQ) (5), Программные прерывания / DPC (SoftIRQ) (6)
     * состояниях. Используйте {@link TickType # getIndex ()} для получения соответствующего
     * индекс. Измеряя разницу между тиками по временному интервалу,
     * Нагрузка процессора на этот интервал может быть рассчитана.
     *
     * Nice и IOWait недоступны в Windows, а IOwait и
     * Информация IRQ недоступна в macOS, поэтому эти тики всегда будут
     * нуль.
     *
     * Чтобы рассчитать общее время простоя с помощью этого метода, включите как Idle, так и
     * IOWait тикает. Аналогично, IRQ, SoftIRQ ticks должны быть добавлены к
     * Системным значениям, чтобы получить общее количество.
     *
     * @return Массив из 7 длинных значений, представляющих время, проведенное в User, Nice,
     * Система, Idle, IOwait, IRQ, SoftIRQ.
     */
    long[] getSystemCpuLoadTicks();

    /**
     * Возвращает «недавнее использование процессора» для всей системы из
     * {@link com.sun.management.OperatingSystemMXBean # getSystemCpuLoad ()}, если
     * Пользователь запускает JVM Oracle. Это значение является двойным в [0.0,1.0]
     * промежуток. Значение 0.0 означает, что все процессоры неактивны в течение последних
     * наблюдаемый период времени, а значение 1.0 означает, что все ЦП были
     * активно работает в 100% случаев за последний период
     * наблюдаемый. Все значения между 0.0 и 1.0 возможны в зависимости от
     * действия, происходящие в системе. Если система недавнего использования процессора не
     * доступно, метод возвращает отрицательное значение. Вызов этого метода
     * сразу после создания экземпляра {@link CentralProcessor} может дать
     * ненадежные результаты. Если пользователь не запускает JVM Oracle, этот метод
     * будет по умолчанию изменено поведение и возвращаемое значение
     * {@link #getSystemCpuLoadBetweenTicks ()}.
     *
     * @return  «недавнее использование процессора» для всей системы; отрицательное значение, если
     *         недоступен.
     */
    @SuppressWarnings("restriction")
    double getSystemCpuLoad();

    /**
     * @return  среднюю нагрузку на систему; или отрицательное значение, если оно недоступно.
     */
    double getSystemLoadAverage();

    /**
     * Возвращает среднее значение загрузки системы для количества указанных элементов, вверх
     * до 3, представляющих 1, 5 и 15 минут. Среднее значение загрузки системы
     * сумма количества запущенных объектов, стоящих в очереди на доступные процессоры
     * и количество запущенных объектов, запущенных на доступных процессорах
     * усредненный за определенный промежуток времени. Способ усреднения нагрузки
     * рассчитана для конкретной операционной системы, но обычно является затухающей
     * зависящий от времени средний. Если среднее значение нагрузки недоступно, отрицательное
     * значение возвращается. Этот метод предназначен для пояснения
     * Системная нагрузка и может часто запрашиваться. Среднее значение нагрузки может быть
     * недоступено на некоторых платформах (например, Windows), где это сложно реализовать
     * @param nelem
     * Количество возвращаемых элементов.
     * @return  массив средних значений загрузки системы за 1, 5 и 15 минут
     * с размером массива, указанным nelem; или отрицательные значения
     * если недоступно.
     */
    double[] getSystemLoadAverage(int nelem);

    /**
     * Возвращает «недавнее  использование процессора» для всех логических процессоров путем подсчета
     * тиков для процессоров из {@link #getProcessorCpuLoadTicks ()} между
     * последовательные вызовы этого метода с минимальным интервалом немного меньше
     * более 1 секунды. Если прошло менее одной секунды со времени последнего вызова
     * этот метод, он вернет расчет, основанный на подсчетах тиков и
     * время предыдущих двух вызовов. Если прошло не менее секунды,
     * вернет среднюю нагрузку на процессор для интервала и обновит
     * «последнее время». Этот метод предназначен для периодического использования
     * опрос (итерация по всем процессорам) с интервалом в 1 секунду или
     * дольше.
     *
     * @return  массив загрузки процессора между 0 и 1 (100%) для каждого логического
     * процессор
     */
    double[] getProcessorCpuLoadBetweenTicks();

    /**
     * Получить счетчик загрузки процессора CPU. Возвращает двумерный массив,
     * с {@link #getLogicalProcessorCount ()} массивами, каждый из которых содержит семь
     * элементов, представляющих либо тики часов, либо миллисекунды (платформа
     * зависимый), проведенный в User (0), Nice (1), System (2), Idle (3), IOwait (4),
     * Аппаратные прерывания (IRQ) (5), Программные прерывания / DPC (SoftIRQ) (6)
     * состояних. Используйте {@link TickType # getIndex ()} для получения соответствующего
     * индекс. Измеряя разницу между тиками по временному интервалу,
     * Нагрузка процессора на этот интервал может быть рассчитана.
     *
     * Nice и IOwait на информацию о процессоре недоступна в Windows,
     * и информация IOwait и IRQ недоступна для macOS, поэтому эти тики
     * всегда будет равным нулю.
     *
     * Чтобы рассчитать общее время простоя с помощью этого метода, включите как Idle, так и
     * IOWait тикает. Аналогично, IRQ, SoftIRQ и Steal ticks должны быть добавлены к
     * Системное значение, чтобы получить общее количество. Системные тики также включают выполнение времени
     * другие виртуальные хосты (кража).
     *
     * @return 2D-массив значений логического ProcessorCount x 7, представляющих
     * время, проведенное в User, Nice, System, Idle, IOwait, IRQ, SoftIRQ и Steal
     *         состояниях.
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
     * @return  системный / процессорный серийный номер, если он доступен, в противном случае возвращается
     * "Неизвестный"
     *
     * @deprecated использовать {@link ComputerSystem # getSerialNumber ()} вместо этого.
     */
    @Deprecated
    String getSystemSerialNumber();

    int getLogicalProcessorCount();

    int getPhysicalProcessorCount();
}
