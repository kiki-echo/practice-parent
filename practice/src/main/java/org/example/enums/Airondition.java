package org.example.enums;

public enum Airondition {
    ONE{

        public void print(){
            System.out.println("one");
        };

    },
    TWO{
        public void print(){
            System.out.println("two");
        };
    };

    public abstract   void  print();

}
