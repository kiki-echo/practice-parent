package org.example;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.contoller.ProductController;
import org.example.domain.TProduct;
import org.example.mapper.TProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest
{

@Autowired
ProductController productController;

    @Test
    public  void  productTest(){


        System.out.println(productController.test());

    }

}
