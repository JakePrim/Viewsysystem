package example.viewsystem.linksu.com.viewsysystem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：7/5 0005
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class XHorizontalScrollView extends ViewGroup {

    private int mChildWidth;//子view的宽度
    private int mChildIndex;//滑动到第几个子view
    private int mChildSize;//有多少个子view
    //记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    //记录上次滑动的坐标(interceptTouchEvent)
    private int mInterceptX = 0;
    private int mInterceptY = 0;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker; //滑动速度跟踪

    public XHorizontalScrollView(Context context) {
        this(context, null);
    }

    public XHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    isIntercept = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int detalX = x - mInterceptX;
                int detalY = y - mInterceptY;
                if (Math.abs(detalX) > Math.abs(detalY)) {
                    // 表示为横向滑动 外部控件拦截事件
                    isIntercept = true;
                } else {
                    // 表示为竖向滑动 内部控件 --》 拦截事件
                    isIntercept = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        mInterceptY = y;
        mInterceptX = x;
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int detalX = x - mLastX;
                int detalY = y - mLastY;
                scrollBy(-detalX, 0);
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX(); //滑动的距离
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();// 水平滑动的速度
                if (Math.abs(xVelocity) >= 50) { // 水平滑动速度大于 50
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;//
                }
//                mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;//
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHight = 0;
        int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int mWidthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int hightMeasureSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int hightMeasureSpaceMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) { //如果没有子view 直接给0
            setMeasuredDimension(measureWidth, measureHight);
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST && hightMeasureSpaceMode == MeasureSpec.AT_MOST) {
            //当宽和高 都设置成ward_parent
            View childAt = getChildAt(0);
            measureWidth = childAt.getMeasuredWidth() * childCount;
            measureHight = childAt.getMeasuredHeight();
            setMeasuredDimension(measureWidth, measureHight);
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST) {
            View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measureWidth, hightMeasureSpaceSize);
        } else if (hightMeasureSpaceMode == MeasureSpec.AT_MOST) {
            measureHight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(mWidthMeasureSpecSize, measureHight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childCount = getChildCount();
        mChildSize = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != GONE) {
                int measuredWidth = childAt.getMeasuredWidth();
                mChildWidth = measuredWidth;
                childAt.layout(childLeft, 0, childLeft + measuredWidth, childAt.getMeasuredHeight());
                childLeft += measuredWidth;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.clear();
        super.onDetachedFromWindow();
    }
}
