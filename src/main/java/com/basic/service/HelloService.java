package com.basic.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * locate com.basic.service
 * Created by 79875 on 2017/6/2.
 */
public interface HelloService extends Remote{
   //这里必须抛出throws RemoteException 不然报错
   public String sayHello(String name) throws RemoteException;
}
