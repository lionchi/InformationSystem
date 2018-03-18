package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.PowerSource;

public abstract class AbstractPowerSource implements PowerSource {

    private static final long serialVersionUID = 1L;

    protected String name;

    protected double remainingCapacity;

    protected double timeRemaining;

    public AbstractPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
        this.name = newName;
        this.remainingCapacity = newRemainingCapacity;
        this.timeRemaining = newTimeRemaining;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getRemainingCapacity() {
        return this.remainingCapacity;
    }

    @Override
    public double getTimeRemaining() {
        return this.timeRemaining;
    }
}
