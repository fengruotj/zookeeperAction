package com.basic.api;

import com.basic.common.Constant;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * locate com.basic.api
 * Created by mastertj on 2018/5/16.
 * Zookeeper简单API增删改查的使用
 */
public class SimlpeZkClient {
    private ZooKeeper zkClient=null;

    /**
     * zkClient初始化
     * None----null表示连接被初始化
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        if(zkClient==null){
            zkClient=new ZooKeeper(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    //收到事件后的回调函数（应该是我们自己定义的事件逻辑处理）
                    System.out.println(event.getType()+"----"+event.getPath());
                }
            });
        }
    }

    /**
     * zkClient 关闭
     */
    @After
    public void close(){
        try {
            if(zkClient!=null)
                zkClient.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zookeeper 数据节点的创建
     */
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        /**
         * 第一个参数数据节点的路径
         * 第二个参数 传入数据字节
         * 第三个参数 权限控制
         * 第四个参数 数据节点的类型
         */
        String s = zkClient.create("/TJ/testCreate", "helloZk".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //上传任何数据都行，只要转换成字节bytes
        System.out.println("创建数据节点成功："+s);
    }

    /**
     * 获取当前数据节点的子节点，并且使用监听器监听
     */
    @Test
    public void getChildren() throws KeeperException, InterruptedException {
        //参数设置为true 表示使用之前定义的监听器Watcher监听
        for (String s : zkClient.getChildren("/TJ", true)) {
            System.out.println("childeren: "+s);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断znode节点是否存在
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void testExists() throws KeeperException, InterruptedException {
        //Stat 为数据节点元数据信息
        Stat exists = zkClient.exists("/TJ", false);
        System.out.println("数据节点/TJ 是否存在: "+exists==null?"false":"true");
    }

    /**
     * 获取znode数据
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getData() throws KeeperException, InterruptedException {
        //参数设置为true，表示监听当前数据节点数据改变的监听事件
        byte[] data = zkClient.getData("/TJ/testCreate", false, null);
        System.out.println("获取数据: "+new String(data));
    }

    /**
     * 删除znode数据节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void deleteData() throws KeeperException, InterruptedException {
        /**
         * 参数1 表示要删除数据节点的路径
         * 参数2 表示要删除数据节点的版本 -1表示所有版本
         */
        zkClient.delete("/TJ/bb",-1);
    }

    /**
     * 修改znode数据节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void setData() throws KeeperException, InterruptedException {
        //参数version 表示要修改数据节点的版本 -1表示所有版本
        zkClient.setData("/TJ/testCreate","11111".getBytes(),-1);
    }
}
