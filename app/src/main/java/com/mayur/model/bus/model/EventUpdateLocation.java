package com.mayur.model.bus.model;

import com.mayur.model.bus.EventModel;
import com.mayur.model.entity.Data;

public class EventUpdateLocation implements EventModel {

    private Data data;

    public EventUpdateLocation(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

}
