package framework.route;

import framework.bean.impl.DefaultBeanFactory;
import framework.context.Context;
import framework.context.ContextManager;
import framework.enums.StatusEnum;
import framework.exception.SimpleException;
import framework.protocol.param.Param;
import framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;


public final class RouteProcess
{

    private volatile static RouteProcess routeProcess;
    private final DefaultBeanFactory beanManager = DefaultBeanFactory.getInstance();

    public static RouteProcess getInstance ()
    {
        if(routeProcess == null)
        {
            synchronized (RouteProcess.class)
            {
                if(routeProcess == null)
                {
                    routeProcess = new RouteProcess();
                }
            }
        }
        return routeProcess;
    }

    public Object invoke (Method method) throws Exception
    {
        if(method == null)
        {
            return null;
        }

        Object[] object = parseRouteParameter(method);

        String beanName=method.getDeclaringClass().getName();
        Object bean = beanManager.getBean(beanName);

        Object result=null;
        if(bean!=null)
            result=ReflectionUtil.invokeMethod(bean,method,object);
        else
            throw new SimpleException("没有找到["+method.getName()+"]对应的bean");
        return result;
    }


    private Object[] parseRouteParameter (Method method) throws IllegalAccessException, InstantiationException, NoSuchFieldException
    {
        Class<?>[] parameterTypes=method.getParameterTypes();

        if(parameterTypes==null)
            return null;
        if(parameterTypes.length>2)
            throw new SimpleException(StatusEnum.ILLEGAL_PARAMETER);

        Object[] parameterInstances=new Object[parameterTypes.length];

        for(int i=0;i<parameterInstances.length;i++)
        {
            if(parameterTypes[i]==Context.class)
            {
                parameterInstances[i]=ContextManager.getContext();
            }else if(parameterTypes[i]== Param.class)
            {
                parameterInstances[i]=ContextManager.getContext().getParam();
            }
        }

        return parameterInstances;
    }


    private Object parseFieldValue (Field field, String value)
    {
        if(value == null)
        {
            return null;
        }

        Class<?> type = field.getType();
        if("".equals(value))
        {
            boolean base = type.equals(int.class) || type.equals(double.class) ||
                    type.equals(short.class) || type.equals(long.class) ||
                    type.equals(byte.class) || type.equals(float.class);
            if(base)
            {
                return 0;
            }
        }
        if(type.equals(int.class) || type.equals(Integer.class))
        {
            return Integer.parseInt(value);
        } else if(type.equals(String.class))
        {
            return value;
        } else if(type.equals(Double.class) || type.equals(double.class))
        {
            return Double.parseDouble(value);
        } else if(type.equals(Float.class) || type.equals(float.class))
        {
            return Float.parseFloat(value);
        } else if(type.equals(Long.class) || type.equals(long.class))
        {
            return Long.parseLong(value);
        } else if(type.equals(Boolean.class) || type.equals(boolean.class))
        {
            return Boolean.parseBoolean(value);
        } else if(type.equals(Short.class) || type.equals(short.class))
        {
            return Short.parseShort(value);
        } else if(type.equals(Byte.class) || type.equals(byte.class))
        {
            return Byte.parseByte(value);
        } else if(type.equals(BigDecimal.class))
        {
            return new BigDecimal(value);
        }

        return null;
    }

}
