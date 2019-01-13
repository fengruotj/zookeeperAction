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
 * 修改znode数据节点
 */
public class SetData {
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

        //参数version 表示要修改数据节点的版本 -1表示所有版本
        zkClient.setData("/TJ/testCreate","11111".getBytes(),-1);

        //关闭Zookeeper 客户端
        zkClient.close();
    }
}
