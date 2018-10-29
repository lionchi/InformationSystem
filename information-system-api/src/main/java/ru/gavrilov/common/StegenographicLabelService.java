package ru.gavrilov.common;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StegenographicLabelService {

    public static void initLabel(File file) {
        String label = getLabel();
        String labelToBinary = getLabelToBinary(label);
        String sourceText = readFile(file);
        String changeText = hidingMessage(sourceText, labelToBinary);
        try {
            writeResultInFile(changeText, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getLabel() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMHHmm");
        return simpleDateFormat.format(date);
    }

    private static String getLabelToBinary(String label) {
        return label.chars()
                .collect(StringBuilder::new,
                        (stringBuilder, value) -> stringBuilder.append(Integer.toBinaryString(value)),
                        StringBuilder::append)
                .toString();
    }

    private static String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF8"))) {
            int i;
            while ((i = in.read()) != -1) {
                stringBuilder.append((char) i);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static void writeResultInFile(String changeText, File file) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"))) {
            writer.write(changeText);
        }
    }

    private static String hidingMessage(String sourceText, String labelToBinary) {
        StringBuffer stringBuffer = new StringBuffer(sourceText);
        int count = 0;
        for (int i = 0; i < stringBuffer.length(); i++) {
            if (stringBuffer.charAt(i) == ':' && stringBuffer.charAt(i + 1) == ' ') {
                if (labelToBinary.charAt(count) == '1') {
                    stringBuffer = stringBuffer.insert(i + 2, " ");
                }
                count++;
            }
            if (count == labelToBinary.length()) {
                break;
            }
        }
        return stringBuffer.toString();
    }
}
