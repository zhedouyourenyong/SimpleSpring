package framework.protocol.param;

import java.util.HashMap;
import java.util.Map;

public class ParamMap implements Param
{
    private Map<String,Object> paramMap=null;

    public ParamMap(Map<String,Object> paramMap)
    {
        this.paramMap=paramMap;
    }

    @Override
    public String getString (String param)
    {
        return (String)paramMap.get(param);
    }

    @Override
    public Integer getInteger (String param)
    {
        return (Integer)paramMap.get(param);
    }

    @Override
    public Long getLong (String param)
    {
        return (Long)paramMap.get(param);
    }

    @Override
    public Double getDouble (String param)
    {
        return (Double)paramMap.get(param);
    }

    @Override
    public Float getFloat (String param)
    {
        return (Float)paramMap.get(param);
    }

    @Override
    public Boolean getBoolean (String param)
    {
        return (Boolean)paramMap.get(param);
    }
}
