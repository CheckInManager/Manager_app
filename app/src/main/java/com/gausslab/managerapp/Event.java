package com.gausslab.managerapp;

public class Event<T> {
    private T data;
    private boolean handled = false;

    public Event(T data){
        this.data = data;
    }

    public T consumeData(){
        handled = true;
        return data;
    }

    public boolean isHandled(){
        return handled;
    }
}
