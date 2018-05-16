package com.basic.server;

import com.basic.service.HelloService;
import com.basic.service.impl.HelloServiceImpl;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * locate com.com.basic.server
 * Created by 79875 on 2017/6/2.
 */
public class Server  {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        //当前RMI服务器的ip地址 和 端口
        String host="211.69.197.83";
        int port=Integer.parseInt("11235");
        ServiceProvider provider=new ServiceProvider();
        HelloService helloService=new HelloServiceImpl();
        provider.publish(helloService,host,port);

        Thread.sleep(Long.MAX_VALUE);
    }
}
