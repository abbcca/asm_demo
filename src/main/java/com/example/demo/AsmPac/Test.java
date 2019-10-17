package com.example.demo.AsmPac;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) throws Exception{
        ClassReader cr=new ClassReader("com.example.demo.AsmPac.TestBean");
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new AopClassVisitor(Opcodes.ASM4,cw),ClassReader.SKIP_DEBUG);
        byte[] bytes=cw.toByteArray();
        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream("TestBean_New.class");
        fos.write(bytes);
        fos.close();
        Class<?> clazz=new MyClassLoader().defineClassForName("com.example.demo.AsmPac.TestBean_Tmp",bytes);
        clazz.getMethods()[0].invoke(clazz.newInstance());
    }
}
