package com.epam.interpreter.model;

//класс только для поиска связок [ и ]
public class Loop {
    private int begin;

    public Loop (int b) {
        this.begin = b;
    }
    public int getBegin(){
        return begin;
    }

}
