package com.basic.curator.connect;
import com.basic.curator.common.Constant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

//使用curator来创建一个含隔离命名空间的ZooKeeper客户端
public class CreateSessionNamespace {
    public static void main(String[] args) throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
        CuratorFrameworkFactory.builder()
                             .connectString(Constant.ZK_CONNECT_STRING)
                             .sessionTimeoutMs(Constant.ZK_SESSION_TIMEOUT)
                             .retryPolicy(retryPolicy)
                             .namespace("base")
                             .build();
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
