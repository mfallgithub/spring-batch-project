package com.springbatch.demo.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        System.out.println("BeforeChunk() executed");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        System.out.println("AfterChunk() executed");
    }


    @AfterChunkError
    public void afterChunkError(ChunkContext context) {
        System.out.println("AfterChunkError() executed");
    }
}
