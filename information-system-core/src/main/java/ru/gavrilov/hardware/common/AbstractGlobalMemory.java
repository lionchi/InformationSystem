package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.GlobalMemory;

public abstract class AbstractGlobalMemory implements GlobalMemory {

    private static final long serialVersionUID = 1L;

    protected long memTotal = 0L;
    protected long memAvailable = 0L;
    protected long swapTotal = 0L;
    protected long swapUsed = 0L;

    protected abstract void updateMeminfo();

    protected abstract void updateSwap();

    @Override
    public long getAvailable() {
        updateMeminfo();
        return this.memAvailable;
    }

    @Override
    public long getTotal() {
        if (this.memTotal == 0) {
            updateMeminfo();
        }
        return this.memTotal;
    }

    @Override
    public long getSwapUsed() {
        updateSwap();
        return this.swapUsed;
    }

    @Override
    public long getSwapTotal() {
        updateSwap();
        return this.swapTotal;
    }
}
