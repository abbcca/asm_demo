package com.example.demo.example;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class ASMGettingStarted {
    /**
     * 动态创建一个类，有一个无参数的构造函数
     */
    static ClassWriter createClassWriter(String className){
        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //声明一个类，使用JDK1.8版本，public的类，父类是java.lang.Object，没有实现任何接口
        cw.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,className,null,"java/lang/Object",null);
        //初始化一个无参的构造函数
        MethodVisitor constructor=cw.visitMethod(Opcodes.ACC_PUBLIC,"<init>","()V",null,null);
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        //执行父类的init初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,"java/lang/Object","<init>","()V",false);
        //从当前方法返回void
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1,1);
        constructor.visitEnd();
        return cw;
    }
    /**
     * 创建一个run方法，里面只有一个输出
     * public void run()
     * {
     * 		System.out.println(message);
     * }
     * @return
     * @throws Exception
     */
    static byte[] createVoidMethod(String className, String message) throws Exception
    {
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test
        ClassWriter cw = createClassWriter(className.replace('.', '/'));

        //创建run方法
        //()V表示函数，无参数，无返回值
        MethodVisitor runMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        //先获取一个java.io.PrintStream对象
        runMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        //将int, float或String型常量值从常量池中推送至栈顶  (此处将message字符串从常量池中推送至栈顶[输出的内容])
        runMethod.visitLdcInsn(message);
        //执行println方法（执行的是参数为字符串，无返回值的println函数）
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1, 1);
        runMethod.visitEnd();

        return cw.toByteArray();
    }


    /**
     * 创建一个返回Integer=10的函数
     * public Integer getIntVal()
     * {
     * 		return 10;
     * }
     * @return
     * @throws Exception
     */
    static byte[] createReturnMethod(String className,int returnValue) throws Exception{
        //注意，这里需要把classname里面的.改成/，如com.asm.Test改成com/asm/Test
        ClassWriter cw = createClassWriter(className.replace('.', '/'));

        //创建get方法
        //()Ljava/lang/Integer;表示函数，无参数，返回值为：java.lang.Integer，注意最后面的分号，没有就会报错
        MethodVisitor getMethod = cw.visitMethod(Opcodes.ACC_PUBLIC, "getIntVal", "()Ljava/lang/Integer;", null, null);
        //将单字节的常量值(-128~127)推送至栈顶(如果不是-128~127之间的数字，则不能用bipush指令)
        getMethod.visitIntInsn(Opcodes.BIPUSH, returnValue);
        //调用Integer的静态方法valueOf把10转换成Integer对象
        String methodDesc = Type.getMethodDescriptor(Integer.class.getMethod("valueOf", int.class));
        getMethod.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", methodDesc, false);
        //从当前方法返回对象引用
        getMethod.visitInsn(Opcodes.ARETURN);
        getMethod.visitMaxs(1, 1);
        getMethod.visitEnd();

        return cw.toByteArray();


    }

    public static void main(String[] args) throws Exception
    {
        String className = "com.example.demo.example.Tester";
       // byte[] classData = createVoidMethod(className, "This is my first ASM test");
        byte[] classData = createReturnMethod(className, 100);
        Class<?> clazz = new MyClassLoader().defineClassForName(className, classData);
        Method[] methods=clazz.getMethods();
        for(Method method:methods){
            System.out.println(method.getName());
        }
        Object value=methods[0].invoke(clazz.newInstance());
        System.out.println(value);
    }

}
