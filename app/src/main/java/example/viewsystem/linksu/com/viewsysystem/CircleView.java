package example.viewsystem.linksu.com.viewsysystem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：7/5 0005
 * 描    述：规范的自定义view
 * 修订历史：
 * ================================================
 */
public class CircleView extends View {

    private static final String TAG = "CircleView";
    private int mColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mHight = 200;
    private int mWidth = 200;

    public CircleView(Context context) {
        this(context, null);
        Log.e(TAG, "CircleView: 第一个构造方法");
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0); // 注意super 改成 this，才会走第三个构造函数，否则就会走父类的 方法无法走第三个构造函数
        Log.e(TAG, "CircleView: 第二个构造方法");
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG, "CircleView: 第三个构造方法");
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = array.getColor(R.styleable.CircleView_circle_color, Color.RED);
        array.recycle();
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingBottom - paddingTop;
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int WidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && hightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, hightSize);
        } else if (hightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WidthSize, mHight);
        }
    }
}
