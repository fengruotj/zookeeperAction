package com.basic.client;

import com.basic.service.HelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * locate com.basic.main
 * Created by 79875 on 2017/6/2.
 */
public class RmiClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        int port=1098;
        String url="rmi://localhost:1098/com.basic.service.impl.HelloServiceImpl";
        HelloService helloService= (HelloService) Naming.lookup(url);
        String result=helloService.sayHello("tanjie");
        System.out.println(result);
    }
}
