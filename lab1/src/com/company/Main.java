package com.company;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;

public class Main
{
    public static void main(String[] args) {
        try {
            String path = args[0];
            String test = args[1];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("No arguments");
            return;
        }
           
        String path = args[0];
        boolean whatToDo=false;
        if(args[1].equals("1")) whatToDo=true;

        File test = new File(path);
        if(!test.exists()){
            System.out.println("Path not existing!");
            return;
        }
        try{
            DiskDirectory dir=new DiskDirectory(path, whatToDo);
            dir.print(0);
        }catch (InvalidArgumentException e){
            System.out.println("Invalid argument!");
        }
    }
}
