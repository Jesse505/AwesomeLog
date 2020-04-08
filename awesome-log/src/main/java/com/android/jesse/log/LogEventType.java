package com.android.jesse.log;

public enum LogEventType {
    USER_EVENT("UserEvent"),
    NET_EVENT("NetEvent");

    String mEventType;

    private LogEventType(String eventType) {
        this.mEventType = eventType;
    }

    public String getEventType() {
        return this.mEventType;
    }
}

