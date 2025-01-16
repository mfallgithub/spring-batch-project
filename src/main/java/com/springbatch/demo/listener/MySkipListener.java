package com.springbatch.demo.listener;

import com.springbatch.demo.domain.OSProduct;
import com.springbatch.demo.domain.Product;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;

import java.io.FileWriter;
import java.io.IOException;

public class MySkipListener implements SkipListener<Product, OSProduct> {
    @Override
    public void onSkipInRead(Throwable t) {
       if(t instanceof FlatFileParseException){
           System.out.println("Skipped Item:-");
           System.out.println(((FlatFileParseException)t).getInput());
           writeToFile(((FlatFileParseException)t).getInput());
       }
    }

    @Override
    public void onSkipInProcess(Product item, Throwable t) {
        System.out.println("Skipped Item:-");
        System.out.println(item);
        writeToFile(item.toString());
    }

    @Override
    public void onSkipInWrite(OSProduct item, Throwable t) {
        SkipListener.super.onSkipInWrite(item, t);
    }

    public void writeToFile(String data) {
        try {
            FileWriter fileWriter= new FileWriter("src/main/rejected/Product_Details_Rejected.txt", true);
            fileWriter.write(data + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
