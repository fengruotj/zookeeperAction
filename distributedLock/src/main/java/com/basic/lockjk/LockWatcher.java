package com.basic.lockjk;

import com.basic.main.TestLock;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * locate com.com.basic.lockjk
 * Created by 79875 on 2017/6/6.
 */
public class LockWatcher implements Watcher {
    public static final Logger logger= LoggerFactory.getLogger(LockWatcher.class);
    private String waitPath;
    private DistributedLock distributedLock;
    private DoTemplate doTemplate;

    public LockWatcher(DistributedLock distributedLock, DoTemplate doTemplate) {
        this.distributedLock = distributedLock;
        this.doTemplate = doTemplate;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType()==Event.EventType.NodeDeleted && event.getPath().equals(distributedLock.getWaitPath())){
            logger.info(Thread.currentThread().getName()+"排到我前面的家伙已经挂了 我是不是可以出山了？");
            try {
                if(distributedLock.checkMinPath()){
                    dosomething();
                    distributedLock.unlock();
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void dosomething() {
        logger.info(Thread.currentThread().getName()+"获取锁成功 赶紧干活！！");
        doTemplate.dodo();
        TestLock.countDownLatch.countDown();
    }

}
