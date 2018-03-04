package com.epam.interpreter.model;

//класс для команд
public class Opcode{
    public enum Type{
        SHIFT,
        ADD,
        ZERO,
        OUT,
        IN,
        WHILE,
        END
    }

    public Type type = null;
    public int arg = 1; //позволяет понять, сколько одинаковых комманд подряд
    public int begin; // следующие три переменные нужны для обозначения циклов. только While, End
    public int end;
    public int pos;

    public Opcode(Type type, int arg) {
        this.type = type;
        this.arg = arg;
    }

    public Opcode(Type type) {
        this.type = type;
    }

    public Opcode clone(){
        return new Opcode(type, arg);
    }
}
