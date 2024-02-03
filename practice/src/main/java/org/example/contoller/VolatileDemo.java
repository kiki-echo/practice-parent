package org.example.contoller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VolatileDemo {
    public static void main(String[] args) {
       new Thread("啊"){
            @Override
            public void  run(){
                System.out.println(Thread.currentThread().getName());
            }
        }.start();
        List<String> one =new ArrayList<>();

        one.add("张三");
        one.add("李四");
        one.add("王二五");
        List<String> two =new ArrayList<>();

        two.add("张三");
        two.add("李四啥");
        two.add("王二五");
        two.add("阿斯顿发生");

       Stream<String>s1=  one.stream().filter(s -> s.length()>1).skip(2);
        Stream<String>s2= two.stream().filter(s -> s.length()>2).skip(2).filter(s -> s.startsWith("阿"));

        Stream <String> stream =Stream.concat( s1,s2);
        System.out.println(stream);
        List <String> l =stream.collect(Collectors.toList());
        System.out.println(l);


        Map<Integer,String> map=new HashMap<>();
        for (int i = 0;     i <10 ; i++) {
            map.put(i,i+"");

        }
map.entrySet().stream().filter(s->s.getKey()>8).collect(Collectors.toMap());
    }
}
