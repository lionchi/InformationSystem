package ru.gavrilov.software.os;

import ru.gavrilov.software.OSFileStore;

import java.io.Serializable;

public interface FileSystem extends Serializable {

    /**
     * Получить файлы на этой машине
     *
     * Создает экземпляр объектов {@link OSFileStore}, представляющих
     * пул хранения, устройство, раздел, объем, конкретная файловая система или другое
     * реализация конкретных средств хранения файлов.
     *
     * @return Массив объектов OSFileStore или пустой массив, если ни один из них не является
     *         настоящее время.
     * /
     */
    OSFileStore[] getFileStores();

    /**
     * @return текущее количество открытых файловых дескрипторов.
     */
    long getOpenFileDescriptors();

    /**
     * @return  максимальное количество дескрипторов открытых файлов
     */
    long getMaxFileDescriptors();

}
