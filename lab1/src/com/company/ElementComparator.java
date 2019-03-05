package com.company;

import java.util.Comparator;

public class ElementComparator implements Comparator{
    @Override
    public int compare(Object one,Object two) {
        try {
            DiskElement a = (DiskElement) one;
            DiskElement b = (DiskElement) two;

            if (a.isDirectory() && !b.isDirectory()){
                return -1;
            }else if(!a.isDirectory() && b.isDirectory()){
                return 1;
            }else if(!a.isDirectory() && !b.isDirectory()){
                if(a.getSize()>b.getSize()) return 1;
                else if (a.getSize()==b.getSize()) return 0;
                else return -1;
            }else return 1;

        }catch (ClassCastException e){
            return 0;
        }
    }
}
