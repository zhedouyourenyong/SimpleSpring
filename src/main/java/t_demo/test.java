package t_demo;

import framework.ApplicationServer;

public class test
{
    public static void main (String[] args) throws Exception
    {
        try
        {
            ApplicationServer.start(test.class, "/example");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
