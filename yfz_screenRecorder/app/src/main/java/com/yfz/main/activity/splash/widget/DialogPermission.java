package com.yfz.main.activity.splash.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.yfz.main.GlobalConstant;
import com.yfz.main.R;

/**
 * Author: youfengze
 * Time: 9999/01/01
 * Describe: open dialog sliding from bottom to top to display permissions info
 */
public class DialogPermission extends Dialog {
    private Context mContext;
    private Window mWindow;
    private View vContentView;
    private ConstraintLayout vClBaseLayout;
    private ViewGroup.MarginLayoutParams vClBaseLayoutLp;
    //记录手势滑动在dialog上的Y位置
    private float mDownY = 0, mMoveY = 0;
    private int mScrollY = 9;
    //回弹时长
    private final int DURATION = 250;

    public DialogPermission(@NonNull Context context) {
        super(context);
        initial(context);
    }
    public DialogPermission(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initial(context);
    }
    private void initial(Context context){
        setCanceledOnTouchOutside(false);
        mContext = context;
        mWindow = getWindow();
        mWindow.setWindowAnimations(R.style.MyDialogAnimation);
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.getDecorView().setPadding(0, 100, 0, 0);
        mWindow.getAttributes().width = GlobalConstant.getDeviceWidth();
        mWindow.getDecorView().getLayoutParams().height = GlobalConstant.getDeviceHeight();
        mWindow.getDecorView().setBackgroundColor(mContext.getResources().getColor(R.color.colorSplash2Selected));

    }

    @Override
    protected void onStart() {
        super.onStart();
        vContentView = View.inflate(mContext,R.layout.splash_dialog_view, null);
        setContentView(vContentView);
        vClBaseLayout = vContentView.findViewById(R.id.vClBaseLayout);
        vClBaseLayoutLp= (ViewGroup.MarginLayoutParams) vClBaseLayout.getLayoutParams();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = event.getY();
                mScrollY = (int)(mMoveY - mDownY);
                checkIsScrollingDown();
//                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                checkIsCloseEvent();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 向下滑动改变dialog距离
     * @return
     */
    private void checkIsScrollingDown(){
        if(null != vClBaseLayoutLp ) {

            int top = mScrollY;
            int bottom = top+vClBaseLayout.getHeight();
            vClBaseLayout.layout(vClBaseLayout.getLeft(),top,vClBaseLayout.getRight(),bottom);
//            int bottomMargin = (int) (vClBaseLayoutLp.bottomMargin + mScrollY);
//            vClBaseLayoutLp.bottomMargin = bottomMargin;
//            vClBaseLayout.setLayoutParams(vClBaseLayoutLp);
        }
    }

    /**
     * 手指向下滑动超过一定距离调dismiss关闭弹窗
     */
    private void checkIsCloseEvent(){
        if(null != vClBaseLayoutLp ) {
           if(vClBaseLayoutLp.bottomMargin > getWindow().getDecorView().getHeight()/4){
               dismiss();
           }else {
               ValueAnimator anim = ObjectAnimator.ofFloat(vClBaseLayoutLp.bottomMargin, 0.0F).setDuration(DURATION);
               anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                   @Override
                   public void onAnimationUpdate(ValueAnimator animation) {
                       vClBaseLayoutLp.bottomMargin = Math.round((Float) animation.getAnimatedValue());
                       vClBaseLayout.setLayoutParams(vClBaseLayoutLp);
                   }
               });
               anim.start();
           }
        }
    }
}
