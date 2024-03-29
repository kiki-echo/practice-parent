package org.example.demo;

import org.example.domain.TProduct;

import java.io.*;
import java.math.BigInteger;

public class SerializableDemo {
    public static void main(String[] args) throws Exception {
        TProduct tProduct=new TProduct();
        tProduct.setId(1L);
        tProduct.setProduct_img("sadfgasd");
        tProduct.setProduct_name("手机");


        OutputStream os = new FileOutputStream("/Users/zhangweilong/Code/practice-parent/practice/src/main/java/org/example/demo/b.dat");
        InputStream is = new FileInputStream("/Users/zhangweilong/Code/practice-parent/practice/src/main/java/org/example/demo/b.dat");
        ObjectOutputStream oos=new ObjectOutputStream(os);
        oos.writeObject(tProduct);
        oos.close();

        ObjectInputStream ois =new ObjectInputStream(is);
        TProduct product =  (TProduct) ois.readObject();
        System.out.println(product);



    }



    public static String calculateFactorial(int n) {
        BigInteger result = new BigInteger("1");
        for (int i = 1; i <= n; i++) {
            BigInteger iValue = new BigInteger(String.valueOf(i));
            result = result.multiply(iValue);
        }
        return result.toString();
    }
}
