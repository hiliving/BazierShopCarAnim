package com.practice.dev.baziershopcaranim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by HY on 2017/4/7.
 */

public class BazierAnimView extends FrameLayout {
    public BazierAnimView(Context context) {
        this(context,null);
    }

    public BazierAnimView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BazierAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //PointF等价于float[]数组，里面存放的x和y的值 (point相当于int[])
    private PointF mLocation = new PointF();//这样创建出来，里面还没有值

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取当前父布局在界面的屏幕坐标(也就是父布局左上角坐标)
        int[] layoutLoc = new int[2];
        getLocationInWindow(layoutLoc);
        mLocation.set(layoutLoc[0], layoutLoc[1]);//将父布局左上角的值赋值给mLocation
    }

    /**
     * 开始贝塞尔动画
     *
     * @param startView    动画从哪个view开始（+号）
     * @param endView      动画在哪个view结束(购物车)
     * @param layoutIdMove 动画作用的移动控件(钱袋子的布局)
     */
    public void startCartAnim(View startView, View endView, int layoutIdMove) {
        //1，开始位置
        int[] startLoc = new int[2];
        startView.getLocationInWindow(startLoc);//获取当前view在屏幕上的坐标
        PointF startF = new PointF(startLoc[0] - mLocation.x, startLoc[1] - mLocation.y);//得到当前view相对于父布局左上角位置的坐标
        // 2，结束位置
        int[] endLoc = new int[2];
        endView.getLocationInWindow(endLoc);
        final PointF endF = new PointF(endLoc[0] - mLocation.x, endLoc[1] - mLocation.y);
        //3.移动控件。inflate()参数：作用布局，参考布局，false
        final View moveView = LayoutInflater.from(getContext()).inflate(layoutIdMove, this, false);
        //开始动画  使用属性动画合集
        AnimatorSet set = new AnimatorSet();
        //缩小动画
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(moveView, "scaleX", 1.0f, 0.3f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(moveView, "scaleY", 1.0f, 0.3f);
        //路径动画(baisaier曲线路径，开始坐标，结束坐标)
        ValueAnimator pathAnim = ObjectAnimator.ofObject(beisaier, startF, endF);
        pathAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新坐标
                PointF newPointF = (PointF) animation.getAnimatedValue();
                moveView.setX(newPointF.x);
                moveView.setY(newPointF.y);
            }
        });
        //将这些动画放入集合中
        set.playTogether(scaleXAnim, scaleYAnim, pathAnim);
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                BazierAnimView.this.addView(moveView);//加入动画作用的控件
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                BazierAnimView.this.removeView(moveView);//移除动画作用的控件
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
        set.addListener(listener);//动画播放监听器
        set.setDuration(1000);  //运动时间
        set.start();
    }

    //路径计算器
    private TypeEvaluator<PointF> beisaier = new TypeEvaluator<PointF>() {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //返回变化的轨迹坐标
            PointF newF = new PointF((startValue.x + endValue.x) / 2, 0);//控制点
            return BezierCurve.bezier(fraction, startValue, newF, endValue);
        }
    };

}
