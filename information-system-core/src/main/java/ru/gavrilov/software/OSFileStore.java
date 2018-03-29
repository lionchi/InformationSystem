package ru.gavrilov.software;

import java.io.Serializable;

public class OSFileStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String volume;

    private String logicalVolume;

    private String mount;

    private String description;

    private String fsType;

    private String uuid;

    private long usableSpace;

    private long totalSpace;

    public OSFileStore() {}

    /**
     * Создает OSFileStore с указанными параметрами.
     *
     * @param newName
     * Название файла
     * @param newVolume
     * Объем филестора
     * @param newMount
     * Точка монтирования
     * @param newDescription
     * Описание хранилища файлов
     * @param newType
     * Тип фильтра, например. FAT, NTFS, etx2, ext4 и т. Д.
     * @param newUuid
     * UUID / GUID файла filestore
     * @param newUsableSpace
     * Доступные / используемые байты
     * @param newTotalSpace
     * Всего байт
     * /
     */
    public OSFileStore(String newName, String newVolume, String newMount, String newDescription, String newType,
                       String newUuid, long newUsableSpace, long newTotalSpace) {
        setName(newName);
        setVolume(newVolume);
        setLogicalVolume("");
        setMount(newMount);
        setDescription(newDescription);
        setType(newType);
        setUUID(newUuid);
        setUsableSpace(newUsableSpace);
        setTotalSpace(newTotalSpace);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getVolume() {
        return this.volume;
    }

    /**
     * Логический том файловой системы
     *
     * Предоставляет дополнительный альтернативный идентификатор тома для файловой системы.
     * Поддерживается только в Linux, обеспечивает символическое значение через '/ dev / mapper /' (используется с файловыми системами LVM).
     *
     * @return Логический том файловой системы
     */
    public String getLogicalVolume() {
        return this.logicalVolume;
    }

    public void setVolume(String value) {
        this.volume = value;
    }


    public void setLogicalVolume(String value) {
        this.logicalVolume = value;
    }

    public String getMount() {
        return this.mount;
    }

    public void setMount(String value) {
        this.mount = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getType() {
        return this.fsType;
    }

    public void setType(String value) {
        this.fsType = value;
    }

    public String getUUID() {
        return this.uuid;
    }

    public void setUUID(String value) {
        this.uuid = value;
    }

    public long getUsableSpace() {
        return this.usableSpace;
    }

    public void setUsableSpace(long value) {
        this.usableSpace = value;
    }

    public long getTotalSpace() {
        return this.totalSpace;
    }

    public void setTotalSpace(long value) {
        this.totalSpace = value;
    }

    @Override
    public String toString() {
        return getName();
    }
}
