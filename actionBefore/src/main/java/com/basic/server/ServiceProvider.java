package com.basic.server;

import com.basic.common.Constant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * locate com.com.basic.server
 * Created by 79875 on 2017/6/2.
 */
public class ServiceProvider {
    public static final Logger logger= LoggerFactory.getLogger(ServiceProvider.class);

    //用于等待SyncConnedcted 事件触发后继续执行当前线程
    private CountDownLatch countDownLatch=new CountDownLatch(1);

    /**
     * 发布RMI服务并主持RMi地址到zookeeper
     * @param remote RMI服务
     * @param host 主机名字
     * @param port 端口号
     */
    public void publish(Remote remote,String host,int port) throws IOException, InterruptedException, KeeperException {
        String url=publishService(remote,host,port);
        if(url !=null){
            ZooKeeper zk=connectServer();
            if (zk !=null){
                createNode(zk,url);
            }
        }
        LocateRegistry.createRegistry(port);
        Naming.rebind(url,remote);
    }

    /**
     * 返回RMI服务字符串
     * @param remote RMI服务
     * @param host 主机名字
     * @param port 端口号
     * @return
     */
    public String publishService(Remote remote,String host,int port){
        String url="";
        try {
            url=String.format("rmi://%s:%d/%s",host,port,remote.getClass().getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

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

    private void createNode(ZooKeeper zk,String url) throws KeeperException, InterruptedException {
        byte[] data=url.getBytes();
        String path=zk.create(Constant.ZK_PROVIDER_PATH,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);//创建一个临时有序的Znode
        logger.debug("create zookeeper node ({} => {})",path,url);
    }
}
