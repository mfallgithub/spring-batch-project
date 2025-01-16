package com.springbatch.demo.skippolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;

public class MySkipPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        System.out.println("Skip count: " + skipCount);
        if(skipCount < 3){
           if(t instanceof ValidationException){
               return true;
           }

           if(t instanceof FlatFileParseException){
               String line = ((FlatFileParseException) t).getInput();
               String[] lineArr= line.split(",");
               if (lineArr.length >=4){
                   return true;
               }
           }
        }
        return false;
    }
}
