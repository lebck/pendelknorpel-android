package de.hsrm.lback.myapplication.helpers;

public interface Callback<T> {
    void handle(T type);
}
