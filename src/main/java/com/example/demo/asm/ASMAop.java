package com.example.demo.asm;

import com.example.demo.example.MyClassLoader;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.io.FileOutputStream;

public class ASMAop {
    public static void main(String[] args) throws Exception{
        ClassReader cr=new ClassReader("com.example.demo.asm.Operation");
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new LogClassVisitor(cw),ClassReader.SKIP_DEBUG);
        byte[] bytes=cw.toByteArray();
        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("Operation_New.class");
        fos.write(bytes);
        fos.close();
        Class<?> clazz=new MyClassLoader().defineClassForName("com.example.demo.asm.Operation",bytes);
        clazz.getMethods()[0].invoke(clazz.newInstance());
    }
}
