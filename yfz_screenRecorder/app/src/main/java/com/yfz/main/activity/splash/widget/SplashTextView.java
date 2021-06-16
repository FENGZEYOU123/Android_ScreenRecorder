package com.yfz.main.activity.splash.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.yfz.main.R;

/**
 * Author: YouFengZe
 * Time: 9999/01/01
 * Describe: 展示在splash页面的弹窗里的view,统一控制 img 和 text
 */
public class SplashTextView extends LinearLayout {
    private Context mContext;
    //已获取到权限的颜色
    private int mColorHasPermission = getResources().getColor(R.color.colorBlack);
    //未获取到权限的颜色
    private int mColorNotPermission = getResources().getColor(R.color.colorGray);
    //图案
    private ImageView vImageView = null;
    //图案-资源
    private Drawable mDrawable = null;
    //文字
    private TextView vTextView =null;
    //文字-内容
    private String mTextPermission = "";
    public SplashTextView(@NonNull Context context) {
        super(context);
        initial(context);
    }
    public SplashTextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SplashTextView);
        mDrawable = typedArray.getDrawable(R.styleable.SplashTextView_setImgDrawable);
        mTextPermission = typedArray.getString(R.styleable.SplashTextView_setText);
        initial(context);
        addImageView();
        addTextView();
    }
    private void initial(Context context){
        mContext = context;
        setOrientation(HORIZONTAL);
    }

    /**
     * 添加权限图案
     */
    private void addImageView(){
        if(null == vImageView){
            vImageView = new ImageView(mContext);
            int WH = (int) getResources().getDimension(R.dimen.splash_dialog_view_wh);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WH, WH);
            vImageView.setLayoutParams(layoutParams);
            addView(vImageView);
            if(null != mDrawable){
                vImageView.setImageDrawable(mDrawable);
            }
        }
    }

    /**
     * 添加权限文字
     */
    private void addTextView(){
        if(null == vTextView){
            vTextView = new TextView(mContext);
            int WH = (int) getResources().getDimension(R.dimen.splash_dialog_view_wh);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WH);
            vTextView.setLayoutParams(layoutParams);
            addView(vTextView);
            vTextView.setTextSize(getResources().getDimension(R.dimen.splash_dialog_text_size));
            vTextView.getPaint().setFakeBoldText(true);
            vTextView.setTextColor(mColorNotPermission);
            vTextView.setText(mTextPermission);
        }
    }
    /**
     * 改变颜色状态-是否获取到了权限
     */
    public void changePermissionStatus(boolean isGetPermission){
        if(null != vTextView) {
            vTextView.setTextColor(isGetPermission ? mColorHasPermission : mColorNotPermission);
        }
    }
}
