package com.circleprogressviewdemo.admin.circleprogressview;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.circleprogressviewdemo.admin.circleprogressview.widget.CircleProgress;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private TextView mButton;
    private CircleProgress mCircleProgress;
    private ValueAnimator valueAnimator;
    private float x;
    private float y;
    private boolean canRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv.post(new Runnable() {
            @Override
            public void run() {
                x = tv.getX();
                y = tv.getY();
            }
        });
        canRepeat = true;
        valueAnimator = ValueAnimator.ofFloat();
        valueAnimator.setDuration(2000);
        valueAnimator.setObjectValues(new PointF(0, 0));
//        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                canRepeat = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                canRepeat = true;
                tv.setX(x);
                tv.setY(y);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                tv.setX(point.x);
                tv.setY(point.y);
            }
        });

        mCircleProgress = (CircleProgress) findViewById(R.id.circleView);
        float n = (float) (Math.random() * 100);
        mCircleProgress.setTextSize(18)
                .setProgressWidth(10)
                .setTextColor(Color.parseColor("#ff6600"))
                .setProgressColor(new int[]{Color.parseColor("#84FF84"), Color.parseColor("#43D329"), Color.YELLOW, Color.parseColor("#ff6600"), Color.parseColor("#DB354B"), Color.RED})
                .setCircleColor(Color.parseColor("#ffffff"))
                .setPreProgress(Color.parseColor("#eeeeee"))
                .setPercent(n, 2000);
        mButton = (TextView) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float n = (float) (Math.random() * 100);
                mCircleProgress.setPercent(n, 2000);
                mButton.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.btn_anim));
                if (canRepeat) {
                    valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {

                        @Override
                        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                            PointF point = new PointF();
                            point.x = (mButton.getX() + mButton.getWidth() / 2 - tv.getWidth() / 2 - tv.getX()) * fraction + tv.getX();

                            point.y = (mButton.getY() - tv.getY()) * fraction * fraction + tv.getY();
                            return point;
                        }
                    });
                    valueAnimator.start();
                }
            }
        });
    }
}
