package com.example.demo.AsmPac;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class AopMethod extends MethodVisitor implements Opcodes {
    public AopMethod(int api,MethodVisitor methodVisitor){
        super(api,methodVisitor);
    }
    public void visitCode(){
        super.visitMethodInsn(INVOKESTATIC,"com/example/demo/AsmPac/AopInterceptor","beforeInvoke","()V",false);
        super.visitCode();

    }
    public void visitInsn(int opcode){
        if(opcode==RETURN){//在返回之前安插after 代码。
            mv.visitMethodInsn(INVOKESTATIC,"com/example/demo/AsmPac/AopInterceptor","afterInvoke","()V",false);
        }
        super.visitInsn(opcode);
    }
}
