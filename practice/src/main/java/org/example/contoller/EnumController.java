package org.example.contoller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.domain.TProduct;
import org.example.mapper.TProductMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class EnumController {

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
