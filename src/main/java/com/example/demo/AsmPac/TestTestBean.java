package com.example.demo.AsmPac;

public class TestTestBean {
    public static void main(String[] args)  {
        TestBean testBean=new TestBean();
        Class testBeanClass=testBean.getClass();
        ClassLoader classLoader=testBeanClass.getClassLoader();

        try {
            Class<?> c=classLoader.loadClass("com.example.demo.AsmPac.TestBean");
            TestBean testBean2=(TestBean)c.newInstance();
            testBean2.halloAop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("类加载器"+classLoader);


        ClassLoader classLoader1=Thread.currentThread().getContextClassLoader();
        AopClassLoader classLoader2=new AopClassLoader(classLoader1);
        try {
            Class<?> clazz=classLoader2.loadClass("com.example.demo.AsmPac.TestBean_Tmp");
            clazz.getMethods()[0].invoke(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
