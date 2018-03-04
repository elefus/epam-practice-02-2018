package com.epam.interpreter.controller;

//собственно всю работу по интерпретированию делает контролер, переводя с языка команд на язык работу с методами
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
    private Stack<Loop> loops = new Stack<>();


    public Controller(Viewer v, Optimizer o, Cells c){
        this.cells = c; this.viewer = v; this.optimizer =o;
        this.listSize = o.optimize(o.code).size();
        this.commandOptimize = o.optimize(o.code);
    }

    public void Interpret() {
        //отдельно пробежимся и свяжем команды while с командами end, чтобы знать куда перепрыгивать после их завершения
        findLoops(cells.getBufSize() / 2 );

        //основной проход по листу команд, с обработкой всех случаев,кроме end(принцип обработки очевиден)
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
                    //если находим цикл, то вызываем отдельную ветку прохода
                    While(pos, commandOptimize.get(i).begin, commandOptimize.get(i).end);
                    //когда цикл кончится, надо сразу перескочить на конец цикла
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


    //ветка с циклом, принципиальное отличие только в том, что мы повторяем, пока position не обнулится
    // нет обработки случая бесконечного цикла, увы
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
    //первый пробег, игнорируем все, кроме сдвигов и скобок
    private void findLoops(int pos) {
        for (int j = 0; j<listSize; j++){
            switch (commandOptimize.get(j).type){
                case SHIFT:
                    Shift(commandOptimize.get(j).arg, pos);
                    break;
                case WHILE:
                    commandOptimize.get(j).begin = j;
                    commandOptimize.get(j).pos = pos;
                    //на каждом новом начале цикла, создаем о нем заметку в стеке циклов
                    loops.push(new Loop(j));
                    break;
                case END:
                    //таким образом мы свяжем первую ] с последней [.
                    //все скобки свяжутся, так как мы проверяли их одинаковое количество еще на токенизации
                    commandOptimize.get(loops.pop().getBegin()).end = j;
            }
        }
    }

}
