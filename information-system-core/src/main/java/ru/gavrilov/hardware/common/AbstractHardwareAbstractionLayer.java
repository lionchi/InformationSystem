package ru.gavrilov.hardware.common;

import ru.gavrilov.hardware.*;

/**
 * Общие поля или методы, используемые платформенно-реализационными реализациями
 * HardwareAbstractionLayer
 */
public abstract class AbstractHardwareAbstractionLayer implements HardwareAbstractionLayer {

    private static final long serialVersionUID = 1L;

    protected ComputerSystem computerSystem;

    protected CentralProcessor processor;

    protected GlobalMemory memory;

    protected Sensors sensors;

}
