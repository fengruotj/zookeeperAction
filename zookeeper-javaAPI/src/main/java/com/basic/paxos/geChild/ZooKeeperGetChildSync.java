package com.basic.paxos.geChild;
import com.basic.common.Constant;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

// ZooKeeper API 获取子节点列表，使用同步(sync)接口。
public class ZooKeeperGetChildSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    
    public static void main(String[] args) throws Exception{

    	String path = "/zk-book";
        zk = new ZooKeeper(Constant.ZK_CONNECT_STRING,
                Constant.ZK_SESSION_TIMEOUT, //
				new ZooKeeperGetChildSync());
        connectedSemaphore.await();
        zk.create(path, "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path+"/c1", "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        
        List<String> childrenList = zk.getChildren(path, true);
        System.out.println(childrenList);
        
        zk.create(path+"/c2", "".getBytes(), 
        		  Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        
        Thread.sleep( Integer.MAX_VALUE );
    }
    public void process(WatchedEvent event) {
      if (KeeperState.SyncConnected == event.getState()) {
        if (EventType.None == event.getType() && null == event.getPath()) {
            //说明事件通知是连接建立事件通知
            connectedSemaphore.countDown();
        } else if (event.getType() == EventType.NodeChildrenChanged) {
            try {
                System.out.println("ReGet Child:"+zk.getChildren(event.getPath(),true));
            } catch (Exception e) {}
        }
      }
    }
}
