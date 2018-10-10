package ru.gavrilov.common;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyHHmm");
        String currentDateAndTime = simpleDateFormat.format(date);
        return currentDateAndTime;
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
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String content = scanner.nextLine();
                stringBuilder.append(content);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static void writeResultInFile(String changeText, File file) throws IOException {
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(changeText);
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
