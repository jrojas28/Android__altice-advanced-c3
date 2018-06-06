package com.altice_crt_a.android__avanzado_3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jaime on 5/26/2018.
 */

public class MyBeautifulService extends Service {

    private static MyBeautifulService instance;
    public ArrayList<ServiceChangeListener> serviceChangeListeners;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for ( ServiceChangeListener l : serviceChangeListeners){
                    l.onChange();
                }
            }


        }, 1000, 1000);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceChangeListeners = new ArrayList<>();
        instance = this;

    }

    public static MyBeautifulService getInstance(){
        if(instance == null) {
            return new MyBeautifulService();
        }
        return instance;
    }

    public void addChangeListener ( ServiceChangeListener listener) {
        serviceChangeListeners.add(listener);
    }

    public void removeChangeListener (ServiceChangeListener listener) {
        serviceChangeListeners.remove(listener);
    }

    public interface ServiceChangeListener {
        void onChange();

    }

}
