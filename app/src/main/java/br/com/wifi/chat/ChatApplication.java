package br.com.wifi.chat;

import android.app.Application;
import android.util.Log;

import br.com.wifi.chat.module.AppModule;
import br.com.wifi.chat.module.RxBus;

public class ChatApplication extends Application {

    private AppModule appModule;
    private RxBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new RxBus();
        appModule = AppModule.init(bus, this);
        bus.send(new AppModule.Initialize());
        Log.e(ChatApplication.class.getName(), "inicializado");
    }

    public AppModule module(){
        return appModule;
    }
}
