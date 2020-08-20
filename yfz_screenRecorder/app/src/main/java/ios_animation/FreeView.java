package ios_animation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class FreeView extends RelativeLayout {

    private int width; //  测量宽度 FreeView的宽度
    private int height; // 测量高度 FreeView的高度
    private int maxWidth; // 最大宽度 window 的宽度
    private int maxHeight; // 最大高度 window 的高度
    private Context context;
    private float downX; //点击时的x坐标
    private float downY;  // 点击时的y坐标
    //是否拖动标识
    private boolean isDrag=false;



    // 处理点击事件和滑动时间冲突时使用 返回是否拖动标识
    public boolean isDrag() {
        return isDrag;
    }

    // 初始化属性
    public FreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;


    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  //测量控件的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏宽高 和 可是适用范围 （我的需求是可在屏幕内拖动 不超出范围 也不需要隐藏）
        width=getMeasuredWidth(); //获取组件的宽
        height=getMeasuredHeight();//获取组件的长
        maxWidth = UiUtil.getMaxWidth(context);
        maxHeight = UiUtil.getMaxHeight(context);// 此时减去状态栏高度 注意如果有状态栏 要减去状态栏 如下行 得到的是可活动的高度
        //maxHeight = UiUtil.getMaxHeight(context)-getStatusBarHeight() - getNavigationBarHeight();
    }
    // 获取状态栏高度
    public int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }
    // 获取导航栏高度
    public int getNavigationBarHeight() {
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;

    }


    /**
     * 处理事件分发
     * @param event
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // 点击动作处理 每次点击时将拖动状态改为 false 并且记录下点击时的坐标 downX downY
                    isDrag=false;
                    downX = event.getX(); // 点击触屏时的x坐标 用于离开屏幕时的x坐标作计算 (视图坐标系)
                    downY = event.getY(); // 点击触屏时的y坐标 用于离开屏幕时的y坐标作计算(视图坐标系)

                    break;
                case MotionEvent.ACTION_MOVE: // 滑动动作处理 记录离开屏幕时的 moveX  moveY 用于计算距离 和 判断滑动事件和点击事件 并作出响应
                     float moveX = event.getX() - downX;
                     float moveY = event.getY() - downY;
                    Log.d("坐标： ", "event.getX():------- "+event.getX());
                    Log.d("坐标： ", "event.getY():------- "+event.getY());
                    Log.d("坐标： ", "event.getRawX():------- "+event.getRawX());  //(坐android标系)
                    Log.d("坐标： ", "event.getRawY():------- "+event.getRawY());  //(坐android标系)
                    Log.e("坐标： ", "------------------------------------------------");


                    int l,r,t,b; // 上下左右四点移动后的偏移量
                    //计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
                    if (Math.abs(moveX) > 3 ||Math.abs(moveY) > 3) { // 偏移量的绝对值大于 3 为 滑动时间 并根据偏移量计算四点移动后的位置
                        l = (int) (getLeft() + moveX);
                        r = l+width;
                        t = (int) (getTop() + moveY);
                        b = t+height;
                        Log.d("坐标： ", "width------ "+width);  //(坐android标系)
                        Log.d("坐标： ", "height-----"+height);  //(坐android标系)

                        Log.d("坐标： ", "getLeft()------ "+getLeft());  //(坐android标系)
                        Log.d("坐标： ", "getRight()-----"+getRight());  //(坐android标系)

                        Log.d("坐标： ", "getTop()------ "+getTop());  //(坐android标系)
                        Log.d("坐标： ", "getBottom()-----"+getBottom());  //(坐android标系)
                        Log.d("坐标： ", "l:::::getLeft() + moveX:------- "+l);  //(坐android标系)
                        Log.d("坐标： ", "r:::::l+width:------- "+r);  //(坐android标系)
                        Log.d("坐标： ", "t:::::(getTop() + moveY);:------- "+t);  //(坐android标系)
                        Log.d("坐标： ", "b:::::t+height:------- "+b);  //(坐android标系)

                        Log.e("坐标： ", "=======================================");

                        //不划出边界判断,最大值为边界值
                        // 如果你的需求是可以划出边界 此时你要计算可以划出边界的偏移量 最大不能超过自身宽度或者是高度  如果超过自身的宽度和高度 view 划出边界后 就无法再拖动到界面内了 注意
                        if(l<0){ // left 小于 0 就是滑出边界 赋值为 0 ; right 右边的坐标就是自身宽度 如果可以划出边界 left right top bottom 最小值的绝对值 不能大于自身的宽高
                            l=0;
                            r=l+width;
                        }else if(r> maxWidth){ // 判断 right 并赋值
                            r= maxWidth;
                            l=r-width;
                        }
                        if(t<0){ // top
                            t=0;
                            b=t+height;
                        }else if(b> maxHeight){ // bottom
                            b= maxHeight;
                            t=b-height;
                        }
//                        this.setRelativeViewLocation(this,l, t, r, b); // 重置view在layout 中位置
                        Log.e("坐标： ", "***l:t:r:b   "+l+" "+t+" "+r+" "+b+"*******");
                        //l 组件最左边相对于 Android坐标系位置
                        //r 组件最右边相对于 Android坐标系位置
                        //t 组件最上边相对于 Android坐标系位置
                        //b 组件最下边相对于 Android坐标系位置

                        this.layout(l, t, r, b); // 重置view在layout 中位置

                        isDrag=true;  // 重置 拖动为 true
                    }else {
                        isDrag=false; // 小于峰值3时 为点击事件
                    }
                    break;
                case MotionEvent.ACTION_UP: // 不处理
                    setPressed(false);
                    break;
                case MotionEvent.ACTION_CANCEL: // 不处理
                    setPressed(false);
                    break;
            }
            return true;
        }
        return false;
    }

    private void setRelativeViewLocation(View view, int left, int top, int right, int bottom) {  //防止刷新布局，组件回到原来位置
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(right - left, bottom - top);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        ViewParent parent = view.getParent();
        View p = (View) parent;
        int marginRight = p.getWidth() - right;
        int marginBottom = p.getHeight() - bottom;
        params.setMargins(left, top, marginRight, marginBottom);
        view.setLayoutParams(params);
    }
}
