package framework.bean.impl;

import framework.annotation.AutoWired;
import framework.annotation.Resource;
import framework.bean.BeanFactory;
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
    private Map<String, Object> beans = new HashMap<>(16);  //key --> className


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
        startDI();
    }

    private void startIOC (String packageName) throws Exception
    {
        Set<Class<?>> beanClasses = ClassScanner.getBeanClasses(packageName);

        for (Class<?> cls : beanClasses)
        {
            Object object = ReflectionUtil.newInstance(cls);
            register(object);
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
                    Object fieldInstance = beans.get(fieldClass.getName());
                    if(fieldInstance != null)
                        ReflectionUtil.setField(value, field, fieldInstance);
                } else if(field.isAnnotationPresent(Resource.class))
                {
                    Resource resource = field.getAnnotation(Resource.class);
                    Object fieldInstance = beans.get(resource.name());
                    if(fieldInstance != null)
                        ReflectionUtil.setField(key, field, fieldInstance);
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