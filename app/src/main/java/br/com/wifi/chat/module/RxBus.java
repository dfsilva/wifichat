package br.com.wifi.chat.module;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private PublishSubject<Object> bus = PublishSubject.create();

    public RxBus() {
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> observable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
