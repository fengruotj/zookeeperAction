package com.basic.zkclient.exists;

import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.ZkClient;

//ZkClient检测节点是否存在
public class ExistNode {
    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
        System.out.println("Node " + path + " exists " + zkClient.exists(path));
    }
}
