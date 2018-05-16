package com.basic.main;

import com.basic.lockjk.DoTemplate;
import com.basic.lockjk.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * locate com.com.basic.main
 * Created by 79875 on 2017/6/6.
 */
public class TestLock {
    public static final Logger logger= LoggerFactory.getLogger(TestLock.class);
    public static final String ZK_CONNECT_STRING="root2:2181,root4:2181,root5:2181";
    public static final int THREAD_NUM=10;
    public static CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
    public static final String GROUP_PATH="/disLocks";
    public static final String SUB_PAHT="/disLocks/sub";

    public static void main(String[] args) {
        for(int i=0;i<THREAD_NUM;i++){
            final int threadID=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new LockService().doService(new DoTemplate() {
                            @Override
                            public void dodo() {
                                logger.info("我要修改一个文件。。。。。");
                            }
                        });
                    } catch (Exception e) {
                        logger.error("第"+threadID+"个线程】抛出异常！！");
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        try {
            countDownLatch.await();
            logger.info("所有线程执行结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


