package framework.protocol.param;

import java.util.Map;


public interface Param
{
    String getString (String param);

    Integer getInteger (String param);

    Long getLong (String param);

    Double getDouble (String param);

    Float getFloat (String param);

    Boolean getBoolean (String param);
}
