package com.mayur.model.bus.model;

import com.mayur.model.bus.EventModel;
import com.mayur.model.entity.GpsStatusEntity;

public class EventUpdateStatus implements EventModel {

    private GpsStatusEntity status;

    public EventUpdateStatus(GpsStatusEntity status) {
        this.status = status;
    }

    public GpsStatusEntity getStatus() {
        return status;
    }

}

