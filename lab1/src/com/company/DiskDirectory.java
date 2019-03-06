package com.company;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

public class DiskDirectory extends DiskElement
{
    TreeSet<DiskElement> children;

     DiskDirectory(String path,boolean what) throws InvalidArgumentException {
        this.file=new File(path);
        if(!this.file.isDirectory()) throw new InvalidArgumentException(new String[]{"Not a path to the directory!"});

        if(what){
            this.children= new TreeSet<>();
        }else {
            this.children = new TreeSet<>(new ElementComparator());
        }
        //this.children = new TreeSet<>(Comparator.comparing(o -> o.file.getName()));

            File[] childElements = this.file.listFiles();
            if(childElements!=null) {
                for (File a : childElements) {
                    if (a.isDirectory()) children.add(new DiskDirectory(a.getPath(),what));
                    else children.add(new DiskFile(a));
                }
            }
    }

    @Override
    protected void print(int depth){
        SimpleDateFormat template = new SimpleDateFormat("yyy-MM-dd");
        StringBuilder info = new StringBuilder();

        for(int i=0;i<depth;i++) info.append('-');
        info.append(this.file.getName());
        int l=51-info.length();
        for (int i=0;i<l;i++){
            info.append(" ");
        }
        info.append("C ");
        info.append(template.format(file.lastModified()));
        System.out.println(info);

        for(DiskElement ele : children) {
            ele.print(depth + 1);
        }
    }
    @Override
    public int hashCode(){
        return (225*(int)this.getSize()+this.file.hashCode());
    }


    public boolean equals(DiskElement ele){
        if(ele==null) return false;
        else return true;
    }

    @Override
    protected boolean isDirectory(){
        return true;
    }

    @Override
    protected long getSize(){
        return 0;
    }

    public int compareTo(DiskElement b){
        if(this.getSize()>b.getSize()) return 1;
        else if (this.getSize()<b.getSize()) return -1;
        else return 1;
        //if(this.file.getName().compareTo(b.file.getName())>0) return 1;
        //else return  -1;
    }
}
