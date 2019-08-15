package framework.interceptor;

import framework.config.AppConfig;
import framework.context.ContextManager;
import framework.protocol.param.Param;
import framework.scanner.ClassScanner;
import framework.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InterceptManager
{
    private volatile static InterceptManager interceptManager;
    private static List<SimpleInterceptor> interceptorList;
    private AppConfig appConfig = AppConfig.getInstance();

    private InterceptManager ()
    {
    }

    public static InterceptManager getInstance ()
    {
        if(interceptManager == null)
        {
            synchronized (InterceptManager.class)
            {
                if(interceptManager == null)
                {
                    interceptManager = new InterceptManager();
                }
            }
        }
        return interceptManager;
    }

    public List<SimpleInterceptor> loadInterceptors (String packageName) throws Exception
    {
        if(interceptorList == null)
        {
            interceptorList = new ArrayList<>(8);
            Map<Class<?>, Integer> interceptMap = ClassScanner.getInterceptorMap(packageName);
            for (Map.Entry<Class<?>, Integer> entry : interceptMap.entrySet())
            {
                Class<?> cls = entry.getKey();
                Integer order = entry.getValue();
                SimpleInterceptor interceptor = (SimpleInterceptor) ReflectionUtil.newInstance(cls);
                interceptor.setOrder(order);
                interceptorList.add(interceptor);
            }
            Collections.sort(interceptorList, new OrderComparator());
        }
        return interceptorList;
    }

    public boolean processBefore (Param param) throws Exception
    {
        for (SimpleInterceptor interceptor : interceptorList)
        {
            boolean result = interceptor.before(ContextManager.getContext(), param);
            if(!result)
                return result;
        }
        return true;
    }

    public void processAfter (Param param) throws Exception
    {
        for (SimpleInterceptor interceptor : interceptorList)
        {
            interceptor.after(ContextManager.getContext(), param);
        }
    }
}
