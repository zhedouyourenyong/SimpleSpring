package framework.bean.impl;

import framework.annotation.Aspect;
import framework.annotation.AutoWired;
import framework.annotation.Resource;
import framework.bean.BeanFactory;
import framework.exception.SimpleException;
import framework.proxy.AbsMethodAdvance;
import framework.scanner.ClassScanner;
import framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultBeanFactory implements BeanFactory
{
    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanFactory.class);
    private Map<String, Object> beans = new HashMap<>(64);  //key --> className


    private static volatile DefaultBeanFactory defaultBeanFactory;

    public static DefaultBeanFactory getInstance ()
    {
        if(defaultBeanFactory == null)
        {
            synchronized (DefaultBeanFactory.class)
            {
                if(defaultBeanFactory == null)
                {
                    defaultBeanFactory = new DefaultBeanFactory();
                }
            }
        }
        return defaultBeanFactory;
    }


    private DefaultBeanFactory ()
    {
    }

    public void init (String packageName) throws Exception
    {
        startIOC(packageName);
        startAOP(packageName);
        startDI();
    }

    private void startIOC (String packageName) throws Exception
    {
        Set<Class<?>> beanClasses = ClassScanner.getIOCBeanClasses(packageName);

        if(beanClasses != null && beanClasses.size() != 0)
        {
            for (Class<?> cls : beanClasses)
            {
                Object object = ReflectionUtil.newInstance(cls);
                register(object);
            }
        }
    }

    private void startAOP (String packageName) throws Exception
    {
        Map<String, Class<?>> proxyMap = ClassScanner.getProxyMap(packageName);
        if(proxyMap != null && proxyMap.size() != 0)
        {
            for (Map.Entry<String, Class<?>> entry : proxyMap.entrySet())
            {
                String className = entry.getKey();
                Class<?> cls = entry.getValue();

                Aspect aspect = cls.getAnnotation(Aspect.class);
                String pointCut = aspect.pointCut();
                String[] arr = pointCut.split(",");

                //目标类名
                String targetClassName = arr[0];
                //目标方法名
                String targetMethodName = arr[1];

                AbsMethodAdvance proxyer = (AbsMethodAdvance) ReflectionUtil.newInstance(cls);
                proxyer.setProxyMethodName(targetMethodName);
                Object targetBean = beans.get(targetClassName);

                if(targetBean != null)
                {
                    Object object = proxyer.createProxyObject(targetBean);
                    beans.put(targetClassName, object);
                }
            }
        }
    }

    private void startDI () throws Exception
    {
        if(beans == null || beans.isEmpty())
            return;

        for (Map.Entry<String, Object> entry : beans.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> cls = value.getClass();

            Field[] beanFields = cls.getDeclaredFields();
            for (Field field : beanFields)
            {
                if(field.isAnnotationPresent(AutoWired.class))
                {
                    Class<?> fieldClass = field.getType();
                    String fieldName = fieldClass.getName();
                    Object fieldInstance = beans.get(fieldName);

                    if(fieldInstance == null) //接口
                    {
                        for (Map.Entry<String, Object> me : beans.entrySet())
                        {
                            String name = me.getKey();
                            Object object = me.getValue();
                            Class<?> clz = object.getClass();
                            Class<?>[] interfs = clz.getInterfaces();
                            if(interfs != null && interfs.length > 0)
                            {
                                Class<?> interf = interfs[0];
                                if(interf.getName().equals(fieldName))
                                {
                                    fieldInstance = object;
                                    break;
                                }
                            }
                        }
                    }

                    if(fieldInstance != null)
                        ReflectionUtil.setField(value, field, fieldInstance);
                    else
                        throw new SimpleException(fieldName + " not found!");
                }
            }
        }
    }

    @Override
    public void register (Object object)
    {
        beans.put(object.getClass().getName(), object);
    }

    @Override
    public Object getBean (String name) throws Exception
    {
        return beans.get(name);
    }

    @Override
    public void releaseBean ()
    {
        beans = null;
        logger.info("release all bean success.");
    }
}
