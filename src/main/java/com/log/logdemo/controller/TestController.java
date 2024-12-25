package com.log.logdemo.controller;

import com.log.logdemo.component.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Slf4j
@RequestMapping(path ="/v1/test" )
public class TestController {

    @Autowired
    Book book;

    @GetMapping("/hello")
    public Book hello(){
//        log.error("Hello World");
//        log.warn("Hello World");
//        log.info("Hello World");

        Integer i= 10000;
        float v = i.floatValue();
        System.out.println(v);
        return book;

    }
}
