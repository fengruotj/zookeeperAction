package com.basic;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;
import org.junit.Test;

public class TestingClusterSample {

	@Test
	public void test() throws Exception{
		TestingCluster cluster = new TestingCluster(3);
		cluster.start();
		Thread.sleep(2000);
		
		TestingZooKeeperServer leader = null;
		for(TestingZooKeeperServer zs : cluster.getServers()){
			System.out.print(zs.getInstanceSpec().getServerId()+"-");
			System.out.print(zs.getQuorumPeer().getServerState()+"-");  
			System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
			if( zs.getQuorumPeer().getServerState().equals( "leading" )){
				leader = zs;
			}
		}
        leader.kill();
        System.out.println( "--After leader kill:" );
        for(TestingZooKeeperServer zs : cluster.getServers()){
			System.out.print(zs.getInstanceSpec().getServerId()+"-");
			System.out.print(zs.getQuorumPeer().getServerState()+"-");  
			System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
		}
        cluster.stop();
	}
}
