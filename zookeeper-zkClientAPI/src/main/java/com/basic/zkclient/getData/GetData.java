package com.basic.zkclient.getData;
import com.basic.zkclient.common.Constant;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

//ZkClient获取节点数据
public class GetData {
    public static void main(String[] args) throws Exception {
    	
    	String path = "/zk-book";
        ZkClient zkClient = new ZkClient(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT);
        zkClient.createEphemeral(path, "123");
        
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("Node " + dataPath + " deleted.");
            }
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("Node " + dataPath + " changed, new data: " + data);
            }
        });
        
        System.out.println((String) zkClient.readData(path));
        zkClient.writeData(path,"456");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep( Integer.MAX_VALUE );
    }
}
