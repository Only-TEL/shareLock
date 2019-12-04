package com.xihua;

import com.xihua.config.GenCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenCodeTest {

    @Autowired
    private GenCode genCode;

    @Test
    public void genCodeTest(){
        genCode.genCode("back_recod");
//        System.out.println(userService.deleteByIds(1));
    }

//    @Autowired
//    private ISysUserService userService;


}
