package com.chetuan.askforit.util;

/**
 * Created by YT on 2015/8/24.
 */
public class MinHeapItem<T> {

    private int index = 0;

    private T value;

    public MinHeapItem(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public boolean lt(MinHeapItem<T> other){
        return false;
    }

}