package com.epam.controllers;

import com.epam.models.Cells;

public class CellController {
    Cells cells;
    private int pointer;
    public CellController(Cells cells){
        this.cells = cells;
        pointer = 0;
    }



    byte left(){
       return cells.getAll()[--pointer];
    }
    byte right(){
        return cells.getAll()[++pointer];
    }
    byte inc(){
        return ++cells.getAll()[pointer];
    }
    byte dec(){
        return --cells.getAll()[pointer];
    }
    byte set(byte s){
        return cells.getAll()[pointer]=s;
    }
    byte get(){
        return cells.getAll()[pointer];
    }
}
