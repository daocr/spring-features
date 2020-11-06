package org.spring.data.jpa;

import org.springframework.data.domain.PageRequest;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        PageRequest.of(1,10);

        Sort sort = Sort.by("firstname").ascending()
                .and(Sort.by("lastname").descending());

        PageRequest.of(1,10,sort);
        System.out.println("Hello World!");
    }
}
