package com.example.demo.asm;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class LogMethodVisitor extends MethodVisitor {
    public LogMethodVisitor(MethodVisitor mv)
    {
        super(Opcodes.ASM5, mv);
    }

    public void visitCode() {
        /**
         * 方法执行之前植入代码
         */
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/example/demo/asm/Log", "startLog", "()V", false);
        super.visitCode();
    }

    public void visitInsn(int opcode)
    {
        if(opcode == Opcodes.RETURN)
        {
            /**
             * 方法return之前，植入代码
             */
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/example/demo/asm/Log", "endLog", "()V", false);
        }
        super.visitInsn(opcode);
    }


}
