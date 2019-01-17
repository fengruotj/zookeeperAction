package com.basic.recipes.distributedAtomicInt;
import com.basic.recipes.common.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

// 使用Curator实现分布式计数器
public class Recipes_DistAtomicInt {

	static String distatomicint_path = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(Constant.ZK_CONNECT_STRING)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
	public static void main( String[] args ) throws Exception {
		client.start();
		DistributedAtomicInteger atomicInteger = 
		new DistributedAtomicInteger( client, distatomicint_path, 
									new RetryNTimes( 3, 1000 ) );
		AtomicValue<Integer> rc = atomicInteger.add( 8 );
		System.out.println( "Result: " + rc.succeeded() );
	}
}
