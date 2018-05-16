package com.basic.api;

import com.basic.common.Constant;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * locate com.basic.api
 * Created by mastertj on 2018/5/16.
 * 获取当前数据节点的子节点，并且使用监听器监听
 */
public class GetChildren {
    private static ZooKeeper zkClient=null;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        //zkClient 初始化

        zkClient=new ZooKeeper(Constant.ZK_CONNECT_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //收到事件后的回调函数（应该是我们自己定义的事件逻辑处理）
                System.out.println(event.getType()+"----"+event.getPath());

                //再次注册监听器...
                try {
                    zkClient.getChildren("/TJ", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //参数设置为true 表示使用之前定义的监听器Watcher监听
        //但是监听器只能监听一次事件，要想一直监听事件，就必须在Watcher的回调函数中再次注册监听器
        for (String s : zkClient.getChildren("/TJ", true)) {
            System.out.println("childeren: "+s);
        }
        Thread.sleep(Long.MAX_VALUE);
    }
}
