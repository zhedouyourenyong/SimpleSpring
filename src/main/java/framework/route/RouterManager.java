package framework.route;

import framework.annotation.Controller;
import framework.annotation.RequestMapping;
import framework.config.AppConfig;
import framework.enums.StatusEnum;
import framework.exception.SimpleException;
import framework.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouterManager
{
    private static final Logger logger = LoggerFactory.getLogger(RouterManager.class);

    private static Map<String, Method> routes = null;

    private AppConfig appConfig = AppConfig.getInstance();
    private volatile static RouterManager routerScanner;

    private RouterManager ()
    {
    }

    public static RouterManager getInstance ()
    {
        if(routerScanner == null)
        {
            synchronized (RouterManager.class)
            {
                if(routerScanner == null)
                {
                    routerScanner = new RouterManager();
                }
            }
        }
        return routerScanner;
    }

    public void init (String packageName) throws Exception
    {
        loadRouteMethods(packageName);
    }

    public Method getProcessMethod (String path) throws Exception
    {
        Method method = routes.get(path);
        if(method == null)
            throw new SimpleException(StatusEnum.NOT_FOUND, path);

        return method;
    }

    private void loadRouteMethods (String packageName) throws Exception
    {
        if(routes == null)
        {
            routes = new HashMap<>(64);
        }

        Map<String, Class<?>> controllerMap = ClassScanner.getControllerMap(packageName);
        if(controllerMap != null && controllerMap.size() != 0)
        {
            for (Map.Entry<String, Class<?>> entry : controllerMap.entrySet())
            {
                String key = entry.getKey();
                Class<?> value = entry.getValue();

                Controller controller = value.getAnnotation(Controller.class);
                Method[] declaredMethods = value.getMethods();

                for (Method method : declaredMethods)
                {
                    if(method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String requestMapping = appConfig.getRootPath();

                        if(!controller.path().equals(""))
                        {
                            if(!controller.path().startsWith("/"))
                                requestMapping += "/";
                            requestMapping += controller.path();
                        }
                        if(!annotation.path().equals(""))
                        {
                            if(!annotation.path().startsWith("/"))
                                requestMapping += "/";
                            requestMapping += annotation.path();
                        }

                        routes.put(requestMapping, method);
                    }
                }
            }
        }
    }
}
