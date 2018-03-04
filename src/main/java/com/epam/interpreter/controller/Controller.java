package com.epam.interpreter.controller;


import com.epam.interpreter.model.Cells;
import com.epam.interpreter.model.Loop;
import com.epam.interpreter.model.Opcode;
import com.epam.interpreter.view.Viewer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Stack;

public class Controller {
    static Optimizer optimizer;
    static Viewer viewer;
    static Cells cells;
    static int listSize;
    static List<Opcode> commandOptimize;
    static int pos = cells.getBufSize() / 2 ;
    private Stack<Loop> loops = new Stack<Loop>();
    private int bracketsCounter = 0;

    public Controller(Viewer v, Optimizer o, Cells c){
        this.cells = c; this.viewer = v; this.optimizer =o;
        this.listSize = o.optimize(o.code).size();
        this.commandOptimize = o.optimize(o.code);
    }

    public void Interpret() {
        findLoops(pos);
        for (int i = 0; i<listSize;i++){
            switch (commandOptimize.get(i).type){
                case  SHIFT:
                    Shift(commandOptimize.get(i).arg);
                    break;
                case  ADD:
                   Add((byte)commandOptimize.get(i).arg, pos);
                    break;
                case  ZERO:
                    Zero(pos);
                    break;
                case  OUT:
                   Out(pos);
                    break;
                case  IN:
                    In(pos);
                    break;
                case  WHILE:
                    While(pos, commandOptimize.get(i).begin, commandOptimize.get(i).end);
                    i=commandOptimize.get(i).end;
                    break;
            }
        }
    }



    private int Shift(int i){
        return pos+=i;
    }
    private int Shift(int i, int pos){
        return pos+=i;
    }
    private void Add(byte i,int pos){
        cells.addCell(pos, i);
    }
    private void Zero(int pos){
        cells.setCell(pos, (byte)0);
    }
    private void Out(int pos) {
        try {
            viewer.write(cells.getCell(pos));
        } catch (Exception e) {
            System.out.print("Output error");
        }
    }
    private void In(int pos){
        try {
            cells.setCell(pos,(byte) viewer.read());
        }catch(Exception e){
            System.out.print("input error");
        }
    }



    private void While(int pos, int begin, int end){
        while(pos!=0){
            for (int j = begin+1; j<end-1;j++){
                switch (commandOptimize.get(j).type){
                    case  SHIFT:
                        Shift(commandOptimize.get(j).arg, pos);
                        break;
                    case  ADD:
                        Add((byte)commandOptimize.get(j).arg, pos);
                        break;
                    case  ZERO:
                        Zero(pos);
                        break;
                    case  OUT:
                        Out(pos);
                        break;
                    case  IN:
                        In(pos);
                        break;
                    case  WHILE:
                        While(pos, commandOptimize.get(j).begin, commandOptimize.get(j).end);
                        j=commandOptimize.get(j).end;
                        break;
                }

            }
        }
    }
    private void findLoops(int pos) {
        for (int j = 0; j<listSize; j++){
            switch (commandOptimize.get(j).type){
                case SHIFT:
                    Shift(commandOptimize.get(j).arg, pos);
                    break;
                case WHILE:
                    commandOptimize.get(j).begin = j;
                    commandOptimize.get(j).pos = pos;
                    loops.push(new Loop(j));
                    break;
                case END:
                    commandOptimize.get(loops.pop().getEnd()).end = j;
            }
        }
    }

}
