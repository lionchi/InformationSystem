package ru.gavrilov.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class EdidUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EdidUtil.class);

    private EdidUtil() {
    }

    /**
     * Converts a byte array to a hexadecimal string
     *
     * @param edid
     *            The EDID byte array
     * @return A string representation of the byte array
     */
    public static String toHexString(byte[] edid) {
        return DatatypeConverter.printHexBinary(edid);
    }

    public static String getManufacturerID(byte[] edid) {
        // Bytes 8-9 are manufacturer ID in 3 5-bit characters.
        String temp = String
                .format("%8s%8s", Integer.toBinaryString(edid[8] & 0xFF), Integer.toBinaryString(edid[9] & 0xFF))
                .replace(' ', '0');
        LOG.debug("Manufacurer ID: {}", temp);
        return String.format("%s%s%s", (char) (64 + Integer.parseInt(temp.substring(1, 6), 2)),
                (char) (64 + Integer.parseInt(temp.substring(7, 11), 2)),
                (char) (64 + Integer.parseInt(temp.substring(12, 16), 2))).replace("@", "");
    }

    public static String getProductID(byte[] edid) {
        // Bytes 10-11 are product ID expressed in hex characters
        return Integer.toHexString(
                ByteBuffer.wrap(Arrays.copyOfRange(edid, 10, 12)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xffff);
    }

    public static String getSerialNo(byte[] edid) {
        // Bytes 12-15 are Serial number (last 4 characters)
        LOG.debug("Serial number: {}", Arrays.toString(Arrays.copyOfRange(edid, 12, 16)));
        return String.format("%s%s%s%s", getAlphaNumericOrHex(edid[15]), getAlphaNumericOrHex(edid[14]),
                getAlphaNumericOrHex(edid[13]), getAlphaNumericOrHex(edid[12]));
    }

    private static String getAlphaNumericOrHex(byte b) {
        return Character.isLetterOrDigit((char) b) ? String.format("%s", (char) b) : String.format("%02X", b);
    }

    public static byte getWeek(byte[] edid) {
        return edid[16];
    }

    public static int getYear(byte[] edid) {
        byte temp = edid[17];
        LOG.debug("Year-1990: {}", temp);
        return temp + 1990;
    }

    public static String getVersion(byte[] edid) {
        return edid[18] + "." + edid[19];
    }

    public static boolean isDigital(byte[] edid) {
        return 1 == (edid[20] & 0xff) >> 7;
    }

    /**
     * Get monitor width in cm
     *
     * @param edid
     *            The EDID byte array
     * @return Monitor width in cm
     */
    public static int getHcm(byte[] edid) {
        // Byte 21 is horizontal size in cm
        return edid[21];
    }

    /**
     * Get monitor height in cm
     *
     * @param edid
     *            The EDID byte array
     * @return Monitor height in cm
     */
    public static int getVcm(byte[] edid) {
        // Byte 22 is vertical size in cm
        return edid[22];
    }

    public static byte[][] getDescriptors(byte[] edid) {
        byte[][] desc = new byte[4][18];
        for (int i = 0; i < desc.length; i++) {
            System.arraycopy(edid, 54 + 18 * i, desc[i], 0, 18);
        }
        return desc;
    }


    public static int getDescriptorType(byte[] desc) {
        return ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 4)).getInt();
    }

    public static String getTimingDescriptor(byte[] desc) {
        int clock = ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        int hActive = (desc[2] & 0xff) + ((desc[4] & 0xf0) << 4);
        int vActive = (desc[5] & 0xff) + ((desc[7] & 0xf0) << 4);
        return String.format("Clock %dMHz, Active Pixels %dx%d ", clock, hActive, vActive);
    }

    public static String getDescriptorRangeLimits(byte[] desc) {
        return String.format("Field Rate %d-%d Hz vertical, %d-%d Hz horizontal, Max clock: %d MHz", desc[5], desc[6],
                desc[7], desc[8], desc[9] * 10);
    }

    public static String getDescriptorText(byte[] desc) {
        return new String(Arrays.copyOfRange(desc, 4, 18)).trim();
    }

    public static String getDescriptorHex(byte[] desc) {
        return DatatypeConverter.printHexBinary(desc);
    }


    public static String toString(byte[] edid) {
        StringBuilder sb = new StringBuilder();
        sb.append("  Manuf. ID=").append(EdidUtil.getManufacturerID(edid)).append(", ID продукта=")
                .append(EdidUtil.getProductID(edid)).append(", ")
                .append(EdidUtil.isDigital(edid) ? "Цифровой" : "Аналоговый").append(", Серийный=")
                .append(EdidUtil.getSerialNo(edid)).append(", Manuf.Date=")
                .append(EdidUtil.getWeek(edid) * 12 / 52 + 1 + "/").append(EdidUtil.getYear(edid)).append(", EDID v")
                .append(EdidUtil.getVersion(edid));
        int hSize = EdidUtil.getHcm(edid);
        int vSize = EdidUtil.getVcm(edid);
        sb.append(String.format("%n  %d x %d cm (%.1f x %.1f in)", hSize, vSize, hSize / 2.54, vSize / 2.54));
        byte[][] desc = EdidUtil.getDescriptors(edid);
        for (byte[] b : desc) {
            switch (EdidUtil.getDescriptorType(b)) {
            case 0xff:
                sb.append("\n  Серийный номер: ").append(EdidUtil.getDescriptorText(b));
                break;
            case 0xfe:
                sb.append("\n  Неопределенный текст: ").append(EdidUtil.getDescriptorText(b));
                break;
            case 0xfd:
                sb.append("\n  Ограничения диапазона: ").append(EdidUtil.getDescriptorRangeLimits(b));
                break;
            case 0xfc:
                sb.append("\n  Имя монитора: ").append(EdidUtil.getDescriptorText(b));
                break;
            case 0xfb:
                sb.append("\n  White Point Data: ").append(EdidUtil.getDescriptorHex(b));
                break;
            case 0xfa:
                sb.append("\n  Стандартный идентификатор синхронизации: ").append(EdidUtil.getDescriptorHex(b));
                break;
            default:
                if (EdidUtil.getDescriptorType(b) <= 0x0f && EdidUtil.getDescriptorType(b) >= 0x00) {
                    sb.append("\n  Данные производителя: ").append(EdidUtil.getDescriptorHex(b));
                } else {
                    sb.append("\n  Предпочтительное время синхронизации: ").append(EdidUtil.getTimingDescriptor(b));
                }
                break;
            }
        }
        return sb.toString();
    }
}
