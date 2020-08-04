package com.example.yfz_screenrecorder;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class scan_file extends MainActivity {


    ArrayList<String> files_name= new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  ArrayList<String> scan_Files(File file){

        Log.e("遍历", "扫描文件路径为>>>>>> "+file);

        try{
            if(file.isDirectory()){ //判断是否是文件夹
                File[] files = file.listFiles();//遍历文件夹里面的所有的
                for(int i=0;i<files.length;i++){
                    Log.e("遍历", "文件>>>>>> "+files[i].toString());
                    String file_name=files[i].toString();
                    String pcg_name=MainActivity.context.getPackageName();
                    String aa="example";
                    int pcg_name_index=pcg_name.indexOf(aa);
                    if(pcg_name_index>0){
                        pcg_name=pcg_name.substring((pcg_name_index+aa.length()+1),pcg_name.length());
                    }
                    int file_name_index=   file_name.indexOf(pcg_name);
                      if(file_name_index>0){
                     file_name= file_name.substring(file_name_index+pcg_name.length()+1);  //只保留文件名
                      }
                    MainActivity.mList.add(file_name);
                }
            }
            System.gc();//系统回收垃圾


            for(int j=0;j<files_name.size();j++){
                Log.e("遍历", "Arrylist存储的为>>>>>>    "+    files_name.get(j));
            }
            return files_name;
        }catch (Exception e){
            Log.e("遍历", "报错！！！: "+e.toString());

            return files_name;

        }

    }


    /**
     * 将刚保存好的录像视频，刷新同步更新到系统相册中，可以使用户在"相册"里立刻就可以看到
     *
     **/

    public static void  update_scanFIle(Context context, String path) {
        MediaScannerConnection.scanFile(context,
                new String[]{path},
                new String[]{"image","video"},
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("同步更新", "扫描系统相册图库完成:  "+path);
                    }
                });

    }
}
