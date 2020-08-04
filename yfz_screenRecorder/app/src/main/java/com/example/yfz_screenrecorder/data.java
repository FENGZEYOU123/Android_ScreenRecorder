package com.example.yfz_screenrecorder;

import android.app.Application;

public class data extends Application {
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    /**
     * 保存录像文件的-----根目录
     **/
    private String rootDir=null;    //根目录
    public String getRootDir() {
        return rootDir;
    }
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * 保存录像文件的------完整目录
     **/
    private String full_file_Dir=null;  //完整目录
    public String getFull_file_Dir() {
        return full_file_Dir;
    }
    public void setFull_file_Dir(String full_file_Dir) {
        this.full_file_Dir = full_file_Dir;
    }

    /**
     * 保存录像文件的------名字
     **/
    private String file_name=null;  //完整目录
    public String getFile_name() {
        return file_name;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }



}

