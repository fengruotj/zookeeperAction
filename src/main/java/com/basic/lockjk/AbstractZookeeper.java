package com.basic.lockjk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * locate com.basic.util
 * Created by 79875 on 2017/6/6.
 */
public class AbstractZookeeper implements Watcher{
    public static final Logger logger= LoggerFactory.getLogger(AbstractZookeeper.class);

    private ZooKeeper zk;

    protected CountDownLatch countDownLatch=new CountDownLatch(1);

    public ZooKeeper connect(String host,int SESSION_TIMEOUT) throws IOException, InterruptedException {
        zk=new ZooKeeper(host,SESSION_TIMEOUT,this);
        countDownLatch.await();
        logger.info("AbstractZookeeper conncect() ");
        return zk;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState()== Event.KeeperState.SyncConnected){
            countDownLatch.countDown();//唤醒当前正在执行的线程
        }
    }

    public void close() throws InterruptedException {
        zk.close();
    }
}
