package com.epam.controllers;

import com.epam.Interpreter;
import com.epam.models.Cells;
import com.epam.views.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.epam.controllers.InterpreterController.TYPE.*;

public class InterpreterController {
    static Cells c;
    static View v;
    int pointer;

    public InterpreterController(View v, Cells c){
        this.c = c; this.v = v;
    }

    public Block generateCommands (){
        ArrayList<Command> toreturn = new ArrayList<>();
        int prev = 0;


        BREAKER: while(true){
            int val = 0;

            try {
                v.mark(1);
                val=v.read();
            } catch (IOException e) { e.printStackTrace();}

            switch (val){
                default:
                    continue BREAKER;
                case -1:
                    break BREAKER;
                case 'q':
                        break BREAKER;
                case ' ':
                    continue BREAKER;
                case 10:
                    continue BREAKER;
                case 13:
                    continue BREAKER;
                case '<':
                    toreturn.add(new Move(-1));
                    break;
                case '>':
                    toreturn.add(new Move(1));
                    break;
                case '+':
                    toreturn.add(new Add(1));
                    break;
                case '-':
                    toreturn.add(new Add(-1));
                    break;
                case ',':
                    toreturn.add(new Read());
                    break;
                case '.':
                    toreturn.add(new Print());
                    break;
                case '[':
                    toreturn.add(new Loop(generateCommands()));
                    try {
                        if(v.read() != ']'){
                            throw new Exception("unmatched [");
                        }
                    } catch (Exception e) {}
                    break;
                case ']':
                    try {
                        v.reset();
                    } catch (Exception e) {}
                    break BREAKER;
            }
        }

        return new Block(toreturn);
    }

    public Block optimize (Runnable cmds){
        ArrayList<Command> ar = new ArrayList<>();
        if(cmds instanceof Block){
            ar = ((Block) cmds).components;
        }
        if(cmds instanceof Loop){
            ar = ((Loop) cmds).body.components;
        }
        for(int i = 1; i < ar.size(); i++){
            Command curr = ar.get(i);
            Command prev = ar.get(i-1);

            if(curr instanceof Loop){
                if(((Loop) curr).body.components.size() == 1 && ((Loop) curr).body.components.get(0).type == A ){
                    ar.set(i,new Assign());
                    i--;
                }
                else {
                    optimize(curr);
                    continue;
                }
            }
            if(curr.type == prev.type && curr.type != null || curr.type == A && prev instanceof Assign) {
                prev.val = prev.val + curr.val;
                ar.remove(i);

                if(prev.val == 0 && i != 1) {
                    ar.remove(i - 1);
                    i--;
                }
                i--;
            }
        }
        return new Block(ar);
    }
    enum TYPE {
        A, M, P, R,
    }

    public abstract class Command implements Runnable {
        public int val;
        public TYPE type;
        public String toString() {
            return getClass().getSimpleName() + "(" + val + ")";
        }
    }

    public class Assign extends Command {
        public Assign() {
        }

        public void add (int toadd) {
            val += toadd;
        }

        @Override
        public void run() {
            c.set(pointer, (byte)(Byte.MIN_VALUE + val));
        }
    }

    public class Move extends Command {
        public Move(int val) {
            this.val = val;
            this.type = M;
        }

        @Override
        public void run() {
            pointer += val;
        }
    }
    public class Add extends Command {
        public Add(int val) {
            this.val = val;
            this.type = A;
        }

        @Override
        public void run() {
            byte toadd = c.get(pointer);
            toadd += val;
            c.set(pointer, toadd);
        }

    }

    public class Read extends Command {
        public Read(){
            this.type = R;
        }
        @Override
        public void run() {
            int character = -1;
            try {
                character = v.readInput();
            } catch (Exception ioe) {
            }

            if (character != -1) {

                System.out.println(character);
                character-=128;
                c.set(pointer, (byte)character);
            }
        }
    };

    public class Print extends Command {
        public Print(){
            this.type = P;
        }
        @Override
        public void run() {
            try {
                v.write(c.get(pointer));
            } catch (Exception e) {}
        }
    };

    public class Block extends Command {
        public ArrayList<Command> components;

        public Block(ArrayList<Command> components) {
            this.components = components;
        }

        @Override
        public String toString() {
            String r = "";

            for(Command c : components)
                r += c.toString() + " ";
            return r;
        }

        @Override
        public void run() {
            for (Runnable comp: components)
                comp.run();
        }
    }

    public class Loop extends Command {
        private Block body;

        public Loop(Block body) {
            this.body = body;
        }

        @Override
        public String toString() {
            String r = "LOOP:";
            for(Command c : body.components)
                r += c.toString();
            return r;
        }

        @Override
        public void run() {
            while (true) {
                if (c.get(pointer) == Byte.MIN_VALUE) return;
                body.run();
            }
        }
    }



}
