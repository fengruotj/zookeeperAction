package com.basic.paxos.create;

import com.basic.common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.concurrent.CountDownLatch;

// ZooKeeper API创建节点，使用异步(async)接口。
public class ZooKeeperCreateASync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        
	 ZooKeeper zookeeper = new ZooKeeper(Constant.ZK_CONNECT_STRING,
			 Constant.ZK_SESSION_TIMEOUT, //
				new ZooKeeperCreateASync());
	 connectedSemaphore.await();
	    
	 zookeeper.create("/zk-test-ephemeral-", "".getBytes(), 
	    		Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, 
	    		new IStringCallback(), "I am context.");
	    
	 zookeeper.create("/zk-test-ephemeral-", "".getBytes(), 
	    		Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, 
	    		new IStringCallback(), "I am context.");
	    
	 zookeeper.create("/zk-test-ephemeral-", "".getBytes(), 
	    		Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, 
	    		new IStringCallback(), "I am context.");
	 Thread.sleep( Integer.MAX_VALUE );
    }
    
    public void process(WatchedEvent event) {
		if (Event.KeeperState.SyncConnected == event.getState()) {
			if (Event.EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			}
		}
    }
}
class IStringCallback implements AsyncCallback.StringCallback{
  public void processResult(int rc, String path, Object ctx, String name) {
    System.out.println("Create path result: [" + rc + ", " + path + ", "
                   + ctx + ", real path name: " + name);
    }
  }
