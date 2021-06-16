package com.yfz.main.activity.splash.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

/**
 * Author: YouFengZe
 * Time: 9999/01/01
 * Describe: draw viewPager pointer view when flips pages
 */
public class DotPointerView extends LinearLayout {
    private Context mContext;
    private Paint mPaintDotSelected;
    private Paint mPaintDotUnSelected;
    private Rect mRect;
    private int mPageNumber=0;
    private int mDotWidth=0;
    private int mDotHeight =0;
    private float mDotMargin=0;
    private int mSelectedPosition=0;
    private Drawable mDotDrawableSelected;
    private Drawable mDotDrawableUnSelected;

    public DotPointerView(Context context) {
        super(context);
        initial(context);
    }
    public DotPointerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initial(context);
    }
    public DotPointerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }
    //初始化
    private void initial(Context context){
        this.mContext=context;
        this.mRect=new Rect();
        initial_paint_dot_selected();
        initial_paint_dot_unSelected();
        this.setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //初始化-画笔-圆点-被选中的
    private void initial_paint_dot_selected(){
        this.mPaintDotSelected=new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaintDotSelected.setStyle(Paint.Style.FILL);
        this.mPaintDotSelected.setColor(Color.BLACK);
    }
    //初始化-画笔-圆点-未被选中的
    private void initial_paint_dot_unSelected(){
        this.mPaintDotUnSelected=new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaintDotUnSelected.setStyle(Paint.Style.STROKE);
        this.mPaintDotUnSelected.setColor(Color.GRAY);
        this.mPaintDotUnSelected.setStrokeWidth(3f);
    }
    //刷新绘制
    public void refreshUI(){
        this.invalidate();
    }

    //设置圆点信息-数量-高-宽
    public void setPointerStyle(int pageNumber, int dotHeight, int dotWidth, float dotMargin, Drawable dotDrawableSelected, Drawable dotDrawableUnSelected){
        mPageNumber = pageNumber; //数量
        mDotHeight  = dotHeight; //高
        mDotWidth   = dotWidth; //宽
        mDotMargin  = dotMargin; //间距
        mDotDrawableSelected=dotDrawableSelected;
        mDotDrawableUnSelected=dotDrawableUnSelected;

        this.getLayoutParams().height= (int)(mDotHeight);
        this.getLayoutParams().width = (int)(mDotWidth * pageNumber + mDotMargin * (pageNumber-1));
    }
    //设置当前选中的页面
    public void refreshPointer(int currentPagePosition){
        mSelectedPosition=currentPagePosition;
        refreshUI();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPageNumber>0 && mDotWidth>0 && mDotHeight>0) { //必须传入数量,Dot长宽,才开始绘制
            int i=0;
            while(i<mPageNumber){
                mRect.left=(int)(i*(mDotWidth+mDotMargin));
                mRect.top=0;
                mRect.right=mRect.left+mDotWidth;
                mRect.bottom=getHeight();
                if(mSelectedPosition==i) {
                    mDotDrawableSelected.setBounds(mRect);
                    mDotDrawableSelected.draw(canvas);
                }else {
                    mDotDrawableUnSelected.setBounds(mRect);
                    mDotDrawableUnSelected.draw(canvas);
                }
                i++;
            }
        }
    }

}

