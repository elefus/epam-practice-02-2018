package com.epam.Compiler;

import com.epam.optimizedInterpreter.*;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Compiler implements Runnable {

    private ClassNode classNode;
    private BlockingQueue<AbstractCommand> commandsQueue;
    private AbstractCommand cmd;
    private InsnList list;
    private MethodNode initMethod;
    LinkedList<LabelNode> labelStack = new LinkedList<>();
    private int lineNumber;

    public Compiler(BlockingQueue commandsQueue) throws IOException {
        this.commandsQueue = commandsQueue;
    }

    private void compileConstructor() {
        classNode = new ClassNode();
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC + ACC_SUPER;
        classNode.name = "com/epam/BFClass";
        classNode.superName = "java/lang/Object";
//        classNode.interfaces.add("java/lang/Runnable");


        classNode.fields.add(new FieldNode(ACC_PRIVATE, "buffer", "[B", null, null));
        classNode.fields.add(new FieldNode(ACC_PRIVATE, "pointer", "I", null, 0));

        initMethod = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        list = initMethod.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new IntInsnNode(BIPUSH, 100));
        list.add(new IntInsnNode(NEWARRAY, T_BYTE));
        list.add(new FieldInsnNode(PUTFIELD, "com/epam/BFClass", "buffer", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
    }

    private void finishConstructor() {
        list.add(new InsnNode(RETURN));
        classNode.methods.add(initMethod);
    }

    private void executeAdd() {
        if (!(cmd instanceof Add)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buffer", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "pointer", "I"));
        list.add(new InsnNode(DUP2));
        list.add(new InsnNode(BALOAD));
        list.add(new IntInsnNode(BIPUSH, cmd.getValue()));
        list.add(new InsnNode(IADD));
        list.add(new InsnNode(I2B));
        list.add(new InsnNode(BASTORE));
    }

    private void executePrint() {
        if (!(cmd instanceof Print)) {
            return;
        }
        list.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buffer", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "pointer", "I"));
        list.add(new InsnNode(BALOAD));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false));
    }

    private void executeMove() {
        if (!(cmd instanceof Move)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new InsnNode(DUP));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "pointer", "I"));
        list.add(new IntInsnNode(BIPUSH, cmd.getValue()));
        list.add(new InsnNode(IADD));
        list.add(new FieldInsnNode(PUTFIELD, "com/epam/BFClass", "pointer", "I"));
    }

    private void leftBracket(){
        LabelNode labelNode = new LabelNode();
        LabelNode labelNode2 = new LabelNode();
        labelStack.push(labelNode2);
        labelStack.push(labelNode);
        list.add(labelNode);
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buffer", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "pointer", "I"));
        list.add(new InsnNode(BALOAD));
        list.add(new JumpInsnNode(IFEQ,labelNode2));
    }

    private void rightBracket(){
        list.add(new JumpInsnNode(GOTO,labelStack.pop()));
        list.add(labelStack.pop());
    }

    private void executeGoto() {
        if (!(cmd instanceof Goto)) {
            return;
        }
        if (cmd.getValue() > 0){
            leftBracket();
        }
        else{
            rightBracket();
        }
    }

    private void executeAssign() {
        if (!(cmd instanceof Assign)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buffer", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "pointer", "I"));
        list.add(new IntInsnNode(BIPUSH, cmd.getValue()));
        list.add(new InsnNode(BASTORE));
    }

    private void writeToFile() throws IOException {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);

        byte[] bytes = writer.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("./out/BFClass.class")) {
            fos.write(bytes);
        }
    }

    @Override
    public void run() {
        compileConstructor();

        while (true) {
            try {
                cmd = commandsQueue.take();
                LabelNode label = new LabelNode();
                list.add(label);
                list.add(new LineNumberNode(++lineNumber,label));
                if (cmd instanceof End) {
                    break;
                }

                executeAdd();
                executePrint();
                executeMove();
                executeGoto();
                executeAssign();

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        finishConstructor();

        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
