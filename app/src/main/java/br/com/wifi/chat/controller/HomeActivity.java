package br.com.wifi.chat.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import br.com.wifi.chat.R;
import br.com.wifi.chat.module.AppModule;
import br.com.wifi.chat.module.UserModule;
import br.com.wifi.chat.service.DownloadService;
import br.com.wifi.chat.service.SocketService;
import br.com.wifi.chat.utils.NsdHelper;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {

    private NsdHelper nsdHelper;
    private UserModule userModule;
    private Disposable userDisposable;
    private SocketService socketService;
    private boolean shouldUnbind = false;

    private AppBarConfiguration mAppBarConfiguration;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SocketService.LocalBinder binder = (SocketService.LocalBinder) service;
            socketService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        userModule = AppModule.getInstance().userModule;

        Intent serviceIntent = new Intent(this, SocketService.class);
        startService(serviceIntent);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userDisposable = userModule.loggedUser.subscribe(loggedUser -> {
            nsdHelper = new NsdHelper(this);
            nsdHelper.mServiceName = loggedUser.get().name;
            nsdHelper.initializeNsd();
        });

        Intent bindIntent = new Intent(getApplicationContext(), SocketService.class);
        if (getApplicationContext().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE)) {
            this.shouldUnbind = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        userDisposable.dispose();
        if (shouldUnbind) {
            unbindService(connection);
            this.shouldUnbind = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
