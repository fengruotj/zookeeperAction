package com.basic.zkclient.setData;

import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.ZkClient;

//ZkClient更新节点数据
public class SetData {

    public static void main(String[] args) throws Exception {
    	String path = "/zk-book";
    	ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
        zkClient.createEphemeral(path, new Integer(1));
        zkClient.writeData(path, new Integer(1));
    }
}
