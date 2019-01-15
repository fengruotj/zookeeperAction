package com.basic.paxos.delete;
import com.basic.common.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

// ZooKeeper API 删除节点，使用同步(sync)接口。
public class DeleteSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) throws Exception {

    	String path = "/zk-book";
    	zk = new ZooKeeper(Constant.ZK_CONNECT_STRING,
                Constant.ZK_SESSION_TIMEOUT, //
				new DeleteSync());
    	connectedSemaphore.await();

    	//zk.create( path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL );
    	zk.delete( path, -1 );
    	
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
