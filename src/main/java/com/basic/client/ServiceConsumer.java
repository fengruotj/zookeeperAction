package com.basic.client;

import com.basic.common.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * locate com.basic.client
 * Created by 79875 on 2017/6/2.
 */
public class ServiceConsumer<T> {
    public static final Logger logger= LoggerFactory.getLogger(ServiceConsumer.class);

    //用于等待SyncConnedcted 事件触发后继续执行当前线程
    private CountDownLatch countDownLatch=new CountDownLatch(1);

    //定义一个volatile 成员变量，用于保存最新的RMI地址（考虑到该变量或许会被其他线程所修改，一旦修改后该变量）
    private volatile List<String> urList=new ArrayList<>();

    private List<String> dataList;
    /**
     * 连接Zookeeper 服务器
     * @return
     */
    private ZooKeeper connectServer() throws IOException, InterruptedException {
        ZooKeeper zk= null;
        try {
            zk=new ZooKeeper(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState()== Event.KeeperState.SyncConnected){
                        countDownLatch.countDown();//唤醒当前正在执行的线程
                    }
                }
            });
            countDownLatch.await();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return zk;
    }

    public ServiceConsumer() throws IOException, InterruptedException {
        ZooKeeper zk= connectServer();
        if(zk!=null){
            watchNode(zk);//观察/registry节点的所有子节点并更新urlList成员变量
        }
    }

    /**
     * 观察/register节点下所有子节点是否有变化
     * @param zk ZooKeeper客户端
     */
    public void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList=zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged){
                       watchNode(zk);//若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
                    }
                }
            });
            List<String> dataList=new ArrayList<>();//用于存放/registry 中所有子节点中的数据
            for(String node :nodeList){
                byte[] data=zk.getData(Constant.ZK_REGISTRY_PATH+"/"+node,false,null);
                dataList.add(new String(data));
            }
            logger.debug("node data: {}",dataList);
            urList=dataList;//更新最新的RMI地址
        } catch (Exception e) {
        }
    }

    public <T extends Remote> T lookup() throws Exception {
        T service=null;
        int size=urList.size();
        if(size>0){
            String url;
            if(size==1){
                url=urList.get(0);//若urlList中只有一个元素，则直接获取该元素
                logger.info(url);
            }else {
                url=urList.get(ThreadLocalRandom.current().nextInt(size));
                logger.debug("using random url:{}",url);
                logger.info(url);
            }
            service=lookupService(url);
        }
        return service;
    }

    public <T extends Remote> T lookupService(String url) throws Exception {
       return (T) Naming.lookup(url);
    }
}
