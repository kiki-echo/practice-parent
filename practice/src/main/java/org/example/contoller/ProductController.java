package org.example.contoller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.domain.TProduct;
import org.example.mapper.TProductMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {
    @RequestMapping("query")
    public String getProduct(){
return "success";
    }

    @Resource
    TProductMapper tProductMapper ;

    @RequestMapping("test")
    public List<TProduct> test(){


                LambdaQueryWrapper<TProduct> lqw=new LambdaQueryWrapper<>();
                lqw.eq(TProduct::getId,20);
                List<TProduct> tProduct=tProductMapper.selectList(lqw);
                System.out.println(tProduct);


return tProduct;
    }
}
