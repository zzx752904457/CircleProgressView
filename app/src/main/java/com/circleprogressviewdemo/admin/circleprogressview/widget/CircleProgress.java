package com.circleprogressviewdemo.admin.circleprogressview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.math.BigDecimal;

/**
 * Created by admin on 2016/7/15.
 */
public class CircleProgress extends View {
    private int width;// 控件的宽度
    private int height;// 控件的高度
    private int radius;// 圆形的半径
    private int socktwidth = dpTopx(10);// 圆环进度条的宽度
    private Rect rec = new Rect();
    private String value = "0.00";// 百分比0~100;
    private float textSize = dpTopx(10);// 文字大小
    private Bitmap bitmap;
    @Deprecated
    float scale = 0.15f;// 中间背景图片相对圆环的大小的比例
    private int preColor = Color.parseColor("#2c2200");// 进度条未完成的颜色
    private float paddingscale = 1f;// 控件内偏距占空间本身的比例
    private int circleColor = Color.parseColor("#CCCCCC");// 圆中间的背景颜色
    private int textColor = Color.parseColor("#6bb849");// 文字颜色
    private int startAngle = 270;//进度条开始时的角度
    RectF rectf = new RectF();
    private int[] colors = {Color.GREEN, Color.YELLOW, Color.WHITE, Color.RED};//进度条颜色
    private Paint paint = new Paint();
    private Paint paint1 = new Paint();

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        int size = height = getHeight();
        if (height > width)
            size = width;
        radius = (int) (size * paddingscale / 2f);
        paint.setAntiAlias(true);
        paint.setColor(preColor);
        // 绘制最大的圆 进度条圆环的背景颜色（未走到的进度）
        canvas.drawCircle(size / 2, size / 2, radius, paint);

        // 绘制扇形图
        paint1.setAntiAlias(true);
        if (colors.length == 0) {
            paint1.setColor(Color.GREEN);
        } else if (colors.length == 1) {
            paint1.setColor(colors[0]);
        } else {
            //设置渐变为旋转型渐变
            Shader mShader = new SweepGradient(size / 2, size / 2,
                    colors,
                    null);
            //设置放置shader的矩阵
            Matrix matrix = new Matrix();
            //设置渐变色起点
            matrix.setRotate(startAngle, size / 2, size / 2);
            mShader.setLocalMatrix(matrix);
            paint1.setShader(mShader);
        }
        rectf.set(0, 0,
                size,
                size);
        canvas.drawArc(rectf, startAngle, Float.parseFloat(value) * 3.6f, true, paint1);


        // 绘制小圆
        paint.setColor(circleColor);
        canvas.drawCircle(size / 2, size / 2, radius - socktwidth, paint);

        String v = value + "%";
        paint.setColor(textColor);
        paint.setTextSize(spTopx(textSize));
        paint.getTextBounds(v, 0, v.length(), rec);
        int textwidth = rec.width();
        int textheight = rec.height();
        // 绘制中间文字
        canvas.drawText(v, (size - textwidth) / 2,
                ((size + textheight) / 2), paint);

        super.onDraw(canvas);

    }

    public int dpTopx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public int spTopx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * @param percent
     */
    //外部设置百分比数
    public void setPercent(float percent, int duration) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }

        setCurPercent(percent, duration);
    }

    //设置进度条渐变色数组\
    public CircleProgress setProgressColor(int[] colors) {
        this.colors = colors;
        return this;
    }

    //内部设置百分比 用于动画效果
    private void setCurPercent(float percent, int duration) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, percent);
        valueAnimator.setDuration(duration);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BigDecimal decimal = new BigDecimal(animation.getAnimatedValue() + "");
                //保留一位小数,并四舍五入
                value = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                CircleProgress.this.invalidate();
            }
        });
    }

    /**
     * 设置圆环进度条的宽度 px
     */
    public CircleProgress setProgressWidth(int width) {
        this.socktwidth = dpTopx(width);
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param value
     */
    public CircleProgress setTextSize(float value) {
        this.textSize = value;
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public CircleProgress setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    /**
     * 设置进度条之前的颜色
     *
     * @param precolor
     */
    public CircleProgress setPreProgress(int precolor) {
        this.preColor = precolor;
        return this;
    }

    /**
     * 设置圆心中间的背景颜色
     *
     * @param color
     * @return
     */
    public CircleProgress setCircleColor(int color) {
        this.circleColor = color;
        return this;
    }

    /**
     * 设置开始的位置
     *
     * @param startAngle 0~360
     *                   <p/>
     *                   ps 0代表在最右边 90 最下方 按照顺时针旋转
     */
    public CircleProgress setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        return this;
    }
}
