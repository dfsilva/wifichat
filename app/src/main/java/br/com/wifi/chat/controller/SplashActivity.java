package br.com.wifi.chat.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import br.com.wifi.chat.ChatApplication;
import br.com.wifi.chat.R;
import br.com.wifi.chat.module.AppModule;
import br.com.wifi.chat.module.UserModule;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {


//    private UserModule userModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ChatApplication application = (ChatApplication) getApplication();

        //        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new CheckRunState().execute();
//            }
//        }, 1200);

        application.module().isInitialized()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((inited) -> {
                    if (inited) {
//                        new CheckRunState().execute();
                        AppModule.getInstance().userModule.loggedUser
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(user -> {
                            if (user != null) {
                                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                                finish();
                            } else {
                                Intent discoverIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(discoverIntent);
                                finish();
                            }
                        });
                    }
                });

    }

    private class CheckRunState extends AsyncTask<Void, Void, Void> {

        boolean isFirstRun = false;

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            isFirstRun = preferences.getBoolean("first_run", true);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isFirstRun) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            } else {
                Intent discoverIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(discoverIntent);
                finish();
            }
            finish();
        }

    }
}
