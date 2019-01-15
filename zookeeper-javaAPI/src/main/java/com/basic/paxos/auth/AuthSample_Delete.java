package com.basic.paxos.auth;
import com.basic.common.Constant;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

//删除节点的权限控制
public class AuthSample_Delete implements Watcher{

    final static String PATH  = "/zk-book-auth_test";
    final static String PATH2 = "/zk-book-auth_test/child";

    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper1 = new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,new AuthSample_Delete());
        zookeeper1.addAuthInfo("digest", "foo:true".getBytes());
        countDownLatch.await();;

        zookeeper1.create( PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT );
        zookeeper1.create( PATH2, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL );
        
        try {
			ZooKeeper zookeeper2 = new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,null);
			zookeeper2.delete( PATH2, -1 );
		} catch ( Exception e ) {
			System.out.println( "删除节点失败: " + e.getMessage() );
		}
        
        ZooKeeper zookeeper3 = new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,null);
        zookeeper3.addAuthInfo("digest", "foo:true".getBytes());
		zookeeper3.delete( PATH2, -1 );
        System.out.println( "成功删除节点：" + PATH2 );
        
        ZooKeeper zookeeper4 = new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,null);
		zookeeper4.delete( PATH, -1 );
        System.out.println( "成功删除节点：" + PATH );
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            }
        }
    }
}
