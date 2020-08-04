package com.example.yfz_screenrecorder;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button button_1;
    private ScreenRecordService screenRecordService;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private DisplayMetrics metrics;
    private static final int RECORD_REQUEST_CODE = 101;
    public static Context context;
    public static ConstraintLayout surfaceView_layout;

    //recycleview
    public RecyclerView lv_local_video_list;
    private MyRecyclerView mAdapter;//适配器
    private LinearLayoutManager mLinearLayoutManager;//布局管理器
    public static  List mList;
    //recycleview

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        metrics = new DisplayMetrics();
        context=getApplication();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        button_1 = (Button) findViewById(R.id.button_1);
        button_1.setOnClickListener(this);
        surfaceView_layout=findViewById(R.id.surfaceView_layout);
        //权限检查
        Data Data = (Data) this.getApplicationContext();
        scan_file scan_file= new scan_file();
        String rootDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "yfz_screenrecorder/";
        Data.setRootDir(rootDir);
        File file = new File(rootDir);
        checkMyPermission();


        //************************recycleView
        lv_local_video_list=findViewById(R.id.lv_local_video_list);
        mList =  new ArrayList<String>();
        scan_file.scan_Files(file);

        //创建布局管理器，垂直设置LinearLayoutManager.VERTICAL，水平设置LinearLayoutManager.HORIZONTAL
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //创建适配器，将数据传递给适配器

        mAdapter = new MyRecyclerView(mList);
        //设置布局管理器
        lv_local_video_list.setLayoutManager(mLinearLayoutManager);
        //设置适配器adapter
        lv_local_video_list.setAdapter(mAdapter);

        //************************recycleView

    }
    public void initData(List list) {
        for (int i = 1; i <= 40; i++) {
            list.add("第" + i + "条数据");
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void checkMyPermission() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1101);
        } else {
            connectService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1101) {
            if (grantResults.length != 0 && ((grantResults[0] != PackageManager.PERMISSION_GRANTED) || (grantResults[1] != PackageManager.PERMISSION_GRANTED))) {
                Toast.makeText(MainActivity.this,"请设置必须的应用权限，否则将会导致运行异常！",Toast.LENGTH_SHORT).show();
            } else if (grantResults.length != 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                connectService();
            }
        }
    }

    private void connectService() {
        Intent intent = new Intent(getApplicationContext(), ScreenRecordService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScreenRecordService.ScreenRecordBinder binder = (ScreenRecordService.ScreenRecordBinder) service;
            screenRecordService = binder.getScreenRecordService();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //大于等于5.1版本的安卓需要获取截屏权限
                mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            }
            //开启录屏请求intent
            Intent captureIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                captureIntent = mediaProjectionManager.createScreenCaptureIntent();
            }
            startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this,"录屏服务断开！",Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            //设置mediaProjection
            if (screenRecordService != null) {
                screenRecordService.setMediaProject(mediaProjection);
                screenRecordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button_1:

                if (screenRecordService != null && !screenRecordService.isRunning()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        screenRecordService.startRecord();
                    }
                    Toast.makeText(MainActivity.this,"开始录屏",Toast.LENGTH_SHORT).show();
                    button_1.setText("结束录屏");
                    setToBackground();
                }else if (screenRecordService != null && screenRecordService.isRunning()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        screenRecordService.stopRecord();
                    }
                    button_1.setText("开始录屏");
                } else if (screenRecordService == null) {
                    connectService();
                }
                break;
        }
    }

    private void setToBackground(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
