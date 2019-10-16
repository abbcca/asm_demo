package com.example.demo.example;

import com.sun.istack.internal.NotNull;
import jdk.internal.org.objectweb.asm.*;

import java.lang.annotation.Annotation;

public class BeanTest extends ClassLoader implements Opcodes {
    /*
     * 生成以下类的字节码
     *
     * public class Person {
     *
     * 		@NotNull
     * 		public String name；
     *
     * }
     */

    public static void main(String[] args) throws Exception {

        /********************************class***********************************************/

        // 创建一个ClassWriter, 以生成一个新的类

        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC, "com/example/demo/example/Person", null, "java/lang/Object", null);



        /*********************************constructor**********************************************/

        MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null,
                null);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mw.visitInsn(RETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();


        /*************************************field******************************************/

        //生成String name字段
        FieldVisitor fv = cw.visitField(ACC_PUBLIC, "name", "Ljava/lang/String;", null, null);
        AnnotationVisitor av = fv.visitAnnotation("LNotNull;", true);
        av.visit("value", "abc");
        av.visitEnd();
        fv.visitEnd();



        /***********************************generate and load********************************************/

        byte[] code = cw.toByteArray();

        BeanTest loader = new BeanTest();
        Class<?> clazz = loader.defineClass(null, code, 0, code.length);


        /***********************************test********************************************/

        Object beanObj = clazz.getConstructor().newInstance();

        clazz.getField("name").set(beanObj, "zhangjg");

        String nameString = (String) clazz.getField("name").get(beanObj);
        System.out.println("filed value : " + nameString);

        Annotation c = clazz.getField("name").getAnnotation(NotNull.class);
        System.out.println("annotation value: " + c.toString());

    }

}
