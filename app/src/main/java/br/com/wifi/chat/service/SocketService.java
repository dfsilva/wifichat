package br.com.wifi.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SocketService extends Service {

    public static final String TAG = SocketService.class.getName();
    private final IBinder socketBinder = new LocalBinder();
    private Long value = 0L;


    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "Bind socket service ", Toast.LENGTH_SHORT).show();
        return socketBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Observable.interval(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((value) -> {
                    Toast.makeText(getApplicationContext(), "Value "+value, Toast.LENGTH_SHORT).show();
                    this.value = value;
                });

        return START_NOT_STICKY;
    }


    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }
}
