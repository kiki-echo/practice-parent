package org.example.contoller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.core.SerializableString;
import org.example.domain.TProduct;
import org.example.enums.Aircondition;
import org.example.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class EnumController {

    @Resource
    TProductMapper tProductMapper ;

    @RequestMapping("test")
    public List<TProduct> test(){
        LambdaQueryWrapper<TProduct> lqw=new LambdaQueryWrapper<>();
        lqw.eq(TProduct::getProductName,"20");
        List<TProduct> tProduct=tProductMapper.selectList(lqw);
        System.out.println(tProduct);
        return  tProduct;
    }


}
