package com.basic.paxos.setdata;
import com.basic.common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

// ZooKeeper API 更新节点数据内容，使用同步(sync)接口。
public class SetDataSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

    	String path = "/zk-book";
    	zk = new ZooKeeper(Constant.ZK_CONNECT_STRING,
                Constant.ZK_SESSION_TIMEOUT, //
				new SetDataSync());
    	connectedSemaphore.await();
    	
        zk.create( path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
        zk.getData( path, true, null );
        
        Stat stat = zk.setData( path, "456".getBytes(), -1 );
        System.out.println(stat.getCzxid()+","+
			        	   stat.getMzxid()+","+
			        	   stat.getVersion());
        Stat stat2 = zk.setData( path, "456".getBytes(), stat.getVersion() );
        System.out.println(stat2.getCzxid()+","+
	        	   		   stat2.getMzxid()+","+
	        	   		   stat2.getVersion());
        try {
			zk.setData( path, "456".getBytes(), stat.getVersion() );
		} catch ( KeeperException e ) {
			System.out.println("Error: " + e.code() + "," + e.getMessage());
		}
        Thread.sleep( Integer.MAX_VALUE );
    }

    @Override
    public void process(WatchedEvent event) {
        if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}