package framework.configuration;


import framework.constant.Constant;

public class ApplicationConfiguration extends AbstractConfiguration
{
    @Override
    public String setPropertiesName ()
    {
        return Constant.SystemProperties.APPLICATION_PROPERTIES;
    }
}
