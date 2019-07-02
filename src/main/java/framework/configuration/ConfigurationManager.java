package framework.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ConfigurationManager
{

    private static Map<String, Properties> config = new HashMap<>(8);


    public static void addConfiguration (String key, Properties configuration)
    {
        config.put(key, configuration);
    }

    public static Properties getConfiguration (Class<? extends AbstractConfiguration> clazz)
    {
        return config.get(clazz.getName());
    }
}
