package org.example.enums;

import org.springframework.web.bind.annotation.RestController;

public enum Aircondition {
    ONE{
        @Override
        public void print(){
            System.out.println("one");
        };

    },
    TWO{
        @Override
        public void print(){
            System.out.println("two");
        };
    };

    public abstract   void  print();

}
