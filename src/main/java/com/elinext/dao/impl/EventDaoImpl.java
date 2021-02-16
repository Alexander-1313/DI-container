package com.elinext.dao.impl;

import com.elinext.annotation.Inject;
import com.elinext.dao.Event;
import com.elinext.dao.EventDao;

public class EventDaoImpl implements EventDao {

    private static EventDaoImpl event;
    Event e;

    @Inject
    public EventDaoImpl(Event event) {
           this.e = event;
    }

    public EventDaoImpl() {
    }

    public Event getE() {
        return e;
    }

    public void setE(Event e) {
        this.e = e;
    }

    @Override
    public void printMessage() {
        System.out.println("This is EventDaoImpl!");
    }

}
