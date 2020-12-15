package com.cobaltspeech.diathekeexample.model;

public interface IModel {
    void registerObserver(IModelObserver o);
    void removeObserver(IModelObserver o);
    void notifyObservers();
}

