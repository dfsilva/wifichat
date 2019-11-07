package br.com.wifi.chat.module;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import java.util.Optional;
import java.util.concurrent.Callable;

import br.com.wifi.chat.model.User;
import br.com.wifi.chat.utils.PrefUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public final class UserModule {

    public final BehaviorSubject<Optional<User>> loggedUser = BehaviorSubject.create();

    private static UserModule _instance;
    private Application application;

    private UserModule(RxBus bus, Application app) {
        this.application = app;
        bus.observable().subscribe(this::process);
    }

    static UserModule init(RxBus bus, Application app) {
        if (_instance == null) {
            _instance = new UserModule(bus, app);
        }
        return _instance;
    }

    private void process(Object msg) {
        Log.e(UserModule.class.getName(), "Mensagem recebida");

        if (msg instanceof SetLoggedUser) {
            this.loggedUser.onNext(Optional.of(((SetLoggedUser) msg).loggedUser));
        }

        if(msg instanceof AppModule.Initialize){
            User user = PrefUtils.loadJson(this.application.getApplicationContext(),
                    "usuario", User.class);

            loggedUser.onNext(Optional.ofNullable(user));
        }
    }

    public Observable<User> addUser(User user) {
        return Observable.fromCallable(() -> {
            Thread.sleep(5000);
            loggedUser.onNext(Optional.ofNullable(user));
            PrefUtils.save(application.getApplicationContext(), "usuario", user.toJson());
            return user;
        }).subscribeOn(Schedulers.computation());
    }

    //actions
    public static class SetLoggedUser {
        final User loggedUser;
        public SetLoggedUser(User loggedUser) {
            this.loggedUser = loggedUser;
        }
    }

}





