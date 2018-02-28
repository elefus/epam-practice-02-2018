package com.epam;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Main {

    public static void main(String[] args) {
        ClassNode classNode = new ClassNode();
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC + ACC_SUPER;
        classNode.name = "TestClass";
        classNode.superName = "java/lang/Object";
        classNode.interfaces.add("java/lang/Runnable");

        MethodNode initMethod = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        InsnList list = initMethod.instructions;
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
        list.add(new InsnNode(RETURN));

        // runMethod

        classNode.methods.add(initMethod);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);

        byte[] bytes = writer.toByteArray();

        Class<?> myClass = new ClassLoader() {

            Class<?> load() {
                return defineClass(null, bytes, 0, bytes.length);
            }

        }.load();

        try {
            ((Runnable) myClass.newInstance()).run();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
