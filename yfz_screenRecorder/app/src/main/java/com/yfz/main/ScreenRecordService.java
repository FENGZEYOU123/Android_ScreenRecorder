package com.yfz.main;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.yfz.main.activity.MainActivity;
import com.yfz.main.base.BaseApplication;

import java.io.File;
import java.io.IOException;

public class ScreenRecordService extends Service {

    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    com.yfz.main.base.BaseApplication BaseApplication = (BaseApplication) MainActivity.context;  //全局变量
    private DisplayMetrics dm= new DisplayMetrics();
    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;

    private String videoPath = "";

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;


    }

    public class ScreenRecordBinder extends Binder {
        public ScreenRecordService getScreenRecordService(){
            return ScreenRecordService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ScreenRecordBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }
        initRecorder();
        createVirtualDisplay();
        try {
            mediaRecorder.start();
            running = true;
            return true;
        }catch (IllegalStateException e){
            e.printStackTrace();
            Toast.makeText(this,"start 出错，录屏失败！", Toast.LENGTH_SHORT).show();
            running = false;
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean stopRecord() {
        if (!running) {
            return false;
        }
        running = false;
        try{
            mediaRecorder.stop();
            mediaRecorder.reset();
            virtualDisplay.release();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ScreenRecordService.this, "录屏出错,保存失败", Toast.LENGTH_SHORT).show();
            Log.e("报错", "stopRecord: "+e.toString() );
            return false;
        }
        Toast.makeText(ScreenRecordService.this, "录屏完成，已保存。", Toast.LENGTH_SHORT).show();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualDisplay() {
        try{
            virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
        }catch (Exception e){
            Toast.makeText(this,"virtualDisplay 录屏出错！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        //设置声音来源
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频来源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //设置视频格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置视频储存地址
        videoPath = getSaveDirectory() + System.currentTimeMillis() + ".mp4";
        BaseApplication.setFull_file_Dir(videoPath);  //保存完整路径
        BaseApplication.setFile_name(System.currentTimeMillis() + ".mp4");//保存名字

        mediaRecorder.setOutputFile(videoPath);
        //设置视频大小
        dm = MainActivity.context.getResources().getDisplayMetrics();

        int dm_W= dm.widthPixels;  //   设备的绝对宽度
        int dm_H= dm.heightPixels;//    设备的绝对长度

        try{

            mediaRecorder.setVideoSize(720, 1080);
            Log.d("屏幕尺寸为", "initRecorder: "+"  "+dm_W+"     "+dm_H);
        }catch (Exception e){
            Log.e("屏幕尺寸为", "报错！！"+e.toString());

        }

        //设置视频编码
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置声音编码
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //视频码率
        mediaRecorder.setVideoEncodingBitRate(2 * 1920 * 1080);
        mediaRecorder.setVideoFrameRate(60);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"prepare出错，录屏失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public String getSaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "yfz_screenrecorder/" + "/";
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            return rootDir;
        } else {
            return null;
        }
    }

}
