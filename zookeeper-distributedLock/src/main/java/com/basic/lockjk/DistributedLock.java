package com.basic.lockjk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;

/**
 * locate com.com.basic.lockjk
 * Created by 79875 on 2017/6/6.
 */
public class DistributedLock {
    public static final Logger logger= LoggerFactory.getLogger(DistributedLock.class);
    private String selfPath="";
    private String waitPath="";
    private String LOG_PREFIX_OP_THREAD=Thread.currentThread().getName();
    public static final String GROUP_PATH="/disLocks";
    public static final String SUB_PAHT="/disLocks/sub";

    private ZooKeeper zk;

    private Watcher watcher;

    public DistributedLock(ZooKeeper zk) {
        this.zk = zk;
    }

    public boolean getLock() throws KeeperException, InterruptedException {
        selfPath=zk.create(SUB_PAHT,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info(LOG_PREFIX_OP_THREAD+"创建路径"+selfPath);
        if(checkMinPath()){
            return true;
        }
        return false;
    }

    /**
     * 解除锁
     */
    public void unlock() throws KeeperException, InterruptedException {
        if(zk.exists(this.selfPath,false)==null){
            logger.error(LOG_PREFIX_OP_THREAD+"本节点不存在了。。。。");
            return;
        }
        zk.delete(this.selfPath,-1);
        logger.info(LOG_PREFIX_OP_THREAD+"删除本地节点"+selfPath);
        zk.close();
    }

    /**
     * 检查自己是否是最小 节点
     * @return
     */
    public boolean checkMinPath() throws KeeperException, InterruptedException {
        List<String> subNodes=zk.getChildren(GROUP_PATH,false);
        Collections.sort(subNodes);
        int index=subNodes.indexOf(selfPath.substring(GROUP_PATH.length()+1));
        switch (index){
            case -1:
                logger.error(LOG_PREFIX_OP_THREAD+"本节点不存在了。。。。");
                return false;
            case 0:
                logger.error(LOG_PREFIX_OP_THREAD+"队列中第一个 拿到Lock。。。");
                return true;
            default:
                this.waitPath=GROUP_PATH+"/"+subNodes.get(index-1);
                logger.info(LOG_PREFIX_OP_THREAD+"获取子节点中排在我前面的"+waitPath);
                try {
                    zk.getData(waitPath,this.watcher,new Stat());
                    return false;
                } catch (KeeperException e) {
                    if(zk.exists(waitPath,false)==null){
                        logger.info(LOG_PREFIX_OP_THREAD+"字节点中，排在我前面的"+waitPath+"已经不见，表示运行完毕 可以拿到锁");
                        return checkMinPath();
                    }
                    else
                        throw e;
                }
        }
    }

    public boolean createPath(String path, String data) throws KeeperException, InterruptedException {
        if(zk.exists(path,false)==null){
            logger.info(LOG_PREFIX_OP_THREAD+" 节点创建成功，Path:"+this.zk.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
                +", content: "+data);
        }
        return true;
    }

    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    public String getWaitPath() {
        return waitPath;
    }

    public void setWaitPath(String waitPath) {
        this.waitPath = waitPath;
    }

}
