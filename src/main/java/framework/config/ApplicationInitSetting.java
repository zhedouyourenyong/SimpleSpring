package framework.config;

import Test.test;
import framework.ApplicationServer;
import framework.bean.impl.DefaultBeanFactory;
import framework.configuration.AbstractConfiguration;
import framework.configuration.ApplicationConfiguration;
import framework.configuration.ConfigurationManager;
import framework.constant.Constant;
import framework.exception.SimpleException;
import framework.interceptor.InterceptManager;
import framework.route.RouterManager;
import framework.scanner.ClassScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static framework.configuration.ConfigurationManager.getConfiguration;

public final class ApplicationInitSetting
{
    public static void setting (Class<?> clazz, String rootPath) throws Exception
    {
        //Initialize the application configuration
        initConfiguration(clazz);

        //Set application configuration
        setAppConfig(rootPath);

        //init route bean interceptor factory
        String packagetName= clazz.getPackage().getName();
        DefaultBeanFactory.getInstance().init(packagetName);
        RouterManager.getInstance().init(packagetName);
        InterceptManager.getInstance().loadInterceptors(packagetName);
    }


    private static void setAppConfig (String rootPath)
    {
        Properties properties=ConfigurationManager.getConfiguration(ApplicationConfiguration.class);

        if(rootPath == null)
        {
            rootPath =(String) properties.get(Constant.ROOT_PATH);
        }
        String port = (String) properties.get(Constant.CICADA_PORT);

        if(rootPath == null)
        {
            throw new SimpleException("No [root.path] exists ");
        }
        if(port == null)
        {
            throw new SimpleException("No [port] exists ");
        }
        AppConfig.getInstance().setRootPath(rootPath);
        AppConfig.getInstance().setPort(Integer.parseInt(port));
    }


    private static void initConfiguration (Class<?> clazz) throws Exception
    {
        AppConfig.getInstance().setRootPackageName(clazz);

        List<Class<?>> configuration = ClassScanner.getConfiguration(AppConfig.getInstance().getRootPackageName());
        for (Class<?> cls : configuration)
        {
            AbstractConfiguration conf = (AbstractConfiguration) cls.newInstance();

            String fileName=conf.getPropertiesName();
            InputStream stream = ApplicationServer.class.getClassLoader().getResourceAsStream(fileName);

            Properties properties = new Properties();
            properties.load(stream);

            if(stream!=null)
                stream.close();

            ConfigurationManager.addConfiguration(cls.getName(), properties);
        }
    }
}
