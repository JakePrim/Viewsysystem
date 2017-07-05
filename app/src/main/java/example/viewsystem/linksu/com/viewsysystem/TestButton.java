package example.viewsystem.linksu.com.viewsysystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：7/4 0004
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class TestButton extends android.support.v7.widget.AppCompatTextView {


    protected GestureDetector mGetstureDetector;
    protected Scroller scroller;
    private Context context;

    public TestButton(Context context) {
        super(context, null, 0);
        this.context = context;
        init();
    }

    public TestButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
        init();
    }

    public TestButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        //        mGetstureDetector = new GestureDetector(listener);
//        //解决长按屏幕后无法拖动的现象
//        mGetstureDetector.setIsLongpressEnabled(false);
        scroller = new Scroller(context);
    }

    /**
     * 通过scroll 实现弹性滑动
     * 缺点：无法控制速度
     * 优点: 系统自带，没有调用一下view 性能最优
     *
     * @param destX
     * @param destY
     */
    public void smoothScrollTo(int destX, int destY) {
        int scroll = getScrollX();
        int delta = destX - scroll;
        //1s 内滑向delta,效果就是慢慢滑动
        scroller.startScroll(scroll, 0, delta, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller != null) {
            if (scroller.computeScrollOffset()) {
                scrollTo(scroller.getCurrX(), scroller.getCurrY());
                postInvalidate();
            }
        }
    }

    int startX = 0;
    int deltaX = 200;

    /**
     * 通过动画实现弹性滑动
     * 可控制滑动的速度 控制滑动时间
     */
    public void animatorScroll() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                scrollTo(startX + (int) (deltaX * animatedFraction), startX);
            }
        });
        animator.start();
    }

    /**
     * 使用延时策略实现弹性滑动
     */
    public void delaySceoll() {
        handler.sendEmptyMessage(MESSAGE_SCROLL_TO);
    }

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private int mCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount < FRAME_COUNT) {
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        scrollTo(scrollX, 0);
                        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    // 记录移动的位置
    int mLastX = 0;
    int mLasty = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int detalX = x - mLastX;
                int detalY = y - mLasty;
                int translationX = (int) (ViewHelper.getTranslationX(this) + detalX);
                int translationY = (int) (ViewHelper.getTranslationY(this) + detalY);
                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastX = x;
        mLasty = y;
        return true;
    }

    GestureDetector.OnGestureListener listener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };
}
