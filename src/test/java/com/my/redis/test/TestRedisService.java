package com.my.redis.test;

import com.my.redis.TestApplication;
import com.my.redis.service.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@TestPropertySource(locations = "/test-application.properties")
public class TestRedisService {

    @Autowired(required = true)
//    @Qualifier("memberService")
    private IRedisService service;

    @Test
    public void testGet() {
        System.out.println(service.get("2") );
        System.out.println(service.get("1") );
        System.out.println(service.get(1, "9") );

    }

    @Test
    public void testSet() {
        System.out.println(service.set(1, "9", "值值值", 300) );
        System.out.println(service.set("1", "值8", 100) );
        System.out.println(service.set("键2", "价值") );
        System.out.println(service.set("键3", "价值价") );
    }

    @Test
    public void testDel(){
        System.out.println(service.del("2","1"));
        System.out.println( service.del(1, "9") ) ;
    }

    @Test
    public void testGetAll(){
        System.out.println( service.getAllKeys(null) ) ;
        System.out.println( service.getAllKeys() ) ;
    }

    @Test
    public void testIncr(){
        System.out.println(service.incr("prod-1234"));
        System.out.println(service.incr(7,"prod-1234"));
        System.out.println(service.incr(8,"prod-1234", 9));
        System.out.println(service.get(7,"prod-1234"));
    }

    @Test
    public void TestIncr(){
        System.out.println(service.incr("prod-1234"));
    }


    //------------------------------控管領獎--------------------------------------------------------
    @Test
    public void Test控管領獎() throws InterruptedException {
        System.out.println("搶票, 1000搶11");
        for(int i = 0; i < 1000; i++){
            new Thread(new MyThread(11, System.currentTimeMillis())).start();
//            System.out.println(service.incr("prod-1234"));
        }
        Thread.sleep(20000);
    }

    class MyThread implements Runnable {
        int maxCount;
        long startTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        public MyThread(int max, long time){
            this.maxCount = max;
            this.startTime = time;
        }
        @Override
        public void run() {
            long getProductCount = service.incr("prod-1234");
            long endTime = System.currentTimeMillis();
            if( getProductCount <= maxCount){
                System.out.println(Thread.currentThread().getName() + " 在 " + simpleDateFormat.format(new Date(startTime)) + " 參加領獎, 在 " + simpleDateFormat.format(new Date(endTime) ) +   " 領到, 剩" + (10-getProductCount) +"個, 耗時 " +  (endTime - startTime) + "ms <<<<<<<<" )  ;
            }
            else{
                System.out.println(Thread.currentThread().getName() + " 在 " + simpleDateFormat.format(new Date(startTime)) + " 參加領獎, 在 " + simpleDateFormat.format(new Date(endTime) ) +   " 沒領到, 耗時 " +  (endTime - startTime) + "ms")  ;
            }
        }
    }

    @Test
    public void TestPub() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println(service.publish("demoChannel", "Hi"));
        Thread.sleep(5000);
        System.out.println(service.publish("demoChannel", "消息消息消息"));
        Thread.sleep(5000);

    }
}
