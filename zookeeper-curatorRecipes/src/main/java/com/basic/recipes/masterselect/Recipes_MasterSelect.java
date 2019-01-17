package com.basic.recipes.masterselect;

import com.basic.recipes.common.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Recipes_MasterSelect {

	static String master_path = "/curator_recipes_master_path";
	
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Constant.ZK_CONNECT_STRING)
			.sessionTimeoutMs(Constant.ZK_SESSION_TIMEOUT)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
    public static void main( String[] args ) throws Exception {
    	client.start();
        LeaderSelector selector = new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
		            public void takeLeadership(CuratorFramework client) throws Exception {
		                System.out.println("成为Master角色");
		                Thread.sleep( 3000 );
		                System.out.println( "完成Master操作，释放Master权利" );
		            }
		        });
        //autoRequeue()方法的调用确保此实例在释放领导权后还可能获得领导
        selector.autoRequeue();
        selector.start();
        Thread.sleep( Integer.MAX_VALUE );
	}
}
