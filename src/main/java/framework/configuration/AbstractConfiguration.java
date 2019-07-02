package framework.configuration;

public abstract class AbstractConfiguration
{
    public abstract String setPropertiesName ();

    public String getPropertiesName()
    {
        return setPropertiesName();
    }
}
