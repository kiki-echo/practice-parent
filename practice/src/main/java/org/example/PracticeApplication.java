package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("org.example.mapper")
public class PracticeApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(PracticeApplication.class,args);
    }
}
