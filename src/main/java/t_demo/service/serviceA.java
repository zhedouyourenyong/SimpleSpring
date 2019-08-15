package t_demo.service;

import framework.annotation.Service;
import t_demo.interF.gg;

@Service
public class serviceA implements gg
{
    public void say()
    {
        System.out.println("hello serviceA！");
    }
}
