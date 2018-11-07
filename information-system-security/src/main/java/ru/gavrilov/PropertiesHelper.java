package ru.gavrilov;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

class PropertiesHelper {
    static Map<String, String> properties = new HashMap<>();
    private static Class clazz;
    private static byte[] bytes;

    static boolean checkFirstStart() {
        if (!checkFile()) {
            Properties properties = new Properties();
            String isFirstStart = "";
            try {
                properties.load(clazz.getResourceAsStream("/security.properties"));
                isFirstStart = properties.getProperty("first.start");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isFirstStart.equals("true");
        }
        return false;
    }

    static void saveSerialNumber(String serialNumber) throws IOException {
        Properties props = new Properties();
        InputStream inputStream = clazz.getResourceAsStream("/security.properties");
        props.load(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream);
        inputStream.close();

        props.setProperty("key.serialNumber", serialNumber);

        props.store(byteArrayOutputStream, null);
        bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
    }

    static void saveStorePath(String path) throws IOException {
        Properties props = new Properties();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        props.load(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(byteArrayInputStream, byteArrayOutputStream);
        byteArrayInputStream.close();

        props.setProperty("path.store", path);

        props.store(byteArrayOutputStream, null);
        bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
    }

    static String getProperty(String nameProperty) throws FileNotFoundException {
        Properties properties = new Properties();
        String property = "";
        try {
            properties.load(clazz.getResourceAsStream("/security.properties"));
            property = properties.getProperty(nameProperty);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    static void setFirstStartToFalse() throws IOException {
        Properties props = new Properties();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        props.load(byteArrayInputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(byteArrayInputStream, byteArrayOutputStream);
        byteArrayInputStream.close();

        props.setProperty("first.start", "false");

        props.store(byteArrayOutputStream, null);
        bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
    }

    static void createFile() throws IOException, InterruptedException {
        File file = new File("cfgis");
        file.setReadOnly();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();
        Process exec = Runtime.getRuntime().exec(String.format("attrib +H %s", file.getPath()));
        exec.waitFor();
    }


    static boolean checkFile() {
        File file = new File("cfgis");
        if (file.exists()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String temp = scanner.nextLine();
            while (scanner.hasNext()) {
                String str = scanner.next();
                String[] strings = str.split("=");
                properties.put(strings[0], strings[1]);
            }
            scanner.close();
            return true;
        } else {
            return false;
        }
    }

    static void setMainClass(Class clazz) {
        PropertiesHelper.clazz = clazz;
    }
}
