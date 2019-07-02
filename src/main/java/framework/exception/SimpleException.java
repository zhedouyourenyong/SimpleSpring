package framework.exception;

import framework.enums.StatusEnum;

public class SimpleException extends GenericException
{


    public SimpleException (String errorCode, String errorMessage)
    {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SimpleException (Exception e, String errorCode, String errorMessage)
    {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SimpleException (String message)
    {
        super(message);
        this.errorMessage = message;
    }

    public SimpleException (StatusEnum statusEnum)
    {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public SimpleException (StatusEnum statusEnum, String message)
    {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public SimpleException (Exception oriEx)
    {
        super(oriEx);
    }

    public SimpleException (Throwable oriEx)
    {
        super(oriEx);
    }

    public SimpleException (String message, Exception oriEx)
    {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public SimpleException (String message, Throwable oriEx)
    {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer (String msg)
    {
        if("Connection reset by peer".equals(msg))
        {
            return true;
        }
        return false;
    }

}
