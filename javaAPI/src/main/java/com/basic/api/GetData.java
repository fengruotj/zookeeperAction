package com.basic.api;

import com.basic.common.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * locate com.basic.api
 * Created by mastertj on 2018/5/16.
 * 获取znode数据
 */
public class GetData {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        //zkClient 初始化
        ZooKeeper zkClient=null;
        zkClient=new ZooKeeper(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //收到事件后的回调函数（应该是我们自己定义的事件逻辑处理）
                System.out.println(event.getType()+"----"+event.getPath());
            }
        });

        //参数设置为true，表示监听当前数据节点数据改变的监听事件
        byte[] data = zkClient.getData("/TJ/testCreate", false, null);
        System.out.println("获取数据: "+new String(data));

        //关闭Zookeeper 客户端
        zkClient.close();
    }
}
