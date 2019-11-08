package br.com.wifi.chat.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class DownloadService extends IntentService {

    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 1;

    public int value = 0;


    public DownloadService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "");
        mBuilder.setContentTitle("Download de arquivo")
                .setContentText("Progresso...")
                .setSmallIcon(android.R.drawable.arrow_down_float);


        while (value < 100) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            value++;

            mBuilder.setProgress(100, value, false);

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        }

        mBuilder.setContentText("Download completed")
                .setProgress(0, 0, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
