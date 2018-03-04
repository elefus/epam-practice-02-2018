package com.epam.interpreter.controller;

import com.epam.interpreter.model.Opcode;

import java.util.ArrayList;
import java.util.List;

//заменяем код брейнфака на простые команды(токены)
public  class Tokenizer{
    public static List<Opcode> tokenize(String code) {

        List<Opcode> retValue = new ArrayList<>();
        int pos = 0;
        int whileQ = 0;
        int endQ =0;


        while (pos < code.length()) {
            switch (code.charAt(pos++)) {

                case '>': retValue.add(new Opcode(Opcode.Type.SHIFT, +1)); break;
                case '<': retValue.add(new Opcode(Opcode.Type.SHIFT, -1)); break;

                case '+': retValue.add(new Opcode(Opcode.Type.ADD, +1)); break;
                case '-': retValue.add(new Opcode(Opcode.Type.ADD, -1)); break;

                case '.': retValue.add(new Opcode(Opcode.Type.OUT)); break;
                case ',': retValue.add(new Opcode(Opcode.Type.IN)); break;
                case '[':
                    char next = code.charAt(pos);


                    if((next == '+' || next == '-') && code.charAt(pos + 1) == ']') {
                        retValue.add(new Opcode(Opcode.Type.ZERO));
                        pos += 2;
                    } else
                        retValue.add(new Opcode(Opcode.Type.WHILE));
                        whileQ +=1;
                    break;
                case ']': retValue.add(new Opcode(Opcode.Type.END));
                    endQ +=1;
                 break;
            }
        }
        try {
            if(whileQ != endQ){
                throw new Exception("brackets error");
            }
        } catch (Exception e) {
           System.out.print("brackets error");
        }
        return retValue;
    }
}