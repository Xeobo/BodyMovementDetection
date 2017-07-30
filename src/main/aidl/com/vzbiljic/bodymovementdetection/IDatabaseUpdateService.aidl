// IDatabaseUpdateService.aidl
package com.vzbiljic.bodymovementdetection;

// Declare any non-default types here with import statements

interface IDatabaseUpdateService {

    boolean isServiceActive();

    void start();

    void stop();

    boolean pause();

    void setStatus(int status);

    void setSampleRate(int period);
}
