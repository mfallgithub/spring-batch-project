package com.springbatch.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChunkProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChunkProcessingApplication.class, args);
        System.out.println("This is a test!");
    }

}
