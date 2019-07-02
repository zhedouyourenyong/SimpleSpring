package Test.configration;

import framework.annotation.Configuration;
import framework.configuration.AbstractConfiguration;


@Configuration
public class KafkaConfiguration extends AbstractConfiguration
{

    @Override
    public String setPropertiesName ()
    {
        return "kafka.properties";
    }
}
