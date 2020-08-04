package com.example.yfz_screenrecorder;

import android.app.Application;

public class Data extends Application {

    private String rootDir=null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    /**
     * 保存录像文件的根目录
     **/
    public String getRootDir() {
        return rootDir;
    }
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }


}

