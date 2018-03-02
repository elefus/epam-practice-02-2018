package com.epam.compiler;

import com.epam.optimization.commands.*;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class ControllerCompiler implements Runnable {

    private ClassNode classNode = new ClassNode();
    private BlockingQueue<Command> optimizedQueue;
    private LinkedList<LabelNode> labelNodes = new LinkedList<>();
    private Command curCmd;
    private InsnList list;
    private int lineNo;
    private MethodNode initMethod;

    public ControllerCompiler(BlockingQueue<Command> optimizedQueue) {
        this.optimizedQueue = optimizedQueue;
    }

    private void startConstructor() {
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC + ACC_SUPER;
        classNode.name = "com/epam/BFClass";
        classNode.superName = "java/lang/Object";

        classNode.fields.add(new FieldNode(ACC_PRIVATE, "buff", "[B", null, null));
        classNode.fields.add(new FieldNode(ACC_PRIVATE, "ptr", "I", null, 0));

        initMethod = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        list = initMethod.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new IntInsnNode(BIPUSH, 100));
        list.add(new IntInsnNode(NEWARRAY, T_BYTE));
        list.add(new FieldInsnNode(PUTFIELD, "com/epam/BFClass", "buff", "[B"));

    }

    private void finishConstructor() {
        list.add(new InsnNode(RETURN));

        classNode.methods.add(initMethod);
    }

    private void saveToFile() throws IOException {

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);

        byte[] bytes = writer.toByteArray();

        try (FileOutputStream fos = new FileOutputStream("./out/BFClass.class")) {
            fos.write(bytes);
        }
    }

    private boolean isEnd() {
        return curCmd instanceof End;
    }

    private void processIfAdd() {
        if (!(curCmd instanceof Add)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buff", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "ptr", "I"));
        list.add(new InsnNode(DUP2));
        list.add(new InsnNode(BALOAD));
        list.add(new IntInsnNode(BIPUSH, curCmd.getVal()));
        list.add(new InsnNode(IADD));
        list.add(new InsnNode(I2B));
        list.add(new InsnNode(BASTORE));
    }

    private void processIfPrint() {
        if (!(curCmd instanceof Print)) {
            return;
        }
        for (int i = 0; i < curCmd.getVal(); i++) {
            list.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buff", "[B"));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "ptr", "I"));
            list.add(new InsnNode(BALOAD));
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V", false));
        }
    }

    private void processIfMove() {
        if (!(curCmd instanceof Move)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new InsnNode(DUP));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "ptr", "I"));
        list.add(new IntInsnNode(BIPUSH, curCmd.getVal()));
        list.add(new InsnNode(IADD));
        list.add(new FieldInsnNode(PUTFIELD, "com/epam/BFClass", "ptr", "I"));
    }

    private void leftBracket() {
        LabelNode start = new LabelNode();
        LabelNode end = new LabelNode();
        labelNodes.push(end);
        labelNodes.push(start);
        list.add(start);
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buff", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "ptr", "I"));
        list.add(new InsnNode(BALOAD));
        list.add(new JumpInsnNode(IFEQ, end));
    }

    private void rightBracket() {
        list.add(new JumpInsnNode(GOTO, labelNodes.pop()));
        list.add(labelNodes.pop());
    }

    private void processIfBrackets() {
        if (!(curCmd instanceof Goto)) {
            return;
        }
        if (curCmd.getVal() > 0) {
            leftBracket();
        } else {
            rightBracket();
        }
    }

    private void processIfAssign() {
        if (!(curCmd instanceof Assign)) {
            return;
        }
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "buff", "[B"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "com/epam/BFClass", "ptr", "I"));
        list.add(new IntInsnNode(BIPUSH, curCmd.getVal()));
        list.add(new InsnNode(BASTORE));
    }

    @Override
    public void run() {
        startConstructor();

        while (true) {
            try {
                curCmd = optimizedQueue.take();

                LabelNode label = new LabelNode();
                list.add(label);
                list.add(new LineNumberNode(++lineNo, label));

                if (isEnd()) {
                    break;
                }
                processIfAdd();
                processIfPrint();
                processIfMove();
                processIfBrackets();
                processIfAssign();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finishConstructor();
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
