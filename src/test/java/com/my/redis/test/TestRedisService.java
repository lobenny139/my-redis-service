package com.my.redis.test;

import com.my.redis.TestApplication;
import com.my.redis.service.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

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

    }


}
