package ru.gavrilov;

import ru.gavrilov.hardware.HWDiskStore;
import ru.gavrilov.hardware.HardwareAbstractionLayer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class Security {
    private static final HardwareAbstractionLayer hardwareAbstractionLayer = SystemInfo.INSTANCE.getHardware();
    private static FileSystemView fw;

    static {
        JFileChooser fr = new JFileChooser();
        fw = fr.getFileSystemView();
    }

    public static void initPropertiesHelper(Class clazz) {
        PropertiesHelper.setMainClass(clazz);
    }

    public static boolean checkFirstStart() throws FileNotFoundException {
        return PropertiesHelper.checkFirstStart();
    }

    public static void setLicenseForIS(HWDiskStore selectDisk) throws IOException, InterruptedException {
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();
        String root = fw.getDefaultDirectory().getPath() + "\\";
        for (HWDiskStore disk : diskStores) {
            if (disk.equals(selectDisk)) {
                generateFile(root, disk.getSerial().trim());
                PropertiesHelper.saveSerialNumber(disk.getSerial().trim());
                break;
            }
        }
        PropertiesHelper.setFirstStartToFalse();
        PropertiesHelper.saveStorePath(root);
    }

    public static boolean checkLicenseForIS() throws IOException, InterruptedException {
        HWDiskStore[] diskStores = hardwareAbstractionLayer.getDiskStores();
        String serialNumber = PropertiesHelper.properties.get("key.serialNumber");
        for (HWDiskStore disk : diskStores) {
            if (disk.getSerial().trim().equals(serialNumber)) {
                return isFileFound(disk.getSerial().trim());
            }
        }
        return false;
    }

    private static boolean isFileFound(String serialNumber) throws IOException {
        String path = PropertiesHelper.properties.get("path.store").replace("\\:", ":");
        File f = new File(path);
        String fileName = PropertiesHelper.getProperty("name.file");
        File[] matchingFiles = f.listFiles((dir, name) -> name.equals(fileName));

        StringBuilder builder = new StringBuilder();
        if (matchingFiles != null && matchingFiles.length > 0) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(matchingFiles[0])))) {
                int i;
                while ((i = in.read()) != -1) {
                    builder.append((char) i);
                }
            }
        }
        return builder.toString().equals(MD5.crypt(serialNumber));
    }

    private static void generateFile(String root, String serial) throws InterruptedException, IOException {
        String fileName = PropertiesHelper.getProperty("name.file");
        File file = new File(root + fileName);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            writer.write(MD5.crypt(serial));
            file.setReadOnly();
            Process exec = Runtime.getRuntime().exec(String.format("attrib +H %s", file.getPath()));
            exec.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSecurityContext() throws IOException, InterruptedException {
        PropertiesHelper.createFile();
    }

    public static String getDir() {
        return PropertiesHelper.properties.get("path.store").replace("\\:", ":");
    }
}
