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
    public static void main(String[] args) {
        System.out.println("11".matches("^[11]"));
        Pattern p = Pattern.compile("\\w");
        Matcher matcher= p.matcher("afds");
        System.out.println(matcher);
        while (matcher.find()){
            System.out.println(matcher.group());
        }

        Map<String,String> map =new HashMap<>();
        map.put("a","a");
        map.put("b","a");
        map.put("c","a");
        map.put("d",null);
        Set<String> strings = map.keySet();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        entries.forEach(m ->{
            System.out.println(m.getKey()+"  "+m.getValue());
        });



        map.forEach((k,v) ->{
            System.out.println(k+v);
        });
    }
    @Resource
    TProductMapper tProductMapper ;

    @RequestMapping("test")
    public List<TProduct> test(){

        new Thread(){
            @Override
            public void run() {
                LambdaQueryWrapper<TProduct> lqw=new LambdaQueryWrapper<>();
                lqw.eq(TProduct::getId,20);
                List<TProduct> tProduct=tProductMapper.selectList(lqw);
                System.out.println(tProduct);
            }
        }.start();

    }


}
