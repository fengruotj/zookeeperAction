package com.basic;

import com.basic.common.Constant;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.basic.common.Constant.parentNode;

/**
 * locate com.basic
 * Created by mastertj on 2018/5/16.
 */
public class DistributedClient {
    private ZooKeeper zkClient=null;
    //系统服务器列表
    private volatile List<String> serversList=new ArrayList<>();
    //当前连接的服务器机器
    private String currentServer;

    private Random random=new Random();
    /**
     * 获取服务器连接
     * @throws Exception
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
            zkClient.create(parentNode,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    /**
     * 获取服务器列表并且更新服务器列表
     */
    public List<String>  getServerList() throws KeeperException, InterruptedException {
        //获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zkClient.getChildren(parentNode, new Watcher() {
            /**
             * 监听getChildren() 数据节点的变化
             * @param event
             */
            @Override
            public void process(WatchedEvent event) {
                //收到事件后的回调函数（应该是我们自己定义的事件逻辑处理）
                System.out.println(event.getType()+"----"+event.getPath());

                //再次注册监听器...
                try {
                    //2.获取/servers/子节点信息 从中获取服务器信息列表，并更新。
                    // 监听/servers/子节点变化，如/servers/子节点发生变化 重新获取服务器数据节点/servers 列表，并且重新更新服务器列表

                    serversList=getServerList();
                    //打印服务器列表
                    System.out.println("servers: "+serversList);

                    if(!serversList.contains(currentServer)){
                        //如果当前连接的服务器宕机,则重启新的服务
                        int i = random.nextInt(serversList.size());
                        currentServer=serversList.get(i);
                        System.out.println("client start working......"+currentServer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        List<String> servers=new ArrayList<>();
        for(String nodeName :children){
            byte[] data = zkClient.getData(parentNode + "/" + nodeName, false, null);
            servers.add(new String(data));
        }
       return servers;
    }

    /**
     * 客户端业务功能调用
     * @param
     */
    public void handleBusiness() throws InterruptedException {
        int i = random.nextInt(serversList.size());
        currentServer=serversList.get(i);
        System.out.println("client start working......"+currentServer);
        Thread.sleep(Long.MAX_VALUE);
    }

    public List<String> getServersList() {
        return serversList;
    }

    public void Main() throws Exception {
        //1.获取zk连接
        this.getConnect();
        //2.获取/servers/子节点信息 从中获取服务器信息列表，并更新。
        // 监听/servers/子节点变化，如/servers/子节点发生变化 重新获取服务器数据节点/servers 列表，并且重新更新服务器列表
        serversList=this.getServerList();

        //业务线程调用
        this.handleBusiness();
    }

    public static void main(String[] args) throws Exception {
       DistributedClient distributedClient=new DistributedClient();
       distributedClient.Main();
    }

}
