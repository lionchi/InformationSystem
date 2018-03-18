package ru.gavrilov.hardware.common;


import ru.gavrilov.hardware.Display;
import ru.gavrilov.util.EdidUtil;

import java.util.Arrays;

public abstract class AbstractDisplay implements Display {

    private static final long serialVersionUID = 1L;

    protected byte[] edid;

    protected AbstractDisplay(byte[] edid) {
        this.edid = edid;
    }

    @Override
    public byte[] getEdid() {
        return Arrays.copyOf(this.edid, this.edid.length);
    }

    @Override
    public String toString() {
        return EdidUtil.toString(this.edid);
    }
}