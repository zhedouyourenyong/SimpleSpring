package Test;

import Test.controller.controllerA;
import Test.controller.controllerB;
import framework.ApplicationServer;
import framework.bean.BeanFactory;
import framework.bean.impl.DefaultBeanFactory;
import framework.route.RouteProcess;
import framework.route.RouterManager;
import framework.scanner.ClassScanner;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class test
{
    public static void main (String[] args) throws Exception
    {
        try
        {
            ApplicationServer.start(test.class,null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

















    public static void test2(String packagetName) throws Exception
    {

        DefaultBeanFactory beanFactory=DefaultBeanFactory.getInstance();
        beanFactory.init(packagetName);

        controllerA object=(controllerA)beanFactory.getBean("Test.controller.controllerA");
//        object.test();
    }

    public static void test1(String packagetName) throws Exception
    {
        System.out.println("test类所在的包路径:"+packagetName);
        Set<Class<?>> classes= ClassScanner.getClasses(packagetName);
        System.out.println("一共有 "+classes.size()+" 个类");
        for(Class<?> cs:classes)
        {
            System.out.println(cs.getName());
        }


        System.out.println("----------------------------------------");


        Set<Class<?>> beanSet=ClassScanner.getBeanClasses(packagetName);
        System.out.println("bean有"+beanSet.size()+"个");
        for (Class<?> cls:beanSet)
        {
            System.out.println(cls.getName());
        }


        System.out.println("----------------------------------------");

        Map<String,Class<?>> controllerMap=ClassScanner.getControllerMap(packagetName);
        System.out.println("controller有"+controllerMap.size()+"个");
        for(Map.Entry<String,Class<?>> entry:controllerMap.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        System.out.println("------------------------------------------");

        Map<String,Class<?>> serviceMap=ClassScanner.getServiceMap(packagetName);
        System.out.println("service有"+serviceMap.size()+"个");
        for(Map.Entry<String,Class<?>> entry:serviceMap.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
