package ru.gavrilov.common;

import ru.gavrilov.HWPartition;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.hardware.HWDiskStore;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager {
    private String mountPoint;
    private String nameFolder;
    private File folder;
    private File file;
    private static final PK pk = PK.INSTANCE;

    public FileManager() {
    }

    public FileManager(String mountPoint, String nameFolder, File folder) {
        this.mountPoint = mountPoint;
        this.nameFolder = nameFolder;
        this.folder = folder;
        createFile();
    }

    private void createFile() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateAndTime = simpleDateFormat.format(date);
        File createFile = new File(folder, currentDateAndTime);
        setFile(createFile);
    }

    public void write() {
        try {
            FileOutputStream fos = new FileOutputStream(this.file);
            ObjectOutputStream serial = new ObjectOutputStream(fos);
            serial.writeObject(pk);
            serial.flush();
            serial.close();
        } catch (Exception ex) {
            System.out.println("Ошибка при сериализации объекта");
        }
    }

    public void read() {
        try {
            FileInputStream ios = new FileInputStream(this.file);
            ObjectInputStream serial = new ObjectInputStream(ios);
            PK pk = (PK) serial.readObject();
            serial.close();
        } catch (Exception ex) {
            System.out.println("Ошибка при сериализации объекта");
        }
    }

    public static boolean formattingСheck(HWDiskStore diskStore) {
        HWPartition[] partitions = diskStore.getPartitions();
        long totalSpace = 0;
        long freeSpace = 0;
        if (partitions == null) {
            return false;
        }
        for (HWPartition part : partitions) {
            if (!part.getMountPoint().isEmpty()) {
                File file = new File(part.getMountPoint());
                totalSpace += file.getTotalSpace();
                freeSpace += file.getFreeSpace();
            }
        }
        if (totalSpace == freeSpace || (totalSpace - freeSpace) <= 17000) {
            return true;
        } else {
            return false;
        }
    }

    public static void createBatch(String drive, String speed, String type, String name) {
        try {
            // Create file
            FileWriter fw = new FileWriter("Phoenix.bat");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Format /y" + " " + drive + speed + " " + type + "/v:" + name);
            bw.close();
            executeBatch();
        } catch (Exception e) {
        }
    }

    private static void executeBatch() throws InterruptedException
    {
        Process p;
        try {
            p = Runtime.getRuntime().exec("Phoenix.bat");
            p.waitFor();
            File f1 = new File("Phoenix.bat");
            f1.delete();
        } catch (IOException ex) {
        }
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}