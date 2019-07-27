package t_demo.configration;

import framework.annotation.Configuration;
import framework.configuration.AbstractConfiguration;


@Configuration
public class RedisConfiguration extends AbstractConfiguration
{
    @Override
    public String setPropertiesName ()
    {
        return "redis.properties";
    }
}
