package com.log.logdemo.component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Book {

    @Value("${book.name}")
    private String name;
    @Value("${book.author}")
    private String author;

}
