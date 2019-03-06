package com.company;
import java.io.File;
import java.text.SimpleDateFormat;

public class DiskFile extends DiskElement
{

    public DiskFile(File file){
        this.file=file;
    }

    @Override
    protected void print(int depth){
        SimpleDateFormat template = new SimpleDateFormat("yyy-MM-dd");
        StringBuilder info = new StringBuilder();

        for(int i=0;i<depth;i++) info.append('-');

        info.append(file.getName());
        int l=50-info.length();
        for (int i=0;i<l;i++){
            info.append(" ");
        }
        info.append(" ");
        info.append("F "+ file.length()+" bytes ");
        info.append(template.format(file.lastModified()));
        System.out.println(info);
    }

    @Override
    protected boolean isDirectory(){
        return false;
    }

    @Override
    protected long getSize(){
        return this.file.length();
    }

    public int compareTo(DiskElement b){
        if(this.file.length()>b.file.length()) return 1;
        else if (this.file.length()<b.file.length()) return -1;
        else return 1;
        //if(this.file.getName().compareTo(b.file.getName())>0) return 1;
        //else return  -1;
    }
}
