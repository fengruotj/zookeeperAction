package com.basic.recipes.util;
import com.basic.recipes.common.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

public class ZKPathsSample {

	static String path = "/curator_zkpath_sample";
	static CuratorFramework client = CuratorFrameworkFactory.builder()
			.connectString(Constant.ZK_CONNECT_STRING)
			.sessionTimeoutMs( Constant.ZK_SESSION_TIMEOUT )
			.retryPolicy( new ExponentialBackoffRetry( 1000, 3 ) )
			.build();
	
	public static void main(String[] args) throws Exception {
		client.start();
		ZooKeeper zookeeper = client.getZookeeperClient().getZooKeeper();
		
		System.out.println(ZKPaths.fixForNamespace(path,"/sub"));
		System.out.println(ZKPaths.makePath(path, "/sub"));
		System.out.println( ZKPaths.getNodeFromPath( "/curator_zkpath_sample/sub1" ) );
		
		PathAndNode pn = ZKPaths.getPathAndNode( "/curator_zkpath_sample/sub1" );
		System.out.println(pn.getPath());
		System.out.println(pn.getNode());
		
		String dir1 = path + "/child1";
		String dir2 = path + "/child2";
		ZKPaths.mkdirs(zookeeper, dir1);
		ZKPaths.mkdirs(zookeeper, dir2);
		System.out.println(ZKPaths.getSortedChildren( zookeeper, path ));
		
		ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(), path, true);
	}
}