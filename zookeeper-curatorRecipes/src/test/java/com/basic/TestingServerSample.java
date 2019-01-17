package com.basic;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Test;

import java.io.File;

public class TestingServerSample {

	@Test
	public void test() throws Exception {
		String path = "/zookeeper";
		TestingServer server = new TestingServer(2181,new File("/home/admin/zk-book-data"));
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
	            .connectString(server.getConnectString())
	            .sessionTimeoutMs(5000)
	            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
	            .build();
        client.start();
        System.out.println( client.getChildren().forPath( path ));
        server.close();
	}
}
