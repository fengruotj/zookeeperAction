package com.basic.paxos.auth;
import com.basic.common.Constant;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
//使用含权限信息的ZooKeeper会话创建数据节点
public class AuthSample {

    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper(Constant.ZK_CONNECT_STRING,Constant.ZK_SESSION_TIMEOUT,null);
        zookeeper.addAuthInfo("digest", "foo:true".getBytes());
        zookeeper.create( PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL );
        Thread.sleep( Integer.MAX_VALUE );
    }
}
