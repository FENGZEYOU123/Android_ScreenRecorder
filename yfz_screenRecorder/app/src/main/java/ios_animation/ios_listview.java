package ios_animation;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 下拉时具有弹性的ListView
 * @author zhangjg
 * @date Dec 21, 2013 4:54:29 PM
 */
public class ios_listview extends ListView implements OnScrollListener{

    private static final String TAG = "PullListView";

    //下拉因子,实现下拉时的延迟效果
    private static final float PULL_FACTOR = 0.6F;

    //回弹时每次减少的高度
    private static final int PULL_BACK_REDUCE_STEP = 1;

    //回弹时递减headview高度的频率, 注意以纳秒为单位
    private static final int PULL_BACK_TASK_PERIOD = 700;


    //记录下拉的起始点
    private boolean isRecored;

    //记录刚开始下拉时的触摸位置的Y坐标
    private int startY;

    //第一个可见条目的索引
    private int firstItemIndex;

    //用于实现下拉弹性效果的headView
    private View headView;

    private int currentScrollState;

    //实现回弹效果的调度器
    private ScheduledExecutorService  schedulor;

    //实现回弹效果的handler,用于递减headview的高度并请求重绘
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LayoutParams params = (LayoutParams) headView.getLayoutParams();

            //递减高度
            params.height -= PULL_BACK_REDUCE_STEP;

            headView.setLayoutParams(params);

            //重绘
            headView.invalidate();

            //停止回弹时递减headView高度的任务
            if(params.height <= 0){
                schedulor.shutdownNow();
            }
        }
    };


    /**
     * 构造函数
     * @param context
     * @param attr
     */
    public ios_listview(Context context, AttributeSet attr) {
        super(context, attr);

        init();
    }

    public ios_listview(Context context) {
        super(context);
    }

    /**
     * 初始化
     */
    private void init() {
        //监听滚动状态
        setOnScrollListener(this);

        //创建PullListView的headview
        headView = new View(this.getContext());

        //默认白色背景,可以改变颜色, 也可以设置背景图片
        headView.setBackgroundColor(Color.WHITE);

        //默认高度为0
        headView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 0));

        this.addHeaderView(headView);
    }



    /**
     * 覆盖onTouchEvent方法,实现下拉回弹效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //记录下拉起点状态
                if (firstItemIndex == 0 ) {

                    isRecored = true;
                    startY = (int) event.getY();

                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:


                if(!isRecored){
                    break;
                }

                //以一定的频率递减headview的高度,实现平滑回弹
                schedulor = Executors.newScheduledThreadPool(1);
                schedulor.scheduleAtFixedRate(new Runnable() {

                    @Override
                    public void run() {
                        handler.obtainMessage().sendToTarget();

                    }
                }, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);

                isRecored = false;

                break;


            case MotionEvent.ACTION_MOVE:

                if (!isRecored && firstItemIndex == 0 ) {
                    isRecored = true;
                    startY = (int) event.getY();
                }

                if(!isRecored){
                    break;
                }

                int tempY = (int) event.getY();
                int moveY = tempY - startY;

                if(moveY < 0){
                    isRecored = false;
                    break;
                }

                headView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,  (int)(moveY * PULL_FACTOR)));
                headView.invalidate();

                break;
        }
        return super.onTouchEvent(event);
    }



    public void onScroll(AbsListView view, int firstVisiableItem,
                         int visibleItemCount, int totalItemCount) {
        firstItemIndex = firstVisiableItem;

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        currentScrollState = scrollState;
    }

}