package com.company;
import java.io.File;

public abstract class DiskElement implements Comparable<DiskElement>
{
    protected File file;

    protected abstract void print(int depth);

    protected abstract boolean isDirectory();

    protected abstract long getSize();
}
