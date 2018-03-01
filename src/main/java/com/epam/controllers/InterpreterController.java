package com.epam.controllers;

import com.epam.views.View;

import java.io.IOException;
import java.util.ArrayList;

public class InterpreterController {
    static CellController c;
    static View v;

    public InterpreterController(View v, CellController c){
        this.c = c; this.v = v;
    }

    public static Runnable generateCommands (){
        ArrayList<Runnable> toreturn = new ArrayList<>();
        int prev = 0;


        BREAKER: while(true){
            int val = 0;

            try {
                v.mark(1);
                val=v.read();
            } catch (IOException e) { System.out.println("OWIBKA");}



            switch (val){
                default:
                    break BREAKER;
                case -1:
                    break BREAKER;
                case 'q':
                        break BREAKER;
                case ' ':
                    continue BREAKER;
                case 10:
                    continue BREAKER;
                case '<':
                    toreturn.add(left);
                    break;
                case '>':
                    toreturn.add(right);
                    break;
                case '+':
                    toreturn.add(increase);
                    break;
                case '-':
                    toreturn.add(decrease);
                    break;
                case ',':
                    toreturn.add(in);
                    break;
                case '.':
                    toreturn.add(out);
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

    private static Runnable left = new Runnable() {
        @Override
        public void run() {
            c.left();
        }
    };

    private static Runnable right = new Runnable() {
        @Override
        public void run() {
            c.right();
        }
    };

    private static Runnable increase = new Runnable() {
        @Override
        public void run() {
            c.inc();
        }
    };

    private static Runnable decrease = new Runnable() {
        @Override
        public void run() {
            c.dec();
        }
    };

    private static Runnable in = new Runnable() {
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
                c.set((byte)character);
            }
        }
    };

    private static Runnable out = new Runnable() {
        @Override
        public void run() {
            try {
                v.write(c.get());
            } catch (Exception e) {}
        }
    };

    private static class Block implements Runnable {
        private Iterable<Runnable> components;

        public Block(Iterable<Runnable> components) {
            this.components = components;
        }

        @Override
        public void run() {
            for (Runnable comp: components)
                comp.run();
        }
    }

    private static class Loop implements Runnable {
        private Runnable body;

        public Loop(Runnable body) {
            this.body = body;
        }

        @Override
        public void run() {
            while (true) {
                if (c.get() == Byte.MIN_VALUE) return;
                body.run();
            }
        }
    }



}
