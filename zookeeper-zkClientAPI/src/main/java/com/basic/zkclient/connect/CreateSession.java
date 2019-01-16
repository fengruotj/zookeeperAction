package com.basic.zkclient.connect;
import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;

// 使用ZkClient来创建一个ZooKeeper客户端
public class CreateSession {
    public static void main(String[] args) throws IOException, InterruptedException {
    	ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
    	System.out.println("ZooKeeper session established.");
    }
}
