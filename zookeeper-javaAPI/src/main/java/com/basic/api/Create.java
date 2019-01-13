package com.basic.api;

import com.basic.common.Constant;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * locate com.basic.api
 * Created by mastertj on 2018/5/16.
 * Zookeeper 数据节点的创建
 */
public class Create {
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

        /**
         * 第一个参数数据节点的路径
         * 第二个参数 传入数据字节
         * 第三个参数 权限控制
         * 第四个参数 数据节点的类型
         */
        String s = zkClient.create("/TJ/testCreate", "helloZk".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("创建数据节点成功："+s);

        //关闭Zookeeper 客户端
        zkClient.close();
    }
}
