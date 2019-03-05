package com.company;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;

public class Main
{
    public static void main(String[] args) {
        String path = args[0];
        boolean whatToDo=false;
        if(args[1].equals("1")) whatToDo=true;
        try{
            DiskDirectory dir=new DiskDirectory(path, whatToDo);
            dir.print(0);
        }catch (InvalidArgumentException e){
            System.out.println("Invalid argument!");
        }
    }
}
