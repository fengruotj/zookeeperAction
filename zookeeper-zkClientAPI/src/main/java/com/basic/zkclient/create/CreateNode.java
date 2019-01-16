package com.basic.zkclient.create;
import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.ZkClient;

// 使用ZkClient创建节点
public class CreateNode {

    public static void main(String[] args) throws Exception {
    	ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
        String path = "/zk-book/c1";
        zkClient.createPersistent(path, true);
    }
}
