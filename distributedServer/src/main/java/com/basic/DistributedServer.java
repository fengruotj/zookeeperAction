package com.basic;

import com.basic.common.Constant;
import org.apache.zookeeper.*;

import java.io.IOException;

import static com.basic.common.Constant.parentNode;

/**
 * locate com.basic
 * Created by mastertj on 2018/5/16.
 * 分布式系统服务器动态上下线(高可用)
 */
public class DistributedServer {
    private ZooKeeper zkClient=null;

    public DistributedServer() {

    }

    /**
     * 获取服务器连接
     * @throws IOException
     */
    public void getConnect() throws Exception {
        if(zkClient==null){
            zkClient=new ZooKeeper(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    //收到事件后的回调函数（应该是我们自己定义的事件逻辑处理）
                    System.out.println(event.getType()+"----"+event.getPath());
                }
            });
        }

        if(zkClient.exists(parentNode,false)==null){
            zkClient.create(parentNode,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
    }

    /**
     * 注册服务器信息
     * @param hostname
     */
    public void registerServers(String hostname) throws KeeperException, InterruptedException {
        String create = zkClient.create(parentNode + "/Server", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname+" is online...."+create);
    }

    /**
     * 处理服务器业务功能
     */
    public void handleServices(String hostname) throws InterruptedException {
        System.out.println(hostname+" is services....");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        DistributedServer distributedServer=new DistributedServer();
        //1.获取zk连接
        distributedServer.getConnect();
        String hostname=args[0];
        //2.利用zk连接注册服务器信息
        distributedServer.registerServers(hostname);
        //3.启用服务器业务功能
        distributedServer.handleServices(hostname);
    }
}
