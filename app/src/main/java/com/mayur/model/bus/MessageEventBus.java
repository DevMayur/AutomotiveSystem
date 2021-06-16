package com.mayur.model.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public enum MessageEventBus {

    INSTANCE;

    private PublishSubject<EventModel> bus = PublishSubject.create();

    public void send(EventModel event) {
        bus.onNext(event);
    }

    public Observable<EventModel> toObservable() {
        return bus;
    }

}
