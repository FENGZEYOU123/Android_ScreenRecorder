package com.yfz.main.activity.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yfz.main.R;
import com.yfz.main.activity.MainActivity;
import com.yfz.main.util.PermissionUtil;
import com.yfz.main.activity.splash.widget.DialogPermission;
import com.yfz.main.activity.splash.widget.DotPointerView;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private ViewPager mViewPager;
    private ArrayList<View> mArrayList;
    private View mView1,mView2,mView3;
    private Button mButton;
    private DotPointerView mDotPointerView;
    //权限弹窗
    private DialogPermission mDialogPermission;
    //权限请求code
    private final int PERMISSION_REQUEST_CODE_ALL = 0x100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialView();
    }
    private void initialView(){
        layoutInflater=getLayoutInflater().from(this);
        mViewPager=findViewById(R.id.viewPager);
        mButton=findViewById(R.id.button);
        mDotPointerView =findViewById(R.id.dotLayout);
        mView1=layoutInflater.inflate(R.layout.splash_view_1,null);
        mView2=layoutInflater.inflate(R.layout.splash_view_2,null);
        mView3=layoutInflater.inflate(R.layout.splash_view_3,null);
        mArrayList=new ArrayList<>(); //将想要展示的view储存到数组中
        mArrayList.add(mView1);
        mArrayList.add(mView2);
        mArrayList.add(mView3);
        mViewPager.setAdapter(new ViewPagerAdapter()); //添加视图桥梁
        mViewPager.setOnPageChangeListener(new ViewPagerChangeListener()); //监听页面状态
        mViewPager.setOffscreenPageLimit(mArrayList.size()-1);  //允许最大view缓存数量
        mViewPager.setOverScrollMode(mViewPager.OVER_SCROLL_NEVER); //去掉翻到顶页和尾页的水波纹
        mDotPointerView.setPointerStyle(mArrayList.size(),10,10,10,getResources().getDrawable(R.drawable.splash_two_dot_selected),getResources().getDrawable(R.drawable.splash_two_dot_unselected));

    }
    //添加切换页面
    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mArrayList.size(); //返回记录的view最大数量
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mArrayList.get(position));
            return mArrayList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mArrayList.get(position));
        }
    }
    //监听页面状态
    class  ViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {  //翻到最后一页才显示按钮
            if(position==mArrayList.size()-1){
                mButton.setVisibility(View.VISIBLE);
                startAnimation(mButton);
            }else {
                mButton.setVisibility(View.GONE);
            }
            mDotPointerView.refreshPointer(position); //刷新圆点UI，传入当前选中的页面位置
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    //按钮动画
    private void startAnimation(View view){
        ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(view,"translationY",150f,0f);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    /**
     * 打开mainActivity
     */
    private void doStartActivity(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
    /**
     * 开始按钮,点击后检查是否同意了所有权限
     * 有权限则直接跳到mainActivity，否则前去获取
     * @param view
     */
    public void doStart(View view){
        openDialog();
//       if(!PermissionUtil.isGetAllNeededPermission()){
//           PermissionUtil.getRequestAllPermission(this,PERMISSION_REQUEST_CODE_ALL);
//       }else {
//           doStartActivity();
//       }
    }

    /**
     * 权限获取-必须同意权限才能使用APP
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE_ALL:
                boolean hasGetAll = true;
                for(int i=0; i<grantResults.length; i++){
                    if(grantResults[i] !=  PackageManager.PERMISSION_GRANTED){
                        hasGetAll = false;
                    }
                }
                if(hasGetAll){
                    doStartActivity();
                }else {
                    openDialog();
                }
                break;
            default:
                break;
        }
    }
    private void openDialog(){
        if(null != mDialogPermission){
            if(mDialogPermission.isShowing()){
                mDialogPermission.dismiss();
            }
            mDialogPermission= null;
        }
        mDialogPermission = new DialogPermission(this,R.style.MyDialogTheme);
        mDialogPermission.show();
    }
}