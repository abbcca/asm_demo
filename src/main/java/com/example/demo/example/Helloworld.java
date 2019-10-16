package com.example.demo.example;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;

public class Helloworld extends ClassLoader implements Opcodes {
    public static void main(final String[] args) throws Exception{
        //定义一个叫做Example的类
        ClassWriter cw=new ClassWriter(0);
        cw.visit(V1_1,ACC_PUBLIC,"Example",null,"java/lang/Object",null);
        //生成默认的构造方法
        MethodVisitor mv=cw.visitMethod(ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null);
        //生成构造方法的字节码指令
        mv.visitVarInsn(ALOAD,0);
        mv.visitMethodInsn(INVOKESPECIAL,"java/lang/Object","<init>","()V");
        mv.visitInsn(RETURN);
        //指定当前要生成的方法的最大局部变量和最大操作数栈
        mv.visitMaxs(1,1);
        mv.visitEnd();
        //生成main方法
        mv=cw.visitMethod(ACC_PUBLIC+ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null);


        //生成main方法中的字节码指令
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");

        mv.visitLdcInsn("Hello world!");
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);

        //字节码生成完成
        mv.visitEnd();

        // 获取生成的class文件对应的二进制流
        byte[] code = cw.toByteArray();


        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Example.class");
        fos.write(code);
        fos.close();

        //直接将二进制流加载到内存中
        Helloworld loader = new Helloworld();
        Class<?> exampleClass = loader.defineClass("Example", code, 0, code.length);

        //通过反射调用main方法
        exampleClass.getMethods()[0].invoke(null, new Object[] { null });


    }
}
