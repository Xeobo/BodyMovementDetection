package com.vzbiljic.bodymovementdetection;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;

/**
 * Created by vzbiljic on 11.4.17..
 */

public class DatabaseUpdaterService extends Service implements Runnable,SensorEventListener{

    private static final String TAG = "DatabaseUpdaterService";
    private static final long SAMPLE_PERIOD = 200;

    public static String STATE_ARGUMENT = "STATE_ARGUMENT";

    private static DatabaseUpdaterService instance;
    private static final int X_AXE_INDEX = 0, Y_AXE_INDEX = 1, Z_AXE_INDEX = 2;

    private boolean isActive = false;
    private Semaphore mutex = new Semaphore(1);

    private float xCurrent, yCurrent, zCurrent;
    private float xLast = 0, yLast = 0, zLast = 0;

    private State state;
    private boolean stopThread = true;

    private IDatabaseUpdateService connection;
    private boolean registered = false;


    @Override
    public void onCreate(){
        connection = new DatabaseUpdaterServiceImpl();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {

        return (null == connection)?null:connection.asBinder();
    }


    public void logToDB() {
        try {
            float localXCurrent, localYCurrent, localZCurrent;
            mutex.acquire();

            localXCurrent = xCurrent;
            localYCurrent = yCurrent;
            localZCurrent = zCurrent;

            mutex.release();


            new AxisDiffereceData(localXCurrent - xLast, localYCurrent - yLast, localZCurrent - zLast,State.intValue(state)).save();


            xLast = localXCurrent;
            yLast = localYCurrent;
            zLast = localZCurrent;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        try {
            mutex.acquire();
            xCurrent = sensorEvent.values[X_AXE_INDEX];
            yCurrent = sensorEvent.values[Y_AXE_INDEX];
            zCurrent = sensorEvent.values[Z_AXE_INDEX];
            mutex.release();
            if(stopThread){
                new Thread(this).start();

                stopThread = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {
        stopThread = true;
        connection = null;

        super.onDestroy();
    }

    @Override
    public void run() {
        while (!stopThread) {
            try {
                Thread.sleep(SAMPLE_PERIOD);

                if(isActive)
                    logToDB();
            }catch (InterruptedException ie){
                Log.e(TAG,"To stop service just call stop! Do not interrupt thread!");
            }
        }
    }

    public enum State{
        STANDING,WALKING,RUNNING;

        public static State getFromInt(int id){
            switch (id){
                case 0: return STANDING;
                case 1: return WALKING;
                case 2: return RUNNING;
                default: {
                    Log.e("ERROR", "Wrong int value!");
                    return null;
                }
            }
        }

        public static Integer intValue(State id){
            switch (id){
                case STANDING: return 0;
                case WALKING: return 1;
                case RUNNING: return 2;
                default: {
                    Log.e("ERROR", "Wrong State value!");
                    return null;
                }
            }
        }



    }

    public class DatabaseUpdaterServiceImpl extends IDatabaseUpdateService.Stub{

        @Override
        public boolean isServiceActive() throws RemoteException {
            Log.i(TAG,"service Active");
            return isActive;
        }

        @Override
        public synchronized void start() throws RemoteException {
            Log.i(TAG,"service started");
            if(!isActive){

                if(!registered){
                    SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
                    Sensor mySensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                    manager.registerListener(DatabaseUpdaterService.this,mySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    registered = true;
                }

                isActive = true;
            }
        }

        @Override
        public void stop() throws RemoteException {
            stopThread = true;

            SensorManager manager = (SensorManager)getSystemService(SENSOR_SERVICE);
            Sensor mySensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            manager.unregisterListener(DatabaseUpdaterService.this);

            registered = false;
        }

        @Override
        public synchronized boolean pause() throws RemoteException {
            Log.i(TAG,"service paused");
            if(isActive){
                isActive = false;
            }
            return true;
        }

        @Override
        public void setStatus(int status) throws RemoteException {
            state = State.getFromInt(status);
        }
    }


}