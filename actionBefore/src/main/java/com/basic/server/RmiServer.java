package com.basic.server;

import com.basic.service.impl.HelloServiceImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * locate com.com.basic.main
 * Created by 79875 on 2017/6/2.
 */
public class RmiServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        int port=1098;
        String url="rmi://localhost:1098/com.com.basic.service.impl.HelloServiceImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url,new HelloServiceImpl());
    }
}
