package org.example.contoller;


import org.example.enums.Aircondition;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EnumController {

    public static void main(String[] args) {

        List <String> list = new ArrayList<>( );
        list.add("ONE");
        list.add("TWO");
        list.stream().forEach( l->{
            System.out.println(l);
            Aircondition.valueOf(l).print();
        });


    }
}
