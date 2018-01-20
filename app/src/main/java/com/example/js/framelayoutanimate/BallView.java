package com.example.js.framelayoutanimate;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by JS on 2017/7/25.
 */

public class BallView extends View {
    private Paint mPaint;
    private float ballPointX;
    private float ballPointY;
    private int minHeight;
    private int ballRadius = 10;
    private int mDragHeight = 800;
    private float progress;
    private Path mPath;
    private Paint mPathPaint;
    private int mTargetWidth;
    private float mTargetGravityHeight=200  ;
    //最大弧度
    private float mTangentAngle=120;
    private float progress_x;


    public BallView(Context context, Paint mPaint) {
        super(context);
        init();
    }

    public BallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Paint mPaint) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mPath.moveTo(0, 0);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        mPathPaint=paint;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawCircle(ballPointX, ballPointY, ballRadius, mPaint);

        canvas.drawPath(mPath, mPathPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int MeasureWidth = 0;
        int MeasureHeight = 0;

        int minWith = 2 * ballRadius + getPaddingLeft() + getPaddingRight();
        int minHeight = (int) ((mDragHeight * progress) + getPaddingTop() + getPaddingBottom());
        if (widthMode == MeasureSpec.AT_MOST) {
            MeasureWidth = Math.min(minWith, width);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            MeasureWidth = width;
        } else {
            MeasureWidth = minWith;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            MeasureHeight = Math.min(minHeight, height);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            MeasureHeight = height;
        } else {
            MeasureHeight = minHeight;
        }
        setMeasuredDimension(MeasureWidth, MeasureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePathLayout();
    }


    public void setProgres(float progress_x,float progress_y) {
        this.progress = progress_y;
        this.progress_x=progress_x;
        requestLayout();
    }

    public  void updatePathLayout()
    {
        final float progress=this.progress;
        final Path path=mPath;
        path.reset();
        //获取可绘制的区域高度
        final float h=getValueByLine(0,mDragHeight,progress);//当成控制点的位置
        final float w=getWidth();

        //控制点的坐标
        float lControlPointX,lControlPointY;
        //结束点
        float lEndPointX,lEndPointY;
        lEndPointY=h;
        lEndPointX=w/2;
        //更新圆的坐标,在结束点的下面
        ballPointY=lEndPointY-ballRadius;
        ballPointX=lEndPointX;
        //控制点Y坐标
        lControlPointY=getValueByLine(0,mTargetGravityHeight,progress);
        lControlPointX=getValueByLine(0,getWidth(),progress_x);
        //设置起始点为0,0

        path.moveTo(0,0);
        //贝赛尔曲线
        path.quadTo(lControlPointX,lControlPointY,w,0);
    }

    private float getValueByLine(float start,float end,float progress)
    {
        return start+(end-start)*progress;
    }

    ValueAnimator valueAnimator;
    public void release()
    {
            if(valueAnimator==null)
            {
                ValueAnimator animator=ValueAnimator.ofFloat(progress,0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(400);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object val = animation.getAnimatedValue();
                        if(val instanceof Float)
                        {
                            setProgres((Float) val,(Float) val);
                        }
                    }
                });
                valueAnimator=animator;
            }else
            {
                valueAnimator.cancel();
                valueAnimator.setFloatValues(progress,0);
            }
        valueAnimator.start();
    }
}
