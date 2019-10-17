package com.example.demo.asm;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class LogClassVisitor extends ClassVisitor {
    public LogClassVisitor(ClassVisitor cv)
    {
        super(Opcodes.ASM5, cv);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        if("oper".equals(name))
        {
            return new LogMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

}
