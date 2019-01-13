package com.basic.client;

import com.basic.service.HelloService;

/**
 * locate com.com.basic.client
 * Created by 79875 on 2017/6/2.
 */
public class Client {
    public static void main(String[] args) throws Exception {
        ServiceConsumer consumer=new ServiceConsumer();

        while (true){
            HelloService helloService= (HelloService) consumer.lookup();
            String result=helloService.sayHello("tanjie");
            System.out.println(result);
            Thread.sleep(3000);
        }
    }
}
