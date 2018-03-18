package ru.gavrilov.hardware;

import org.threeten.bp.LocalDate;

import java.io.Serializable;

public interface Firmware extends Serializable {

    String getManufacturer();

    String getName();

    String getDescription();

    String getVersion();

    LocalDate getReleaseDate();
}
