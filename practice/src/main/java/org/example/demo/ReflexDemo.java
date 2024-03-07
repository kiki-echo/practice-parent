package org.example.demo;

import org.example.domain.TProduct;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflexDemo {
    @Test
    public void getClassConstructor() throws Exception {
        Class <TProduct> s = TProduct.class;
//        获取构造器
        Constructor<?>[] declaredConstructors = s.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
            System.out.println(declaredConstructor.getName()+"------参数个数："+declaredConstructor.getParameterCount());

        }
//        创建对象
        Constructor<TProduct> declaredConstructor = s.getDeclaredConstructor();
//        暴力访问私有构造器
//        declaredConstructor.setAccessible(true);
        TProduct tProduct=declaredConstructor.newInstance();
        System.out.println(tProduct);
//获取成员变量

        Field[] declaredFields = s.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println(declaredField.getName()+"-----"+declaredField.getType());
        }
    }
}
