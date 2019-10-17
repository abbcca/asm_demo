package com.example.demo.AsmPac;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.InputStream;

public class AopClassLoader extends ClassLoader implements Opcodes {
    public AopClassLoader(ClassLoader parent){
        super(parent);
    }
    public AopClassLoader(){
        super(Thread.currentThread().getContextClassLoader());
    }
    public Class<?> loadClass(String name) throws ClassNotFoundException{
        if(!name.contains("TestBean_Tmp"))
            return super.loadClass(name);
        try{
            ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("com/example/demo/AsmPac/TestBean.class");
            ClassReader reader=new ClassReader(is);
            AopClassVisitor classAdapter=new AopClassVisitor(Opcodes.ASM4,cw);
            reader.accept(classAdapter,ClassReader.SKIP_DEBUG);
            byte[] code=cw.toByteArray();
            FileOutputStream fos=new FileOutputStream("TestBean_Tmp.class");
            fos.write(code);
            fos.flush();
            fos.close();
            return this.defineClass(name,code,0,code.length);
        }catch (Throwable e){
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
}
