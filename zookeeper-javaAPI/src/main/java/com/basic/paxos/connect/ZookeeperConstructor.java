package com.basic.paxos.connect;

import com.basic.common.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * locate com.basic.paxos.connect
 * Created by mastertj on 2019/1/15.
 */
public class ZookeeperConstructor implements Watcher{
    private static CountDownLatch countDownLatch=new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper=new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,new ZookeeperConstructor());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Zookeeper session established");
    }

    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected==event.getState()){
            countDownLatch.countDown();
        }
    }
}
