package framework.scanner;

import framework.annotation.*;
import framework.configuration.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner
{
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    private static Set<Class<?>> classes = null;
    private static Set<Class<?>> beanClasses = null;  // controller service interceptor configuration

    private static Map<String, Class<?>> controllerMap = null;
    private static Map<String, Class<?>> serviceMap = null;
    private static Map<Class<?>, Integer> interceptorMap = null;
    private static List<Class<?>> configurationList = null;
    private static Map<String, Class<?>> proxyMap = null;


    public static List<Class<?>> getConfiguration (String packageName) throws Exception
    {
        if(configurationList == null)
        {
            Set<Class<?>> beanSet = getBeanClasses(packageName);
            configurationList = new ArrayList<>();

            //
            configurationList.add(ApplicationConfiguration.class);

            if(beanSet == null || beanSet.isEmpty())
                return configurationList;

            for (Class<?> cls : beanSet)
            {
                if(cls.isAnnotationPresent(Configuration.class))
                {
                    configurationList.add(cls);
                }
            }
        }
        return configurationList;
    }

    public static Map<String, Class<?>> getProxyMap (String packageName) throws Exception
    {
        if(proxyMap == null)
        {
            Set<Class<?>> classes = getBeanClasses(packageName);
            if(classes == null || classes.isEmpty())
                return proxyMap;

            proxyMap = new HashMap<>();
            for (Class<?> cls : classes)
            {
                if(cls.isAnnotationPresent(Aspect.class))
                {
                    proxyMap.put(cls.getName(), cls);
                }
            }
        }
        return proxyMap;
    }

    public static Map<String, Class<?>> getControllerMap (String packageName) throws Exception
    {
        if(controllerMap == null)
        {
            Set<Class<?>> classes = getBeanClasses(packageName);
            if(classes == null || classes.isEmpty())
                return controllerMap;

            controllerMap = new HashMap<>();
            for (Class<?> cls : classes)
            {
                if(cls.isAnnotationPresent(Controller.class))
                {
                    controllerMap.put(cls.getName(), cls);
//                    Controller controller=cls.getAnnotation(Controller.class);
//                    controllerMap.put(controller.name().equals("")?cls.getName():controller.name(),cls);
                }
            }
        }
        return controllerMap;
    }

    public static Map<String, Class<?>> getServiceMap (String packageName) throws Exception
    {
        if(serviceMap == null)
        {
            Set<Class<?>> classes = getBeanClasses(packageName);
            if(classes == null || classes.isEmpty())
                return serviceMap;

            serviceMap = new HashMap<>();
            for (Class<?> cls : classes)
            {
                if(cls.isAnnotationPresent(Service.class))
                {
                    serviceMap.put(cls.getName(), cls);
//                    Service service=cls.getAnnotation(Service.class);
//                    serviceMap.put(service.name().equals("")?cls.getName():service.name(),cls);
                }
            }
        }
        return serviceMap;
    }

    public static Map<Class<?>, Integer> getInterceptorMap (String packageName) throws Exception
    {
        if(interceptorMap == null)
        {
            Set<Class<?>> classes = getBeanClasses(packageName);
            if(classes == null || classes.isEmpty())
                return interceptorMap;

            interceptorMap = new HashMap<>();
            for (Class<?> cls : classes)
            {
                if(cls.isAnnotationPresent(Interceptor.class))
                {
                    Interceptor interceptor = cls.getAnnotation(Interceptor.class);
                    interceptorMap.put(cls, interceptor.order());
                }
            }
        }
        return interceptorMap;
    }

    public static Set<Class<?>> getBeanClasses (String packageName) throws Exception
    {
        if(beanClasses == null)
        {
            Set<Class<?>> classes = getClasses(packageName);
            if(classes == null || classes.isEmpty())
                return beanClasses;

            beanClasses = new HashSet<>();
            for (Class<?> cls : classes)
            {
                if(cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Service.class)
                        || cls.isAnnotationPresent(Interceptor.class) || cls.isAnnotationPresent(Configuration.class)
                        || cls.isAnnotationPresent(Aspect.class))
                {
                    beanClasses.add(cls);
                }
            }
        }
        return beanClasses;
    }

    public static Set<Class<?>> getClasses (String packageName) throws Exception
    {
        if(classes == null)
        {
            classes = new HashSet<>(64);
            baseScanner(packageName, classes);
        }
        return classes;
    }


    private static void baseScanner (String packageName, Set set)
    {
        boolean recursive = true;

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try
        {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements())
            {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if("file".equals(protocol))
                {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, set);
                } else if("jar".equals(protocol))
                {
                    JarFile jar;
                    try
                    {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements())
                        {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if(name.charAt(0) == '/')
                            {
                                name = name.substring(1);
                            }
                            if(name.startsWith(packageDirName))
                            {
                                int idx = name.lastIndexOf('/');
                                if(idx != -1)
                                {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if((idx != -1) || recursive)
                                {
                                    if(name.endsWith(".class") && !entry.isDirectory())
                                    {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try
                                        {
                                            set.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e)
                    {
                        logger.error("IOException", e);
                    }
                }
            }
        } catch (IOException e)
        {
            logger.error("IOException", e);
        }
    }


    public static void findAndAddClassesInPackageByFile (String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes)
    {
        File dir = new File(packagePath);
        if(!dir.exists() || !dir.isDirectory())
        {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : files)
        {
            if(file.isDirectory())
            {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else
            {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try
                {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e)
                {
                    logger.error("ClassNotFoundException", e);
                }
            }
        }
    }
}