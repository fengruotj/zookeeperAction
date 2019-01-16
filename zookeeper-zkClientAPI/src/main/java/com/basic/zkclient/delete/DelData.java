package com.basic.zkclient.delete;

import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.ZkClient;

//ZkClient删除节点数据
public class DelData {
	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
    	ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
        //zkClient.createPersistent(path, "");
        //zkClient.createPersistent(path+"/c1", "");
        zkClient.deleteRecursive(path);
    }
}
