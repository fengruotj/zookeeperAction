package com.basic.lockjk;

import com.basic.common.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * locate com.com.basic.lockjk
 * Created by 79875 on 2017/6/6.
 */
public class LockService {
    public static final Logger logger= LoggerFactory.getLogger(AbstractZookeeper.class);
    public static final int THREAD_NUM=10;
    public static CountDownLatch countDownLatch=new CountDownLatch(THREAD_NUM);
    public static final String GROUP_PATH="/disLocks";
    public static final String SUB_PAHT="/disLocks/sub";
    public static final int  SESSION_TIMEOUT=10000;

    AbstractZookeeper az=new AbstractZookeeper();

    public void doService(DoTemplate doTemplate){
        try {
            ZooKeeper zk=az.connect(Constant.ZK_CONNECT_STRING,SESSION_TIMEOUT);
            DistributedLock dc=new DistributedLock(zk);
            LockWatcher lockWatcher=new LockWatcher(dc,doTemplate);
            dc.setWatcher(lockWatcher);
            dc.createPath(GROUP_PATH,"该节点由线程"+Thread.currentThread().getName()+"创建");
            boolean rs=dc.getLock();
            if(rs==true){
                lockWatcher.dosomething();
                dc.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
