package br.com.wifi.chat.module;

import android.app.Application;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public final class AppModule {


    private RxBus bus;
    private Application application;

    private final BehaviorSubject<Boolean> initialized = BehaviorSubject.createDefault(false);
    private static AppModule _instance = null;

    public final UserModule userModule;

    private AppModule(RxBus bus, Application application) {
        initialized.onNext(false);
        this.bus = bus;
        this.application = application;

        this.userModule = UserModule.init(bus, application);

        bus.observable()
                .observeOn(Schedulers.io())
                .subscribe(this::process);
        Log.d(AppModule.class.getName(), "App modulo inicializado");
    }

    public static AppModule init(RxBus bus, Application application) {
        if (_instance == null) {
            _instance = new AppModule(bus, application);
        }
        return _instance;
    }

    public static AppModule getInstance(){
        return _instance;
    }

    public BehaviorSubject<Boolean> isInitialized() {
        return this.initialized;
    }

    private void process(Object msg) {
        Log.e(UserModule.class.getName(), "Mensagem recebida");
        if (msg instanceof SetInitialized) {
            this.initialized.onNext(((SetInitialized) msg).initialized);
        }

        if (msg instanceof Initialize) {
            try {
                Thread.sleep(5000);
                bus.send(new SetInitialized(Boolean.TRUE));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //Actions

    public static class Initialize {
    }

    public static class SetInitialized {
        private final Boolean initialized;

        public SetInitialized(Boolean initialized) {
            this.initialized = initialized;
        }
    }
}
