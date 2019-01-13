package com.basic.service.impl;

import com.basic.service.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * locate com.com.basic.service
 * Created by 79875 on 2017/6/2.
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    public HelloServiceImpl() throws RemoteException {

    }

    public String sayHello(String name) throws RemoteException{
        return String.format("hello %s",name);
}
}
