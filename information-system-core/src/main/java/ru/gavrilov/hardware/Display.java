package ru.gavrilov.hardware;

import java.io.Serializable;


public interface Display extends Serializable {

    byte[] getEdid();
}
