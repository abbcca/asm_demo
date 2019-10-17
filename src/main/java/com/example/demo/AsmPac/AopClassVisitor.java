package com.example.demo.AsmPac;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class AopClassVisitor extends ClassVisitor implements Opcodes {
    public AopClassVisitor(int api, ClassVisitor cv){
        super(api,cv);
    }
    public void visit(int version,int access,String name,String signature,String superName,String[] interfaces){
        //更改类名，并使新类继承原有的类。
        super.visit(version,access,name+"_Tmp",signature,name,interfaces);
        {
            //生成默认的构造方法
            MethodVisitor methodVisitor=super.visitMethod(ACC_PUBLIC,
                    "<init>",
                    "()V",
                    null,
                    null);
            methodVisitor.visitCode();
            //生成构造方法的字节码指令
            methodVisitor.visitVarInsn(ALOAD,0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL,name,"<init>","()V");
            methodVisitor.visitInsn(RETURN);
            //指定当前要生成的方法的最大局部变量和最大操作数栈
            methodVisitor.visitMaxs(1,1);
            methodVisitor.visitEnd();
        }
    }
    public MethodVisitor visitMethod(int access,String name,String desc,String signature,String[] exceptions){
        if("<init>".equals(name))
            return null;//放弃原有类中所有构造方法
        if(!name.equals("halloAop"))
            return null;// 只对halloAop方法执行代理
       /* MethodVisitor mv=super.visitMethod(access,name,desc,signature,exceptions);
        return new AopMethod(this.api,mv);*/
       if(name.equals("halloAop")){
           return new AopMethod(Opcodes.ASM5,super.visitMethod(access,name,desc,signature,exceptions));
       }
       return super.visitMethod(access,name,desc,signature,exceptions);
    }
}
