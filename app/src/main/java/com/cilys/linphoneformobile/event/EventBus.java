package com.cilys.linphoneformobile.event;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private static EventBus event;
    private List<EventImpl> subs;

    private EventBus(){
        subs = new ArrayList<>();
    }
    public static EventBus getInstance() {
        if (event == null) {
            synchronized (EventBus.class) {
                if (event == null) {
                    event = new EventBus();
                }
            }
        }
        return event;
    }
    public void postEvent(Event event) {
        if (subs == null) {
            subs = new ArrayList<>();
        }
        for (EventImpl m : subs) {
            m.onTrigger(event);
        }
    }

    public void postEvent(int type) {
        Event e = new Event();
        e.what = type;

        postEvent(e);
    }

    public void onSub(EventImpl m){
        if (m == null) {
            return;
        }
        if (subs != null) {
            if (!subs.contains(m)) {
                subs.add(m);
            }
        }
    }
    public void unSub(EventImpl m){
        if (m == null) {
            return;
        }
        if (subs != null) {
            subs.remove(m);
        }
    }


}